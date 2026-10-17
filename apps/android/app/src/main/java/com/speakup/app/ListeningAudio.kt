package com.speakup.app

import android.media.AudioAttributes
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.Locale

@Composable
internal fun ListeningAudio(phrase: String) {
    val context = LocalContext.current.applicationContext
    val owner = LocalLifecycleOwner.current
    var ready by remember(phrase) { mutableStateOf(false) }
    var speaking by remember(phrase) { mutableStateOf(false) }
    var message by remember(phrase) { mutableStateOf("Preparing English voice…") }
    var slow by remember { mutableStateOf(false) }
    var engine by remember(phrase) { mutableStateOf<TextToSpeech?>(null) }
    val session = remember(phrase) { ListeningSession {
        ready = it.ready
        speaking = it.speaking
        message = it.message
    } }

    DisposableEffect(phrase, owner) {
        val handler = Handler(Looper.getMainLooper())
        var disposed = false
        var tts: TextToSpeech? = null
        fun stop() {
            session.stop()
            tts?.stop()
        }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) stop()
        }
        owner.lifecycle.addObserver(observer)
        try {
            tts = TextToSpeech(context) { status ->
                // Always post: initialization may finish before the constructor returns.
                handler.post {
                    if (!disposed) {
                        val available = status == TextToSpeech.SUCCESS &&
                            (tts?.setLanguage(Locale.US) ?: TextToSpeech.LANG_NOT_SUPPORTED) >= 0
                        session.initialized(available)
                    }
                }
            }
            tts?.setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build())
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) = Unit
                override fun onDone(utteranceId: String?) { handler.post { session.finished(utteranceId) } }
                @Deprecated("Required by Android")
                override fun onError(utteranceId: String?) { handler.post { session.finished(utteranceId, true) } }
                override fun onError(utteranceId: String?, errorCode: Int) { onError(utteranceId) }
            })
            engine = tts
        } catch (_: RuntimeException) {
            session.initialized(false)
        }
        onDispose {
            disposed = true
            session.close()
            owner.lifecycle.removeObserver(observer)
            tts?.stop()
            tts?.shutdown()
            handler.removeCallbacksAndMessages(null)
        }
    }

    Text("Listen to the sentence, then answer. Replay or reveal the transcript whenever you need help.")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(enabled = ready, onClick = {
            if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                val token = session.play()
                if (token != null) {
                    val tts = engine
                    val rate = tts?.setSpeechRate(if (slow) 0.75f else 1f)
                    if (rate != TextToSpeech.SUCCESS ||
                        tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, token) != TextToSpeech.SUCCESS) {
                        session.finished(token, true)
                    }
                }
            }
        }) { Text(if (speaking) "Replay" else "Play sentence") }
        OutlinedButton(enabled = speaking, onClick = { session.stop(); engine?.stop() }) { Text("Stop") }
    }
    Row {
        Checkbox(checked = slow, onCheckedChange = { slow = it; session.stop(); engine?.stop() })
        Text("Slower speech")
    }
    Text(message, style = MaterialTheme.typography.bodyMedium)
}
