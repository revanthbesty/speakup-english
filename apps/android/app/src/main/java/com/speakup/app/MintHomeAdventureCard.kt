package com.speakup.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

/** Display-only suggestion from existing verified lesson completion count. No XP mutation. */
@Composable
internal fun MintHomeAdventureCard(completedLessons: Int, onExploreGames: () -> Unit) {
    val adventure = MintPetLearningEngine.suggest(
        MintPetLearningSnapshot(
            completedLessons = completedLessons,
            recentIncorrectAttempts = 0,
            recentCorrectAttempts = 0
        )
    )
    MintSectionCard("Mintverse mini-adventure") {
        Text(adventure.message, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(adventure.adventure.title,
            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(adventure.adventure.description,
            color = MaterialTheme.colorScheme.onPrimaryContainer)
        MintPrimaryAction("Explore Games", onClick = onExploreGames)
    }
}
