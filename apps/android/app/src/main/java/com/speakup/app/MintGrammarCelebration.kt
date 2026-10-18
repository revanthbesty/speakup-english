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

/** An accessible, non-punitive set result. Count is based on first checks, not XP. */
@Composable
internal fun MintGrammarCelebration(
    setNumber: Int,
    firstTryCorrect: Int,
    questionsWithMistakes: Int,
    newBloom: Boolean,
    reduceMotion: Boolean
) {
    var revealed by remember(setNumber) { mutableStateOf(false) }
    LaunchedEffect(setNumber) { revealed = true }
    val zoom by animateFloatAsState(if (revealed || reduceMotion) 1f else 0.8f, label = "Mint Bloom celebration")
    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text("🎉 Grammar Set $setNumber completed!", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(if (newBloom) "🌸✨" else "🌸", modifier = Modifier.scale(if (reduceMotion) 1f else zoom),
                style = MaterialTheme.typography.displayLarge)
            Text(if (newBloom) "Grammar Bloom unlocked!" else "Your Grammar Bloom is already growing",
                style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text("✅ $firstTryCorrect correct on first try · 🔁 $questionsWithMistakes needed another try",
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(if (questionsWithMistakes > 0) "Review the explanations below, then try the next set."
                else "Wonderful work! Ready to grow another Bloom?",
                color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
