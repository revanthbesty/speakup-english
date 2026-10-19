package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Profile overview owns no persisted state, XP calculations, or navigation routes. */
@Composable
internal fun MintProfileOverview(
    profileName: String,
    journey: String,
    difficulty: String,
    dailyGoalMinutes: Int,
    xp: Int,
    completedLessons: Int,
    lessonCount: Int,
    surfaceColor: Color,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    onReview: () -> Unit,
    onHelp: () -> Unit,
    settingsExpanded: Boolean,
    onToggleSettings: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("🌿", style = MaterialTheme.typography.displayMedium)
            Column {
                Text("My learning space", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Your goals and achievements", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
    Text(if (profileName.isBlank()) "Your learning space" else "Learning as $profileName",
        color = secondaryTextColor)
    Text("$journey · $difficulty · $dailyGoalMinutes minutes per day", color = secondaryTextColor)
    Surface(color = surfaceColor, shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Your progress", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, color = primaryTextColor)
            Text("$xp XP earned · $completedLessons of $lessonCount lessons completed",
                color = secondaryTextColor)
            LinearProgressIndicator(
                progress = { completedLessons.toFloat() / lessonCount.coerceAtLeast(1) },
                modifier = Modifier.fillMaxWidth())
            OutlinedButton(onClick = onReview,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text("View progress & achievements →")
            }
        }
    }
    OutlinedButton(onClick = onHelp,
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
        Text("Help & guidance →")
    }
    Button(onClick = onToggleSettings, modifier = Modifier.fillMaxWidth()) {
        Text(if (settingsExpanded) "Close settings" else "Edit profile and settings →")
    }
}
