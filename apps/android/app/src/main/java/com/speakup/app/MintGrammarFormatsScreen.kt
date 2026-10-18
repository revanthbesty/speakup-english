package com.speakup.app

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
internal fun MintGrammarFormatsScreen() {
    var selectedFormat by remember { mutableStateOf(MintGrammarFormat.DIALOGUE) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var checked by remember { mutableStateOf(false) }
    var ttsAvailable by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val tts = remember(context) {
        var engine: TextToSpeech? = null
        engine = TextToSpeech(context) { code ->
            ttsAvailable = code == TextToSpeech.SUCCESS
            if (ttsAvailable) engine?.language = Locale.ENGLISH
        }
        engine
    }
    DisposableEffect(tts) { onDispose { tts.stop(); tts.shutdown() } }
    val item = MintGrammarFormats.samples.first { it.format == selectedFormat }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("🎭 Grammar Formats · offline pilot", style = MaterialTheme.typography.titleLarge)
        Text("Three original sample activities. They are not an approved exam or imported question bank.")
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            MintGrammarFormat.entries.forEach { format ->
                FilterChip(
                    selected = selectedFormat == format,
                    onClick = { selectedFormat = format; selectedAnswer = -1; checked = false },
                    label = { Text(when(format) {
                        MintGrammarFormat.DIALOGUE -> "Dialogue"
                        MintGrammarFormat.LISTENING -> "Listening"
                        MintGrammarFormat.VISUAL_CUE -> "Visual"
                    }) }
                )
            }
        }
        if (selectedFormat == MintGrammarFormat.LISTENING) {
            Button(onClick = { tts.speak(item.cue,TextToSpeech.QUEUE_FLUSH,null,"mint-grammar-s32") },
                enabled = ttsAvailable, modifier = Modifier.fillMaxWidth()) { Text("🔊 Play device voice") }
            Text(if (ttsAvailable) "Listen, or read the transcript below."
                else "Speech engine unavailable. Read the transcript below instead.")
        }
        Text(item.cue, style = MaterialTheme.typography.titleMedium)
        Text(item.prompt)
        item.options.forEachIndexed { index, value ->
            OutlinedButton(onClick = { selectedAnswer = index; checked = false },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text("${if (selectedAnswer == index) "●" else "○"} ${('A' + index)}. $value",
                    modifier = Modifier.fillMaxWidth())
            }
        }
        Button(onClick = { checked = true }, enabled = selectedAnswer >= 0,
            modifier = Modifier.fillMaxWidth()) { Text("Check answer") }
        if (checked) {
            Text(if (MintGrammarFormats.checked(item,selectedAnswer)) "Correct!" else "Try another answer.")
            Text(item.explanation)
        }
        Text("This practice preview does not award mastery or Mint Blooms.")
    }
}
