package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Vocabulary presentation only. Verified mastery, XP and persistence remain in caller. */
@Composable
internal fun MintVocabularyScreen(
    index: Int,
    total: Int,
    word: String,
    choices: List<String>,
    meaning: String,
    example: String,
    selectedChoice: String,
    checked: Boolean,
    masteredCount: Int,
    latestActivityXp: Int,
    reducedMotion: Boolean,
    accentTextColor: Color,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    onSelectChoice: (String) -> Unit,
    onCheck: () -> Unit,
    onCompletionNext: () -> Unit,
    onSkipNext: () -> Unit,
    onDashboard: () -> Unit
) {
    val correct = selectedChoice == meaning
    Text("Vocabulary ${index + 1} of $total", color = accentTextColor,
        fontWeight = FontWeight.Bold)
    Text(word, style = MaterialTheme.typography.headlineMedium,
        color = primaryTextColor, fontWeight = FontWeight.Bold)
    Text("What does this word mean?", color = secondaryTextColor)
    Spacer(Modifier.height(8.dp))
    choices.forEach { choice ->
        OutlinedButton(onClick = { onSelectChoice(choice) }, modifier = Modifier.fillMaxWidth()) {
            Text((if (selectedChoice == choice) "✓ " else "") + choice)
        }
    }
    if (checked) {
        Text(if (correct) "Correct! $example" else "Not quite. Try another meaning.",
            color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
        MintPetAnswerReaction(checked = checked, correct = correct, reducedMotion = reducedMotion)
    }
    Button(onClick = onCheck, enabled = selectedChoice.isNotBlank()) { Text("Check meaning") }
    Text("Words mastered: $masteredCount / $total • +10 XP for each new word",
        color = accentTextColor)
    if (checked && correct) {
        MintActivityCompletion(
            activity = "Vocabulary",
            newXp = latestActivityXp,
            reducedMotion = reducedMotion,
            nextLabel = "Next word →",
            onNext = onCompletionNext,
            onDashboard = onDashboard
        )
    }
    OutlinedButton(onClick = onSkipNext, modifier = Modifier.fillMaxWidth()) {
        Text("Next word →")
    }
}
