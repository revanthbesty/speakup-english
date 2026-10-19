package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Offline help. No contact, tickets or live customer support service is implied. */
@Composable
internal fun MintHelpScreen(onBack: () -> Unit, onGames: () -> Unit, onSpeak: () -> Unit, onClips: () -> Unit) {
    var topic by remember { mutableStateOf("Getting started") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onBack) { Text("← Profile") }
        MintScreenHeading("Help & guidance", "Simple answers for your learning journey.", "PROFILE • SUPPORT")
        Text("Choose a topic", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("Getting started", "Using games", "Speaking privately", "Stories & offline", "Privacy & support").forEach { item ->
                MintChoiceChip(item, selected = topic == item, onClick = { topic = item })
            }
        }
        Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(topic, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(when (topic) {
                    "Getting started" -> "Go to Home and tap Start today's adventure. You can practise for just a few minutes."
                    "Using games" -> "Choose a response and check it. Earned XP is recorded once per unique mastery."
                    "Speaking privately" -> "Microphone access is optional. Android speech recognition may use your device provider. Typing works without microphone access."
                    "Stories & offline" -> "The included illustrated stories are available without a video streaming service. Turn captions on in Clips."
                    else -> "Progress is stored on this device. Use Profile → Privacy & local data to inspect or export local history. Live customer-support tickets are not available."
                }, color = MaterialTheme.colorScheme.onSurfaceVariant)
                when (topic) {
                    "Using games" -> MintPrimaryAction("Open Games", onClick = onGames)
                    "Speaking privately" -> MintPrimaryAction("Open Speak", onClick = onSpeak)
                    "Stories & offline" -> MintPrimaryAction("Open Clips", onClick = onClips)
                }
            }
        }
    }
}
