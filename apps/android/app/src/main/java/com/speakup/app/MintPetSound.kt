package com.speakup.app

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator

/** Opt-in, offline preview cues for future Mint Pets; never play over quiet mode. */
internal object MintPetSound {
    enum class Cue { GREETING, ENCOURAGEMENT, ACHIEVEMENT }

    fun play(context: Context, enabled: Boolean, cue: Cue) {
        if (!enabled) return
        val audio = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
        if (audio.ringerMode != AudioManager.RINGER_MODE_NORMAL) return
        if (audio.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) return
        val tone = when (cue) {
            Cue.GREETING -> ToneGenerator.TONE_PROP_ACK
            Cue.ENCOURAGEMENT -> ToneGenerator.TONE_PROP_PROMPT
            Cue.ACHIEVEMENT -> ToneGenerator.TONE_PROP_BEEP2
        }
        // Short foreground UI cue only; no automatic looping or audio focus theft.
        val player = runCatching { ToneGenerator(AudioManager.STREAM_MUSIC, 35) }.getOrNull()
            ?: return
        try {
            player.startTone(tone, 120)
        } finally {
            // ToneGenerator owns its playback; the cue is intentionally short.
            player.stopTone()
            player.release()
        }
    }
}
