package com.speakup.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun MintRealWorldQuestCelebration(
    title: String, newlyEarned: Boolean, speakingMode: String, reducedMotion: Boolean
) {
    var appeared by remember(title) { mutableStateOf(false) }
    LaunchedEffect(title) { appeared = true }
    val bloomScale by animateFloatAsState(
        targetValue = if (appeared || reducedMotion) 1f else 0.8f, label = "Quest Bloom reveal"
    )
    Surface(color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("🎉 $title completed!", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text("🌸✨", style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.scale(if (reducedMotion) 1f else bloomScale))
            Text(if (newlyEarned) "Real-World Mint Bloom collected!"
                 else "Previously completed · Your Bloom is already saved",
                style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(when (speakingMode) {
                "voice" -> "You tried a spoken reply. No pronunciation score is claimed."
                "text" -> "You completed the optional reply using quiet text."
                else -> "You skipped speaking safely. No voice attempt is recorded."
            }, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
