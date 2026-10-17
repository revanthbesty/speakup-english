package com.speakup.app

import android.os.Bundle
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
    var completed by remember { mutableStateOf(false) }
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
                        FeatureCard("✦  AI Speaking Studio", "Practice real-life conversations", { page = "Speak" })
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
                        Button(onClick = { if (checked && selected == answer) { completed = true; page = "Review" } else checked = true }, enabled = selected.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
                            Text(if (checked && selected == answer) "Complete lesson" else "Check my answer")
                        }
                    }
                    "Speak" -> {
                        Text("AI Speaking Studio", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
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
                        Text("Live AI voice conversation will be enabled after secure speech-service integration.", color = Color.DarkGray)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { page = "Learn" }, modifier = Modifier.fillMaxWidth()) { Text("Practice this sentence") }
                    }
                    "Review" -> {
                        Text("My Growth", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        FeatureCard(if (completed) "✓  First mission complete" else "◇  Your first mission awaits", if (completed) "Great start! Keep practising." else "Complete your first lesson to begin.", { page = "Learn" })
                        Spacer(Modifier.height(12.dp))
                        Text("Persistent mastery, revision scheduling and learning analytics will appear when account sync is connected.", color = Color.DarkGray)
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
