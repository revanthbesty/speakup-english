package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/** Riddles presentation only. Award checks and persistence stay with the caller. */
@Composable
internal fun MintRiddlesScreen(
    index: Int,
    total: Int,
    question: String,
    options: List<String>,
    answer: String,
    explanation: String,
    selectedOption: String,
    checked: Boolean,
    awardsCount: Int,
    latestActivityXp: Int,
    reducedMotion: Boolean,
    accentTextColor: Color,
    primaryTextColor: Color,
    onSelectOption: (String) -> Unit,
    onCheck: () -> Unit,
    onCompletionNext: () -> Unit,
    onSkipNext: () -> Unit,
    onDashboard: () -> Unit
) {
    val correct = selectedOption == answer
    Text("Riddle ${index + 1} of $total", color = accentTextColor,
        fontWeight = FontWeight.Bold)
    Text(question, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
    options.forEach { option ->
        OutlinedButton(onClick = { onSelectOption(option) }, modifier = Modifier.fillMaxWidth()) {
            Text((if (selectedOption == option) "✓ " else "") + option)
        }
    }
    if (checked) {
        Text(if (correct) "Correct! $explanation" else "Try again. Think about the clue.",
            color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
        MintPetAnswerReaction(checked = checked, correct = correct, reducedMotion = reducedMotion)
    }
    Button(onClick = onCheck, enabled = selectedOption.isNotBlank()) { Text("Check answer") }
    Text("Riddles mastered: $awardsCount / $total • +10 XP per new correct riddle",
        color = accentTextColor)
    if (checked && correct) {
        MintActivityCompletion(
            activity = "Riddles",
            newXp = latestActivityXp,
            reducedMotion = reducedMotion,
            nextLabel = "Next riddle →",
            onNext = onCompletionNext,
            onDashboard = onDashboard
        )
    }
    OutlinedButton(onClick = onSkipNext, modifier = Modifier.fillMaxWidth()) {
        Text("Next riddle →")
    }
}
