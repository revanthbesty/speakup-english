package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun MintFiveWordStoryScreen(age: Int, onBack: () -> Unit, onSpeaking: (String) -> Unit) {
    val packs = remember(age) { MintFiveWordCatalog.visible(age) }
    var packIndex by rememberSaveable { mutableIntStateOf(0) }
    var sceneIndex by rememberSaveable { mutableIntStateOf(0) }
    var selected by rememberSaveable { mutableStateOf("") }
    var checked by rememberSaveable { mutableStateOf(false) }
    var showWords by rememberSaveable { mutableStateOf(false) }
    val pack = packs.getOrNull(packIndex.coerceIn(0, (packs.size - 1).coerceAtLeast(0)))
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack) { Text("← Clips") }
        Text("Five-Word Story", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        if (pack == null) {
            Text("No age-appropriate stories are available yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Text("${packIndex + 1} of ${packs.size} · ${pack.title}", style = MaterialTheme.typography.titleLarge)
            Text("Today's five words", fontWeight = FontWeight.Bold)
            Text(pack.words.joinToString(" • "), color = MaterialTheme.colorScheme.primary)
            OutlinedButton(onClick = { showWords = !showWords }) { Text(if (showWords) "Hide word practice" else "Practise these words") }
            if (showWords) {
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        pack.words.forEach { word -> Text("• $word", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        Text("Say each word aloud, or read it quietly.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            val scene = pack.scenes[sceneIndex.coerceIn(0, pack.scenes.lastIndex)]
            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(scene.illustration, style = MaterialTheme.typography.displayLarge)
                    Text(scene.caption, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(scene.narration, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    LinearProgressIndicator(progress = { (sceneIndex + 1f) / pack.scenes.size }, modifier = Modifier.fillMaxWidth())
                    Text("Scene ${sceneIndex + 1} of ${pack.scenes.size}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            ListeningAudio(scene.narration)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { sceneIndex-- }, enabled = sceneIndex > 0, modifier = Modifier.weight(1f)) { Text("Previous") }
                Button(onClick = { sceneIndex++ }, enabled = sceneIndex < pack.scenes.lastIndex, modifier = Modifier.weight(1f)) { Text("Next scene") }
            }
            if (sceneIndex == pack.scenes.lastIndex) {
                Text("Story challenge", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(pack.question)
                pack.choices.forEach { option ->
                    OutlinedButton(onClick = { selected = option; checked = false }, modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selected == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)) {
                        Text(option, modifier = Modifier.fillMaxWidth())
                    }
                }
                Button(onClick = { checked = true }, enabled = selected.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Check answer") }
                if (checked) Text(if (selected == pack.correctAnswer) "Correct! Well done." else "Try again. You can revisit the story.")
                OutlinedButton(onClick = { onSpeaking("Retell '${pack.title}' using these words: ${pack.words.joinToString(", ")}.") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Speak or type your story (optional)")
                }
                if (packIndex < packs.lastIndex) Button(onClick = { packIndex++; sceneIndex = 0; selected = ""; checked = false; showWords = false }, modifier = Modifier.fillMaxWidth()) { Text("Next five-word story") }
                else Text("All caught up. Come back later or revisit a story.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
