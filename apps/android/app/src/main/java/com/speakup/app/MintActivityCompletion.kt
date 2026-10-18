package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Unified, truthful post-success guidance. XP is awarded by the existing once-only activity ledger. */
@Composable
internal fun MintActivityCompletion(
    activity: String,
    newXp: Int,
    nextLabel: String,
    onNext: () -> Unit,
    onDashboard: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("✓ $activity completed!", style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(if (newXp > 0) "+$newXp XP earned · Added to your Home dashboard"
                 else "Already mastered · No duplicate XP",
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(nextLabel) }
            OutlinedButton(onClick = onDashboard, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text("View my rewards on Home")
            }
        }
    }
}
