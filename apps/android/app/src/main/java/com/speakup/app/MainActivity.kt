package com.speakup.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val Ink = Color(0xFF17243D)
private val Purple = Color(0xFF6655E8)
private val Canvas = Color(0xFFF7F8FE)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SpeakUpApp() }
    }
}

@Composable
fun SpeakUpApp() {
    var page by remember { mutableStateOf("home") }
    var selected by remember { mutableStateOf<String?>(null) }
    var checked by remember { mutableStateOf(false) }
    val correct = selected == "I eat rice for breakfast."
    MaterialTheme(colorScheme = lightColorScheme(primary = Purple, background = Canvas, surface = Color.White)) {
        Column(Modifier.fillMaxSize().background(Canvas)) {
            Column(Modifier.weight(1f).padding(horizontal = 22.dp, vertical = 24.dp)) {
                Text("speakup", style = MaterialTheme.typography.headlineMedium, color = Purple, fontWeight = FontWeight.ExtraBold)
                Text("ENGLISH", style = MaterialTheme.typography.labelSmall, color = Ink)
                Spacer(Modifier.height(30.dp))
                when (page) {
                    "home" -> {
                        Text("Your English journey starts here ✨", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
                        Spacer(Modifier.height(12.dp))
                        Text("Small lessons. Real conversations. More confidence.", color = Color.DarkGray)
                        Spacer(Modifier.height(28.dp))
                        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFFEAE7FF)) {
                            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                                Text("TODAY'S MISSION", color = Purple, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                Text("Talk about your breakfast", style = MaterialTheme.typography.headlineSmall, color = Ink, fontWeight = FontWeight.Bold)
                                Text("Practice a useful everyday sentence.", color = Ink)
                                Spacer(Modifier.height(20.dp))
                                Button(onClick = { page = "lesson"; selected = null; checked = false }, modifier = Modifier.fillMaxWidth()) { Text("Start lesson →") }
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Text("Your learning path", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Ink)
                        Spacer(Modifier.height(10.dp))
                        Text("01  Everyday English  •  Start here", color = Ink)
                        Spacer(Modifier.height(10.dp))
                        Text("02  Conversation practice  •  Coming next", color = Color.Gray)
                    }
                    "lesson" -> {
                        Text("LESSON 01  •  EVERYDAY ENGLISH", color = Purple, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(18.dp))
                        Text("Choose the correct sentence", style = MaterialTheme.typography.headlineMedium, color = Ink, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Text("How would you tell someone what you have for breakfast?", color = Color.DarkGray)
                        Spacer(Modifier.height(24.dp))
                        listOf("I eating rice for breakfast.", "I eat rice for breakfast.", "I eats rice for breakfast.").forEach { option ->
                            OutlinedButton(
                                onClick = { selected = option; checked = false },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selected == option) Color(0xFFEAE7FF) else Color.White)
                            ) { Text(option, modifier = Modifier.fillMaxWidth().padding(vertical = 9.dp), color = Ink) }
                        }
                        if (checked) {
                            Spacer(Modifier.height(18.dp))
                            Text(if (correct) "Great work! That's the natural sentence." else "Try again. Use 'eat' with 'I' for a daily habit.", color = if (correct) Color(0xFF147D52) else Color(0xFFAD5B18), fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(Modifier.weight(1f))
                        Button(onClick = { if (checked && correct) page = "complete" else checked = true }, enabled = selected != null, modifier = Modifier.fillMaxWidth()) {
                            Text(if (checked && correct) "Finish lesson →" else "Check answer")
                        }
                    }
                    else -> {
                        Spacer(Modifier.height(50.dp))
                        Text("🎉", style = MaterialTheme.typography.displayLarge, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        Spacer(Modifier.height(20.dp))
                        Text("Lesson complete!", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Ink)
                        Spacer(Modifier.height(12.dp))
                        Text("You practiced a sentence for describing your daily routine. Keep going!", color = Ink)
                        Spacer(Modifier.height(30.dp))
                        Button(onClick = { page = "home" }, modifier = Modifier.fillMaxWidth()) { Text("Back to home") }
                    }
                }
            }
            HorizontalDivider(color = Color(0xFFE7E9F2))
            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { page = "home" }) { Text("⌂ Home") }
                TextButton(onClick = { page = "lesson"; selected = null; checked = false }) { Text("▣ Learn") }
                TextButton(onClick = { page = "home" }) { Text("◉ Practice soon") }
            }
        }
    }
}
