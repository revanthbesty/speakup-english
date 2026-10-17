package com.speakup.app

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Navy = Color(0xFF111C35)
private val Violet = Color(0xFF6C5CE7)
private val Cream = Color(0xFFF7F7FC)
private val Soft = Color(0xFFECE9FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PremiumSpeakUp() }
    }
}

@Composable
fun PremiumSpeakUp() {
    var page by remember { mutableStateOf("Today") }
    var selected by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val preferences = remember(context) { context.getSharedPreferences("speakup_learning", android.content.Context.MODE_PRIVATE) }
    var completed by remember { mutableStateOf(preferences.getBoolean("cafe_lesson_complete", false)) }
    var practiceResponse by remember { mutableStateOf("") }
    var practiceChecked by remember { mutableStateOf(false) }
    var xp by remember { mutableIntStateOf(preferences.getInt("total_xp", 0)) }
    var speakingAttempts by remember { mutableIntStateOf(preferences.getInt("speaking_attempts", 0)) }
    var speechStatus by remember { mutableStateOf("") }
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val words = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!words.isNullOrBlank()) {
                practiceResponse = words
                practiceChecked = false
                speakingAttempts += 1
                preferences.edit().putInt("speaking_attempts", speakingAttempts).apply()
                speechStatus = "Speech captured. Review your words and check your reply."
            } else speechStatus = "No words were recognized. Please try again."
        } else speechStatus = "Speech recognition was cancelled or unavailable."
    }
    val answer = "I'd like a cup of coffee, please."
    MaterialTheme(colorScheme = lightColorScheme(primary = Violet, background = Cream)) {
        Column(Modifier.fillMaxSize().background(Cream)) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("speakup.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Text("ENGLISH", color = Violet, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.height(24.dp))
                when (page) {
                    "Today" -> {
                        Text("Your English journey", style = MaterialTheme.typography.headlineMedium, color = Navy, fontWeight = FontWeight.Bold)
                        Text("Speak a little. Grow every day.", color = Color.DarkGray)
                        Spacer(Modifier.height(8.dp))
                        Text("$xp XP earned  •  $speakingAttempts speaking attempts", color = Violet, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(20.dp))
                        Surface(color = Navy, shape = RoundedCornerShape(26.dp)) {
                            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                                Text("TODAY'S SPEAKING MISSION", color = Color(0xFFBEB5FF), fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                Text("Order confidently at a café", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                Text("A practical 5-minute conversation challenge", color = Color(0xFFE4E3F6))
                                Spacer(Modifier.height(20.dp))
                                Button(onClick = { page = "Learn"; checked = false; selected = "" }, modifier = Modifier.fillMaxWidth()) { Text("Start my mission →") }
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Text("Explore your academy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(12.dp))
                        FeatureCard("✦  Speaking Studio", "Practise a café conversation", { page = "Speak" })
                        FeatureCard("▣  My Learning Path", "Build practical everyday English", { page = "Learn" })
                        FeatureCard("↻  Smart Revision", "Revisit what you have learned", { page = "Review" })
                    }
                    "Learn" -> {
                        Text("Everyday English", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
                        Text("LESSON 01 • CAFÉ CONVERSATIONS", color = Violet, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(18.dp))
                        Text("Which sentence would you say when ordering coffee?", color = Navy, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(16.dp))
                        listOf("I want cup coffee.", answer, "Me like coffee cup.").forEach { choice ->
                            OutlinedButton(onClick = { selected = choice; checked = false }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selected == choice) Soft else Color.White)) {
                                Text(choice, color = Navy, modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp))
                            }
                        }
                        if (checked) {
                            Spacer(Modifier.height(12.dp))
                            Text(if (selected == answer) "Excellent! This is a polite, natural request." else "Try again. 'I'd like...' is a polite way to order.", color = if (selected == answer) Color(0xFF177B57) else Color(0xFF9D5417))
                        }
                        Spacer(Modifier.height(20.dp))
                        Button(onClick = { if (checked && selected == answer) { if (!completed) { xp += 50; preferences.edit().putInt("total_xp", xp).apply() }; completed = true; preferences.edit().putBoolean("cafe_lesson_complete", true).apply(); page = "Review" } else checked = true }, enabled = selected.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
                            Text(if (checked && selected == answer) "Complete lesson" else "Check my answer")
                        }
                    }
                    "Speak" -> {
                        Text("Speaking Studio", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(12.dp))
                        Surface(color = Soft, shape = RoundedCornerShape(22.dp)) {
                            Column(Modifier.fillMaxWidth().padding(22.dp)) {
                                Text("CAFÉ ROLE-PLAY", color = Violet, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                Text("Barista: Hi! What would you like to order?", style = MaterialTheme.typography.titleLarge, color = Navy)
                                Spacer(Modifier.height(12.dp))
                                Text("Practice response: $answer", color = Navy)
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("Speak or type your reply. Recognition uses the speech service available on your device; AI pronunciation scoring is not yet connected.", color = Color.DarkGray)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                speechStatus = ""
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toLanguageTag())
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your reply to the barista")
                                }
                                try { speechLauncher.launch(intent) }
                                catch (_: android.content.ActivityNotFoundException) {
                                    speechStatus = "No speech recognition app is available on this device."
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("🎙 Speak my answer") }
                        if (speechStatus.isNotBlank()) Text(speechStatus, color = Color.DarkGray)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = practiceResponse,
                            onValueChange = { practiceResponse = it; practiceChecked = false },
                            label = { Text("Your reply to the barista") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                        if (practiceChecked) {
                            Spacer(Modifier.height(10.dp))
                            val naturalReply = practiceResponse.trim().contains("coffee", ignoreCase = true) &&
                                (practiceResponse.contains("please", ignoreCase = true) || practiceResponse.contains("I'd like", ignoreCase = true))
                            Text(
                                if (naturalReply) "Nice! Your reply sounds polite and clear."
                                else "Tip: Try 'I'd like a cup of coffee, please.'",
                                color = if (naturalReply) Color(0xFF177B57) else Color(0xFF9D5417)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { practiceChecked = true },
                            enabled = practiceResponse.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Check my reply") }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { page = "Learn" }, modifier = Modifier.fillMaxWidth()) { Text("Practice this sentence") }
                    }
                    "Review" -> {
                        Text("My Growth", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        FeatureCard(if (completed) "✓  First mission complete" else "◇  Your first mission awaits", if (completed) "Great start! Keep practising." else "Complete your first lesson to begin.", { page = "Learn" })
                        Spacer(Modifier.height(12.dp))
                        Text("Total XP: $xp  •  Speaking attempts: $speakingAttempts", color = Violet, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Your progress is saved on this device. Account sync and revision scheduling are planned.", color = Color.DarkGray)
                    }
                }
            }
            HorizontalDivider()
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Today", "Learn", "Speak", "Review").forEach { tab ->
                    TextButton(onClick = { page = tab }) { Text(tab, color = if (page == tab) Violet else Color.Gray) }
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(title: String, subtitle: String, onClick: () -> Unit) {
    Surface(color = Color.White, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shadowElevation = 2.dp) {
        Column(Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Navy, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.DarkGray)
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onClick) { Text("Explore →") }
        }
    }
}
