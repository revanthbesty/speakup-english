package com.speakup.app

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Games category navigation; the caller continues to own game mode and filters. */
internal object MintGamesRecommendations {
    fun forGoal(learningGoal: String): List<String> = when (learningGoal) {
        "Start speaking" -> listOf("Dialogues", "Word Builder", "Listening")
        "School & exams" -> listOf("Grammar", "Reading", "Vocabulary")
        "Work & interviews" -> listOf("Dialogues", "Situations", "Vocabulary")
        "Reading & writing" -> listOf("Reading", "Grammar", "Word Builder")
        "Travel" -> listOf("Dialogues", "Situations", "Listening")
        else -> listOf("Vocabulary", "Situations", "Word Builder")
    }

    val modes = listOf("Mint Quests", "Real-World Quests", "Co-op Stories",
        "Career & Exam", "Grammar Formats", "Word Builder", "Riddles",
        "Vocabulary", "Grammar", "Situations", "Listening", "Reading", "Dialogues")
}

@Composable
internal fun MintGamesFilterBar(
    expanded: Boolean,
    learningGoal: String,
    difficulty: String,
    selectedMode: String,
    accentTextColor: Color,
    secondaryTextColor: Color,
    onToggle: () -> Unit,
    onSelectMode: (String) -> Unit
) {
    TextButton(onClick = onToggle) {
        Text(if (expanded) "Hide all game categories ↑" else "Browse all games & levels →")
    }
    if (!expanded) return
    Text("Recommended for $learningGoal • $difficulty", color = accentTextColor,
        fontWeight = FontWeight.Bold)
    Text("Suggested: " + MintGamesRecommendations.forGoal(learningGoal).joinToString(" • "),
        color = secondaryTextColor)
    Text("Select a game to practise.", color = secondaryTextColor,
        style = MaterialTheme.typography.bodySmall)
    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MintGamesRecommendations.modes.forEach { mode ->
            FilterChip(selected = selectedMode == mode, onClick = { onSelectMode(mode) },
                label = { Text(mode) })
        }
    }
}
