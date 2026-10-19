package com.speakup.app

import android.content.Context
import android.media.AudioManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioFocusRequest
import kotlin.math.PI
import kotlin.math.sin

/** Opt-in, offline preview cues for future Mint Pets; never play over quiet mode. */
internal object MintPetSound {
    enum class Cue { GREETING, ENCOURAGEMENT, ACHIEVEMENT }

    fun play(context: Context, enabled: Boolean, cue: Cue, petId: String = "") {
        if (!enabled) return
        val audio = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
        if (audio.ringerMode != AudioManager.RINGER_MODE_NORMAL) return
        if (audio.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) return
        // A soft two-note chime, generated locally; never blocks the UI thread.
        Thread {
            // Stable pitch families give every selected companion an identifiable sound.
            val transpose = when (petId) {
                "mint_fox" -> 1.0
                "buddy_panda" -> .75
                "spark_cat" -> 1.20
                "wise_owl" -> .90
                "joy_rabbit" -> 1.12
                "tiny_dragon" -> 1.34
                else -> 1.0
            }
            val notes = (when (cue) {
                Cue.GREETING -> doubleArrayOf(523.25, 659.25)
                Cue.ENCOURAGEMENT -> doubleArrayOf(440.0, 523.25)
                Cue.ACHIEVEMENT -> doubleArrayOf(659.25, 880.0)
            }).map { it * transpose }.toDoubleArray()
            val rate = 22050
            val samplesPerNote = rate / 10
            val samples = ShortArray(samplesPerNote * notes.size)
            for (i in samples.indices) {
                val noteIndex = i / samplesPerNote
                val t = (i % samplesPerNote).toDouble() / rate
                val fade = (1.0 - (i % samplesPerNote).toDouble() / samplesPerNote)
                samples[i] = (sin(2.0 * PI * notes[noteIndex] * t) * fade * 4200).toInt().toShort()
            }
            runCatching {
                val track = AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                    .setAudioFormat(AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(rate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                    .setBufferSizeInBytes(samples.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()
                try {
                    track.write(samples, 0, samples.size)
                    track.play()
                    Thread.sleep(250)
                } finally {
                    if (track.playState == AudioTrack.PLAYSTATE_PLAYING) track.stop()
                    track.release()
                }
            }
        }.start()
    }
}
