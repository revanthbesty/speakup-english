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
        MintProgressBar((step + 1) / 4f)
        Text("STEP ${step + 1} OF 4", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        when (step) {
            0 -> {
                MintSectionHeading("Welcome to English Mint", "Fresh Games. Fluent Voices.")
                MintDashboardHero(
                    kicker="Play • Practise • Speak",
                    title="Grow with English",
                    description="Small adventures, friendly practice and rewards you really earn.",
                    action="Get started →",
                    onAction={step=1}
                )
            }
            1 -> {
                Text("Who is learning?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Choose an age group for suitable content and privacy.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                listOf(
                    Triple("4–7", "Young learners", "🧒"),
                    Triple("8–12", "Children", "🎒"),
                    Triple("13–17", "Teenagers", "🎧"),
                    Triple("18–49", "Adults & professionals", "💼"),
                    Triple("50+", "Lifelong learners", "🌟")
                ).forEach { (band, label, symbol) ->
                    MintChoiceTile(label, "Ages $band", symbol, ageBand == band) { onAge(band) }
                }
                if (ageBand in listOf("4–7", "8–12", "13–17")) {
                    Text("Young learners stay private. A parent or guardian should guide setup. Public messaging is unavailable.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            2 -> {
                Text("What's your English goal?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Choose one. You can change it later.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                goals.forEachIndexed { index, option ->
                    val symbols = listOf("🎤","💬","📚","💼","✍","✈")
                    MintChoiceTile(option,"You can update this later",symbols[index],goal==option) {
                        onGoal(option)
                    }
                }
            }
            else -> {
                MintInfoBanner("You're all set! ✨", "Your first five-minute English adventure is ready.",MintVisualTokens.mint)
                Text("Your learning journey starts here",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
                OutlinedTextField(value = name, onValueChange = { onName(it.take(40)) }, singleLine = true, label = { Text("Name (optional)") }, modifier = Modifier.fillMaxWidth())
                Text("A short level check is optional. Skipping it won't affect your saved learning history.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (step == 3) {
            Button(onClick = { onFinish(false) }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Start today's adventure") }
            TextButton(onClick = { onFinish(true) }, modifier = Modifier.fillMaxWidth()) { Text("Take optional level check") }
        } else {
            if (step > 0) Button(onClick = { step++ }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) { Text("Continue →") }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { if (step > 0) step-- }, enabled = step > 0) { Text("Back") }
                TextButton(onClick = { step = 3 }) { Text("Skip optional steps") }
            }
        }
    }
}
