package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * First-run UI only. The Activity owns legacy profile persistence and placement scoring.
 * No microphone permission request occurs during setup.
 */
@Composable
internal fun MintWelcomeFlow(
    ageBand: String,
    goal: String,
    name: String,
    onAge: (String) -> Unit,
    onGoal: (String) -> Unit,
    onName: (String) -> Unit,
    onFinish: (Boolean) -> Unit
) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    val goals = listOf("Start speaking", "Everyday conversation", "School & exams", "Work & interviews", "Reading & writing", "Travel")
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(progress = { (step + 1) / 4f }, modifier = Modifier.fillMaxWidth())
        Text("STEP ${step + 1} OF 4", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        when (step) {
            0 -> {
                Text("🌿", style = MaterialTheme.typography.displayLarge, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Text("Welcome to English Mint", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Play a little. Speak with confidence. Grow every day.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Text("Your first adventure takes about five minutes.", modifier = Modifier.fillMaxWidth().padding(20.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            1 -> {
                Text("Who is learning?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Choose an age group for suitable content and privacy.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                listOf("4–7", "8–12", "13–17", "18–49", "50+").forEach { band ->
                    FilterChip(selected = ageBand == band, onClick = { onAge(band) }, label = { Text(band) }, modifier = Modifier.fillMaxWidth())
                }
                if (ageBand in listOf("4–7", "8–12", "13–17")) {
                    Text("Young learners stay private. A parent or guardian should guide setup. Public messaging is unavailable.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            2 -> {
                Text("What's your English goal?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Choose one. You can change it later.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                goals.forEach { option ->
                    FilterChip(selected = goal == option, onClick = { onGoal(option) }, label = { Text(option) }, modifier = Modifier.fillMaxWidth())
                }
            }
            else -> {
                Text("You're all set! ✨", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Your journey is ready. Try your first game, then speak when you feel ready.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(value = name, onValueChange = { onName(it.take(40)) }, singleLine = true, label = { Text("Name (optional)") }, modifier = Modifier.fillMaxWidth())
                Text("A short level check is optional. Skipping it won't affect your saved learning history.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (step == 3) {
            Button(onClick = { onFinish(false) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Start today's adventure") }
            TextButton(onClick = { onFinish(true) }, modifier = Modifier.fillMaxWidth()) { Text("Take optional level check") }
        } else {
            Button(onClick = { step++ }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text(if (step == 0) "Get started" else "Continue") }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { if (step > 0) step-- }, enabled = step > 0) { Text("Back") }
                TextButton(onClick = { step = 3 }) { Text("Skip optional steps") }
            }
        }
    }
}
