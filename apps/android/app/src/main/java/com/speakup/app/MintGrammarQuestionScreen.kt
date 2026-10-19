package com.speakup.app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Grammar question rendering. Caller retains checked state, revision tracking and once-only XP. */
@Composable
internal fun MintGrammarQuestionScreen(
    prompt: String,
    options: List<String>,
    correctAnswer: String,
    explanation: String,
    selectedChoice: String,
    checked: Boolean,
    darkAppearance: Boolean,
    reducedMotion: Boolean,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    accentTextColor: Color,
    softSurfaceColor: Color,
    surfaceColor: Color,
    masteredCount: Int,
    totalQuestions: Int,
    isLastQuestionInSet: Boolean,
    onSelectChoice: (String) -> Unit,
    onCheck: () -> Unit,
    onContinue: () -> Unit
) {
    val correct = selectedChoice == correctAnswer
    Surface(
        shape = RoundedCornerShape(26.dp),
        color = if (darkAppearance) Color(0xFF25334C) else Color(0xFFF0E7FF),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("✦  GRAMMAR CHALLENGE",
                color = if (darkAppearance) Color(0xFFB9A3FF) else Color(0xFF6D28D9),
                style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(prompt, style = MaterialTheme.typography.headlineSmall,
                color = primaryTextColor, fontWeight = FontWeight.Bold)
            Text("Choose the best answer", color = secondaryTextColor,
                style = MaterialTheme.typography.bodyMedium)
        }
    }
    options.forEachIndexed { index, choice ->
        val selected = selectedChoice == choice
        Surface(
            onClick = { if (!(checked && selected && choice == correctAnswer)) onSelectChoice(choice) },
            shape = RoundedCornerShape(20.dp),
            color = if (selected) {
                if (darkAppearance) Color(0xFF214F49) else Color(0xFFD4FAEA)
            } else surfaceColor,
            border = BorderStroke(if (selected) 2.dp else 1.dp,
                if (selected) Color(0xFF10B981)
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp).semantics {
                contentDescription = "Option ${index + 1} of ${options.size}: $choice. ${if (selected) "Selected" else "Not selected"}"
            }
        ) {
            Row(Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(('A' + index).toString() + ".",
                    color = if (selected) Color(0xFF10B981) else secondaryTextColor,
                    fontWeight = FontWeight.ExtraBold)
                Text(choice, color = primaryTextColor, modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge)
                if (selected) Text("✓", color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
            }
        }
    }
    if (checked) {
        Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(if (correct) "✓ Correct! Well done." else "Not quite — let's learn why.",
                    color = primaryTextColor, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text(if (correct) "Why this answer works" else "Why this answer needs another try",
                    color = accentTextColor, style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold)
                Text(explanation, color = primaryTextColor,
                    style = MaterialTheme.typography.bodyLarge)
                MintPetAnswerReaction(checked = checked, correct = correct, reducedMotion = reducedMotion)
                if (!correct) Text("Choose another answer above, then check again.",
                    color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    if (!(checked && correct)) {
        Button(onClick = onCheck, enabled = selectedChoice.isNotBlank()) { Text("Check grammar") }
    }
    Text("🌸 $masteredCount of $totalQuestions grammar blooms growing",
        color = accentTextColor)
    if (checked && correct) {
        MintPrimaryAction(
            label = if (isLastQuestionInSet) "Complete set →" else "Continue →",
            onClick = onContinue
        )
    }
}
