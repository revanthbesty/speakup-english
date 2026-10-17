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

private data class LearningPath(val id: String, val title: String, val subtitle: String, val nextStep: String)
private val learningPaths = listOf(
    LearningPath("zero", "Start from zero", "Letters, everyday words and first sentences", "Begin with friendly introductions and simple phrases."),
    LearningPath("basic", "I know a little English", "Build vocabulary, grammar and daily conversation", "Practise the four beginner conversation lessons."),
    LearningPath("speaking", "I understand but cannot speak", "Gain confidence through guided speaking", "Open Speaking Studio and practise one short response."),
    LearningPath("advanced", "I want advanced English", "Fluency, presentations and precise vocabulary", "Start with speaking practice while advanced courses are developed."),
    LearningPath("exams", "I am preparing for exams", "IELTS and other English exam pathways", "Practise reading and speaking while exam modules are developed.")
)

private data class Lesson(val title: String, val question: String, val answer: String, val choices: List<String>, val tip: String)
private val lessons = listOf(
    Lesson("Café conversations", "Which sentence would you say when ordering coffee?", "I'd like a cup of coffee, please.", listOf("I want cup coffee.", "I'd like a cup of coffee, please.", "Me like coffee cup."), "Use I'd like to make a polite request."),
    Lesson("Friendly introductions", "How do you introduce yourself politely?", "Hello, my name is Alex. Nice to meet you.", listOf("Me Alex. You?", "Hello, my name is Alex. Nice to meet you.", "I name Alex hello."), "Use My name is to introduce yourself and Nice to meet you to greet someone."),
    Lesson("Finding your way", "How do you politely ask for directions?", "Excuse me, could you tell me where the station is?", listOf("Where station go?", "Station where you?", "Excuse me, could you tell me where the station is?"), "Excuse me and could you make your request more polite."),
    Lesson("At the workplace", "How do you ask for help at work?", "Could you help me with this task, please?", listOf("Help task now.", "Could you help me with this task, please?", "You do this for me."), "Could you help me ... please? is a respectful request.")
)
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
    var chosenPath by remember { mutableStateOf(preferences.getString("learning_path", "") ?: "") }
    var learnerAge by remember { mutableStateOf(preferences.getString("learner_age", "Adult") ?: "Adult") }
    var showPathSetup by remember { mutableStateOf(chosenPath.isBlank()) }
    var completedIds by remember { mutableStateOf((preferences.getStringSet("completed_lessons", emptySet()) ?: emptySet()).toSet() + if (preferences.getBoolean("cafe_lesson_complete", false)) setOf("0") else emptySet()) }
    var lessonIndex by remember { mutableIntStateOf(0) }
    val lesson = lessons[lessonIndex]
    var practiceResponse by remember { mutableStateOf("") }
    var practiceChecked by remember { mutableStateOf(false) }
    var xp by remember { mutableIntStateOf(preferences.getInt("total_xp", 0)) }
    var speakingAttempts by remember { mutableIntStateOf(preferences.getInt("speaking_attempts", 0)) }
    var speechStatus by remember { mutableStateOf("") }
    var revisionIndex by remember { mutableIntStateOf(0) }
    var revisionAnswer by remember { mutableStateOf("") }
    var revisionChecked by remember { mutableStateOf(false) }
    var revisionCorrect by remember { mutableIntStateOf(preferences.getInt("revision_correct", 0)) }
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
    val answer = lesson.answer
    MaterialTheme(colorScheme = lightColorScheme(primary = Violet, background = Cream)) {
        Column(Modifier.fillMaxSize().background(Cream)) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("speakup.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Text("ENGLISH", color = Violet, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.height(24.dp))
                if (showPathSetup) {
                    Text("Your English, your way", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
                    Text("Choose the path that fits you. You can change it any time.", color = Color.DarkGray)
                    Spacer(Modifier.height(16.dp))
                    Text("Who is learning?", fontWeight = FontWeight.SemiBold, color = Navy)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Kids", "Teen", "Adult").forEach { age ->
                            FilterChip(selected = learnerAge == age, onClick = { learnerAge = age }, label = { Text(age) })
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    learningPaths.forEach { path ->
                        Surface(color = if (chosenPath == path.id) Soft else Color.White, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shadowElevation = 1.dp) {
                            Column(Modifier.padding(16.dp)) {
                                Text(path.title, color = Navy, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text(path.subtitle, color = Color.DarkGray)
                                TextButton(onClick = { chosenPath = path.id }) { Text(if (chosenPath == path.id) "✓ Selected" else "Choose this path") }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        preferences.edit().putString("learning_path", chosenPath).putString("learner_age", learnerAge).apply()
                        showPathSetup = false
                        page = "Today"
                    }, enabled = chosenPath.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Start my learning journey →") }
                } else when (page) {
                    "Today" -> {
                        Text("Your English journey", style = MaterialTheme.typography.headlineMedium, color = Navy, fontWeight = FontWeight.Bold)
                        Text("Speak a little. Grow every day.", color = Color.DarkGray)
                        Spacer(Modifier.height(8.dp))
                        val activePath = learningPaths.firstOrNull { it.id == chosenPath }
                        Text("${activePath?.title ?: "My English journey"} • $learnerAge", color = Violet, fontWeight = FontWeight.Bold)
                        Text(activePath?.nextStep ?: "Choose a lesson to begin.", color = Color.DarkGray)
                        TextButton(onClick = { showPathSetup = true }) { Text("Change my learning path") }
                        Spacer(Modifier.height(8.dp))
                        Text("$xp XP earned  •  $speakingAttempts speaking attempts", color = Violet, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(12.dp))
                        val levels = listOf(0, 100, 300, 600, 1000, 1800, 3000, 5000, 8000, 12000)
                        val level = levels.indexOfLast { xp >= it } + 1
                        val levelNames = listOf("First Steps", "Word Explorer", "Sentence Builder", "Rising Speaker", "Confident Communicator", "Conversation Champion", "Fluent Explorer", "English Achiever", "Language Master", "SpeakUp Legend")
                        val currentFloor = levels[level - 1]
                        val nextTarget = levels.getOrNull(level)
                        Surface(color = Color.White, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text("LEVEL $level • ${levelNames[level - 1]}", color = Navy, fontWeight = FontWeight.Bold)
                                if (nextTarget != null) {
                                    LinearProgressIndicator(progress = { (xp - currentFloor).toFloat() / (nextTarget - currentFloor) }, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))
                                    Text("${nextTarget - xp} XP to Level ${level + 1}", color = Color.DarkGray)
                                } else Text("Maximum achievement level reached!", color = Violet)
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Surface(color = Navy, shape = RoundedCornerShape(26.dp)) {
                            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                                Text("TODAY'S SPEAKING MISSION", color = Color(0xFFBEB5FF), fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                Text("Order confidently at a café", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                Text("Four practical beginner conversation lessons", color = Color(0xFFE4E3F6))
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
                        LinearProgressIndicator(progress = { completedIds.size.toFloat() / lessons.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                        Text("LESSON ${lessonIndex + 1} OF ${lessons.size} • ${lesson.title.uppercase()}", color = Violet, fontWeight = FontWeight.Bold)
                        lessons.forEachIndexed { index, item ->
                            TextButton(onClick = { lessonIndex = index; selected = ""; checked = false }, modifier = Modifier.fillMaxWidth()) {
                                Text("${if (index.toString() in completedIds) "✓" else "○"} ${index + 1}. ${item.title}")
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                        Text(lesson.question, color = Navy, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(16.dp))
                        lesson.choices.forEach { choice ->
                            OutlinedButton(onClick = { selected = choice; checked = false }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selected == choice) Soft else Color.White)) {
                                Text(choice, color = Navy, modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp))
                            }
                        }
                        if (checked) {
                            Spacer(Modifier.height(12.dp))
                            Text(if (selected == answer) "Excellent! ${lesson.tip}" else "Try again. ${lesson.tip}", color = if (selected == answer) Color(0xFF177B57) else Color(0xFF9D5417))
                        }
                        Spacer(Modifier.height(20.dp))
                        Button(onClick = { if (checked && selected == answer) { if (lessonIndex.toString() !in completedIds) {
                                    completedIds = completedIds + lessonIndex.toString()
                                    xp += 50
                                    preferences.edit().putInt("total_xp", xp).putStringSet("completed_lessons", completedIds).apply()
                                }
                                page = "Review" } else checked = true }, enabled = selected.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
                            Text(if (checked && selected == answer) "Complete lesson" else "Check my answer")
                        }
                    }
                    "Speak" -> {
                        Text("Speaking Studio", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(12.dp))
                        Surface(color = Soft, shape = RoundedCornerShape(22.dp)) {
                            Column(Modifier.fillMaxWidth().padding(22.dp)) {
                                Text("${lesson.title.uppercase()} • SPEAKING PRACTICE", color = Violet, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                Text(lesson.question, style = MaterialTheme.typography.titleLarge, color = Navy)
                                Spacer(Modifier.height(12.dp))
                                Text("Practice response: $answer", color = Navy)
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("Speak or type your answer for this lesson. Keyword feedback is a practice aid, not a pronunciation score.", color = Color.DarkGray)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                speechStatus = ""
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toLanguageTag())
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your answer for ${lesson.title}")
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
                            label = { Text("Your spoken or typed response") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                        if (practiceChecked) {
                            Spacer(Modifier.height(10.dp))
                            val keywords = lesson.answer.lowercase().replace(Regex("[^a-z ]"), "").split(" ").filter { it.length > 3 }.toSet()
                            val spokenWords = practiceResponse.lowercase().replace(Regex("[^a-z ]"), "").split(" ").toSet()
                            val naturalReply = keywords.isNotEmpty() && spokenWords.intersect(keywords).size * 2 >= keywords.size
                            Text(
                                if (naturalReply) "Good practice! You used several key words from the example."
                                else "Try this example: ${lesson.answer}",
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
                        Button(onClick = { page = "Learn" }, modifier = Modifier.fillMaxWidth()) { Text("Open this lesson") }
                    }
                    "Review" -> {
                        Text("My Growth", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Navy)
                        Spacer(Modifier.height(16.dp))
                        FeatureCard("✓  ${completedIds.size} of ${lessons.size} lessons completed", "Each new lesson earns 50 XP.", { page = "Learn" })
                        LinearProgressIndicator(progress = { completedIds.size.toFloat() / lessons.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Total XP: $xp  •  Speaking attempts: $speakingAttempts", color = Violet, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Text("Quick revision", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Navy)
                        Text("Correct revision answers: $revisionCorrect", color = Violet)
                        Spacer(Modifier.height(16.dp))
                        Text("Achievement badges", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Navy)
                        val badges = listOf(
                            Triple("First Steps", completedIds.isNotEmpty(), "Complete your first lesson"),
                            Triple("Lesson Explorer", completedIds.size >= 4, "Complete four lessons"),
                            Triple("Brave Speaker", speakingAttempts >= 1, "Try microphone speaking"),
                            Triple("Memory Starter", revisionCorrect >= 3, "Answer three revision questions correctly"),
                            Triple("XP Collector", xp >= 300, "Earn 300 XP")
                        )
                        badges.forEach { (name, earned, description) ->
                            Surface(color = if (earned) Soft else Color.White, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("${if (earned) "🏅" else "🔒"} $name", color = Navy, fontWeight = FontWeight.Bold)
                                    Text(description, color = Color.DarkGray, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        val reviewable = lessons.indices.filter { it.toString() in completedIds }
                        if (reviewable.isEmpty()) {
                            Text("Finish a lesson to unlock revision questions.", color = Color.DarkGray)
                        } else {
                            val current = reviewable[revisionIndex % reviewable.size]
                            val reviewLesson = lessons[current]
                            Text(reviewLesson.question, color = Navy, style = MaterialTheme.typography.titleMedium)
                            reviewLesson.choices.forEach { choice ->
                                OutlinedButton(
                                    onClick = { revisionAnswer = choice; revisionChecked = false },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = if (revisionAnswer == choice) Soft else Color.White)
                                ) { Text(choice, color = Navy) }
                            }
                            if (revisionChecked) {
                                Text(
                                    if (revisionAnswer == reviewLesson.answer) "Correct! ${reviewLesson.tip}" else "Not quite. ${reviewLesson.tip}",
                                    color = if (revisionAnswer == reviewLesson.answer) Color(0xFF177B57) else Color(0xFF9D5417)
                                )
                            }
                            Button(
                                onClick = {
                                    if (!revisionChecked) {
                                        revisionChecked = true
                                        if (revisionAnswer == reviewLesson.answer) {
                                            revisionCorrect += 1
                                            preferences.edit().putInt("revision_correct", revisionCorrect).apply()
                                        }
                                    } else {
                                        revisionIndex += 1
                                        revisionAnswer = ""
                                        revisionChecked = false
                                    }
                                },
                                enabled = revisionChecked || revisionAnswer.isNotBlank(),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text(if (revisionChecked) "Next revision question" else "Check revision answer") }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Your progress is saved on this device. Account sync and revision scheduling are planned.", color = Color.DarkGray)
                    }
                }
            }
            if (!showPathSetup) {
            HorizontalDivider()
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Today", "Learn", "Speak", "Review").forEach { tab ->
                    TextButton(onClick = { page = tab }) { Text(tab, color = if (page == tab) Violet else Color.Gray) }
                }
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
