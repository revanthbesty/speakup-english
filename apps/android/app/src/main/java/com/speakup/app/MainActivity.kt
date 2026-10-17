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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Navy = Color(0xFF111C35)
private val Violet = Color(0xFF6C5CE7)
private val Cream = Color(0xFFF7F7FC)
private val Soft = Color(0xFFECE9FF)
private val Night = Color(0xFF0D1426)
private val NightCard = Color(0xFF1B2741)
private val NightAccent = Color(0xFFB7ACFF)

private data class LearningPath(val id: String, val title: String, val subtitle: String, val nextStep: String)
private val learningPaths = listOf(
    LearningPath("zero", "Start from zero", "Letters, everyday words and first sentences", "Begin with friendly introductions and simple phrases."),
    LearningPath("basic", "I know a little English", "Build vocabulary, grammar and daily conversation", "Practise the four beginner conversation lessons."),
    LearningPath("speaking", "I understand but cannot speak", "Gain confidence through guided speaking", "Open Speaking Studio and practise one short response."),
    LearningPath("advanced", "I want advanced English", "Fluency, presentations and precise vocabulary", "Start with speaking practice while advanced courses are developed."),
    LearningPath("exams", "I am preparing for exams", "IELTS and other English exam pathways", "Practise reading and speaking while exam modules are developed.")
)

private data class PlacementQuestion(val prompt: String, val choices: List<String>, val correct: String)
private val placementQuestions = listOf(
    PlacementQuestion("Choose the correct greeting.", listOf("Hello, how are you?", "Hello, how you?", "Hello, you how?"), "Hello, how are you?"),
    PlacementQuestion("Choose the correct past-tense sentence.", listOf("Yesterday I go to school.", "Yesterday I went to school.", "Yesterday I going school."), "Yesterday I went to school."),
    PlacementQuestion("Choose the most natural formal request.", listOf("Give me your report now.", "I need report.", "Could you please send me the report by Friday?"), "Could you please send me the report by Friday?")
)

private data class Lesson(val title: String, val question: String, val answer: String, val choices: List<String>, val tip: String)
private val lessons = listOf(
    Lesson("Café conversations", "Which sentence would you say when ordering coffee?", "I'd like a cup of coffee, please.", listOf("I want cup coffee.", "I'd like a cup of coffee, please.", "Me like coffee cup."), "Use I'd like to make a polite request."),
    Lesson("Friendly introductions", "How do you introduce yourself politely?", "Hello, my name is Alex. Nice to meet you.", listOf("Me Alex. You?", "Hello, my name is Alex. Nice to meet you.", "I name Alex hello."), "Use My name is to introduce yourself and Nice to meet you to greet someone."),
    Lesson("Finding your way", "How do you politely ask for directions?", "Excuse me, could you tell me where the station is?", listOf("Where station go?", "Station where you?", "Excuse me, could you tell me where the station is?"), "Excuse me and could you make your request more polite."),
    Lesson("At the workplace", "How do you ask for help at work?", "Could you help me with this task, please?", listOf("Help task now.", "Could you help me with this task, please?", "You do this for me."), "Could you help me ... please? is a respectful request.")
)
private data class Riddle(val question: String, val options: List<String>, val answer: String, val explanation: String)
private val riddles = listOf(
    Riddle("I have keys but no locks. You use me to type. What am I?", listOf("A keyboard", "A door", "A clock"), "A keyboard", "A keyboard has keys for typing, not opening locks."),
    Riddle("What gets wetter the more it dries?", listOf("A towel", "The sun", "A book"), "A towel", "A towel absorbs water while drying things."),
    Riddle("I have hands but cannot clap. What am I?", listOf("A clock", "A child", "A tree"), "A clock", "A clock has hands that show the time."),
    Riddle("What has a neck but no head?", listOf("A bottle", "A bird", "A person"), "A bottle", "A bottle has a neck near its opening.")
)
private data class VocabularyCard(val word: String, val meaning: String, val example: String, val choices: List<String>)
private val vocabularyCards = listOf(
    VocabularyCard("Polite", "Showing respect and good manners", "It is polite to say please and thank you.", listOf("Showing respect and good manners", "Feeling very sleepy", "Moving quickly")),
    VocabularyCard("Confident", "Feeling sure about your abilities", "She feels confident speaking English.", listOf("Feeling afraid of everything", "Feeling sure about your abilities", "Being late for work")),
    VocabularyCard("Borrow", "To take something temporarily and return it", "May I borrow your pen?", listOf("To buy something new", "To throw something away", "To take something temporarily and return it")),
    VocabularyCard("Improve", "To make something better", "I practise daily to improve my English.", listOf("To make something better", "To stop trying", "To forget a skill")),
    VocabularyCard("Journey", "Travel from one place to another", "Our journey to the city was exciting.", listOf("A short message", "Travel from one place to another", "A kind of food")),
    VocabularyCard("Grateful", "Feeling thankful", "I am grateful for your help.", listOf("Feeling angry", "Feeling thankful", "Feeling confused")),
    VocabularyCard("Prepare", "To get ready for something", "I prepare for my interview every evening.", listOf("To get ready for something", "To give up", "To arrive late")),
    VocabularyCard("Curious", "Wanting to learn or know more", "The curious student asked a question.", listOf("Not interested", "Very tired", "Wanting to learn or know more"))
)
private data class GrammarQuestion(val prompt: String, val options: List<String>, val correct: String, val explanation: String)
private val grammarQuestions = listOf(
    GrammarQuestion("Choose the correct present simple sentence.", listOf("She go to school.", "She goes to school.", "She going to school."), "She goes to school.", "Use goes with he, she, or it in the present simple."),
    GrammarQuestion("Which sentence describes yesterday?", listOf("I visited my friend yesterday.", "I visit my friend yesterday.", "I visiting my friend yesterday."), "I visited my friend yesterday.", "Use the past tense visited for a completed action."),
    GrammarQuestion("Choose the correct question.", listOf("Where you are going?", "Where are you going?", "Where going you?"), "Where are you going?", "In present continuous questions, put are before the subject."),
    GrammarQuestion("Which sentence uses an article correctly?", listOf("I saw a elephant.", "I saw an elephant.", "I saw elephant."), "I saw an elephant.", "Use an before a vowel sound, as in elephant."),
    GrammarQuestion("Choose the polite request.", listOf("Can you help me, please?", "You helping me?", "Help me you."), "Can you help me, please?", "Can you ... please? is a natural, polite request."),
    GrammarQuestion("Which sentence uses the comparative correctly?", listOf("This book is more easy.", "This book is easiest than that.", "This book is easier than that."), "This book is easier than that.", "Use easier than when comparing two things.")
)
private data class ConversationScenario(val situation: String, val prompt: String, val options: List<String>, val correct: String, val explanation: String)
private val conversationScenarios = listOf(
    ConversationScenario("At a shop", "You want to know the price of a shirt. What do you say?", listOf("How much is this shirt?", "Give price shirt.", "This shirt how money?"), "How much is this shirt?", "How much is ...? is the natural way to ask about price."),
    ConversationScenario("Job interview", "The interviewer says: Tell me about yourself.", listOf("I don't say.", "I'm a motivated learner who enjoys solving problems.", "You tell first."), "I'm a motivated learner who enjoys solving problems.", "Give a brief, positive introduction relevant to the interview."),
    ConversationScenario("At the airport", "You cannot find your gate. What do you ask?", listOf("Where gate me?", "Excuse me, could you tell me where Gate 12 is?", "Gate now!"), "Excuse me, could you tell me where Gate 12 is?", "Excuse me and could you make your question polite."),
    ConversationScenario("At a restaurant", "Your order has not arrived. What is a polite response?", listOf("Where is my food? Hurry!", "Excuse me, could you check on my order, please?", "Food no here."), "Excuse me, could you check on my order, please?", "A polite request is more effective than an angry demand."),
    ConversationScenario("Meeting a new colleague", "A coworker says: Nice to meet you.", listOf("Nice to meet you too.", "No meet.", "I am not."), "Nice to meet you too.", "Reply with Nice to meet you too to return the greeting."),
    ConversationScenario("Asking for help", "You do not understand an instruction. What do you say?", listOf("I no understand you.", "Could you explain that again, please?", "Don't talk."), "Could you explain that again, please?", "Asking for clarification politely is a useful speaking skill.")
)
private data class ListeningCheck(val phrase: String, val question: String, val options: List<String>, val correct: String, val explanation: String)
private val listeningChecks = listOf(
    ListeningCheck("Could I have a glass of water, please?", "What is the speaker asking for?", listOf("A glass of water", "A bus ticket", "A notebook"), "A glass of water", "Could I have ... please? is a polite way to ask for something."),
    ListeningCheck("The train leaves at half past six.", "When does the train leave?", listOf("6:00", "6:30", "7:30"), "6:30", "Half past six means thirty minutes after six."),
    ListeningCheck("I have been studying English for two years.", "How long has the person studied English?", listOf("Two days", "Two months", "Two years"), "Two years", "For two years tells us the duration."),
    ListeningCheck("Please turn left at the next street.", "Which direction should you turn?", listOf("Right", "Left", "Straight ahead"), "Left", "Turn left means move to your left side."),
    ListeningCheck("Would you mind speaking a little more slowly?", "What does the speaker want?", listOf("Faster speech", "Slower speech", "No speech"), "Slower speech", "Would you mind ...? is a polite request."),
    ListeningCheck("The meeting has been moved to Friday morning.", "When is the meeting now?", listOf("Thursday evening", "Friday morning", "Saturday afternoon"), "Friday morning", "Has been moved to indicates a schedule change.")
)
private data class ReadingPassage(val title: String, val level: String, val passage: String, val question: String, val options: List<String>, val answer: String, val explanation: String)
private val readingPassages = listOf(
    ReadingPassage("Morning routine", "Beginner", "Maya wakes up at seven. She drinks water, eats breakfast, and walks to school. She likes to arrive early so she can read before class.", "Why does Maya arrive early?", listOf("To read before class", "To play football", "To buy breakfast"), "To read before class", "The final sentence tells us why Maya arrives early."),
    ReadingPassage("At the library", "Beginner", "On Saturday, Ben visits the library with his sister. He borrows a science book and promises to return it next week. His sister chooses a storybook.", "Which book does Ben borrow?", listOf("A storybook", "A science book", "A cookbook"), "A science book", "The passage says Ben borrows a science book."),
    ReadingPassage("A change of plans", "Intermediate", "The picnic was planned for Sunday morning, but the forecast predicted heavy rain. The family decided to visit the museum instead and save the picnic for another weekend.", "Why did the family change its plan?", listOf("The museum was closed", "They forgot the food", "Heavy rain was predicted"), "Heavy rain was predicted", "The weather forecast caused the change."),
    ReadingPassage("A helpful colleague", "Intermediate", "Priya noticed that her colleague was struggling with a presentation. Although her own work was finished, she stayed late to review his slides. The next morning, he thanked her for the thoughtful suggestions.", "What can we infer about Priya?", listOf("She is supportive", "She dislikes teamwork", "She missed the presentation"), "She is supportive", "Staying late to help someone suggests she is supportive."),
    ReadingPassage("Learning a new skill", "Intermediate", "At first, Omar found public speaking intimidating. He began practising short talks each evening, recorded his voice, and asked for feedback. After several weeks, he felt more comfortable speaking to a group.", "What helped Omar gain confidence?", listOf("Avoiding presentations", "Regular practice and feedback", "Memorising a single word"), "Regular practice and feedback", "Both daily practice and feedback contributed to his progress."),
    ReadingPassage("The community garden", "Advanced", "Residents transformed an unused lot into a community garden. Besides supplying fresh vegetables, the project brought neighbours together. Volunteers shared tools and taught children how to grow food, making the space valuable beyond its harvest.", "What is the central idea?", listOf("Vegetables are always expensive", "Unused lots should remain empty", "The garden benefits the community in several ways"), "The garden benefits the community in several ways", "The passage emphasises social and educational benefits as well as fresh food.")
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
    var dailyGoal by remember { mutableIntStateOf(preferences.getInt("daily_goal", 10)) }
    var learningGoal by remember { mutableStateOf(preferences.getString("learning_goal", "Everyday conversation") ?: "Everyday conversation") }
    var chosenDifficulty by remember { mutableStateOf(preferences.getString("learning_difficulty", "Easy") ?: "Easy") }
    var profileName by remember { mutableStateOf(preferences.getString("profile_name", "") ?: "") }
    var profileFeedback by remember { mutableStateOf("") }
    val learningGoals = listOf("Start speaking", "Everyday conversation", "School & exams", "Work & interviews", "Reading & writing", "Travel")
    var darkAppearance by remember { mutableStateOf(preferences.getBoolean("dark_appearance", false)) }
    var showPathSetup by remember { mutableStateOf(chosenPath.isBlank()) }
    var placementDone by remember { mutableStateOf(preferences.getBoolean("placement_done", false)) }
    var placementScore by remember { mutableIntStateOf(preferences.getInt("placement_score", -1)) }
    var placementIndex by remember { mutableIntStateOf(0) }
    var placementSelected by remember { mutableStateOf("") }
    var placementAnswers by remember { mutableStateOf(listOf<Boolean>()) }
    var showPlacement by remember { mutableStateOf(false) }
    var completedIds by remember { mutableStateOf((preferences.getStringSet("completed_lessons", emptySet()) ?: emptySet()).toSet() + if (preferences.getBoolean("cafe_lesson_complete", false)) setOf("0") else emptySet()) }
    var lessonIndex by remember { mutableIntStateOf(0) }
    val lesson = lessons[lessonIndex]
    var practiceResponse by remember { mutableStateOf("") }
    var practiceChecked by remember { mutableStateOf(false) }
    var confidenceAttempts by remember { mutableIntStateOf(preferences.getInt("confidence_attempts", 0)) }
    var independentReplies by remember { mutableIntStateOf(preferences.getInt("independent_replies", 0)) }
    var showSpeakingHint by remember { mutableStateOf(true) }
    var gameMode by remember { mutableStateOf("Word Builder") }
    var readingIndex by remember { mutableIntStateOf(0) }
    var readingChoice by remember { mutableStateOf("") }
    var readingChecked by remember { mutableStateOf(false) }
    var readingMastered by remember { mutableStateOf((preferences.getStringSet("reading_mastered", emptySet()) ?: emptySet()).toSet()) }
    var listeningIndex by remember { mutableIntStateOf(0) }
    var listeningChoice by remember { mutableStateOf("") }
    var listeningChecked by remember { mutableStateOf(false) }
    var listeningMastered by remember { mutableStateOf((preferences.getStringSet("listening_mastered", emptySet()) ?: emptySet()).toSet()) }
    var showListeningTranscript by remember { mutableStateOf(false) }
    var scenarioIndex by remember { mutableIntStateOf(0) }
    var scenarioChoice by remember { mutableStateOf("") }
    var scenarioChecked by remember { mutableStateOf(false) }
    var scenarioMastered by remember { mutableStateOf((preferences.getStringSet("scenario_mastered", emptySet()) ?: emptySet()).toSet()) }
    var grammarIndex by remember { mutableIntStateOf(0) }
    var grammarChoice by remember { mutableStateOf("") }
    var grammarChecked by remember { mutableStateOf(false) }
    var grammarMastered by remember { mutableStateOf((preferences.getStringSet("grammar_mastered", emptySet()) ?: emptySet()).toSet()) }
    var vocabularyIndex by remember { mutableIntStateOf(0) }
    var vocabularyChoice by remember { mutableStateOf("") }
    var vocabularyChecked by remember { mutableStateOf(false) }
    var vocabularyMastered by remember { mutableStateOf((preferences.getStringSet("vocabulary_mastered", emptySet()) ?: emptySet()).toSet()) }
    var riddleIndex by remember { mutableIntStateOf(0) }
    var riddleChoice by remember { mutableStateOf("") }
    var riddleChecked by remember { mutableStateOf(false) }
    var riddleAwards by remember { mutableStateOf((preferences.getStringSet("riddle_awards", emptySet()) ?: emptySet()).toSet()) }
    var puzzleWords by remember { mutableStateOf(listOf<String>()) }
    var puzzleChecked by remember { mutableStateOf(false) }
    var puzzlesSolved by remember { mutableIntStateOf(preferences.getInt("puzzles_solved", 0)) }
    var puzzleAwarded by remember { mutableStateOf((preferences.getStringSet("puzzle_awards", emptySet()) ?: emptySet()).toSet()) }
    var xp by remember { mutableIntStateOf(preferences.getInt("total_xp", 0)) }
    var speakingAttempts by remember { mutableIntStateOf(preferences.getInt("speaking_attempts", 0)) }
    var speechStatus by remember { mutableStateOf("") }
    var revisionSchedule by remember {
        mutableStateOf(lessons.indices.associate { index ->
            index.toString() to preferences.getLong("revision_due_$index", 0L)
        })
    }
    var revisionStages by remember {
        mutableStateOf(lessons.indices.associate { index ->
            index.toString() to preferences.getInt("revision_stage_$index", 0)
        })
    }
    var reviewEarly by remember { mutableStateOf(false) }
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
    val backgroundColor = if (darkAppearance) Night else Cream
    val primaryTextColor = if (darkAppearance) Color.White else Navy
    val secondaryTextColor = if (darkAppearance) Color(0xFFD2D9EA) else Color.DarkGray
    val accentTextColor = if (darkAppearance) NightAccent else Violet
    val surfaceColor = if (darkAppearance) NightCard else Color.White
    val softSurfaceColor = if (darkAppearance) NightCard else Soft
    MaterialTheme(colorScheme = if (darkAppearance) darkColorScheme(primary = NightAccent, background = Night, surface = NightCard, onSurface = Color.White) else lightColorScheme(primary = Violet, background = Cream)) {
        Column(Modifier.fillMaxSize().background(backgroundColor)) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("speakup.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = primaryTextColor)
                    Text("ENGLISH", color = accentTextColor, style = MaterialTheme.typography.labelMedium)
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {
                        darkAppearance = !darkAppearance
                        preferences.edit().putBoolean("dark_appearance", darkAppearance).apply()
                    }) { Text(if (darkAppearance) "Use light theme" else "Use dark theme") }
                }
                if (showPathSetup) {
                    Text("Your English, your way", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                    Text("Choose the path that fits you. You can change it any time.", color = secondaryTextColor)
                    Spacer(Modifier.height(16.dp))
                    Text("Your name (optional)", color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(value = profileName, onValueChange = { profileName = it.take(40) }, label = { Text("Name or nickname") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Text("Who is learning?", fontWeight = FontWeight.SemiBold, color = primaryTextColor)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Kids", "Teen", "Adult").forEach { age ->
                            FilterChip(selected = learnerAge == age, onClick = { learnerAge = age }, label = { Text(age) })
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("Why are you learning English?", color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                    learningGoals.forEach { goal ->
                        FilterChip(selected = learningGoal == goal, onClick = { learningGoal = goal }, label = { Text(goal) }, modifier = Modifier.padding(end = 4.dp))
                    }
                    Text("Your preferred challenge", color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Easy", "Medium", "Hard").forEach { difficulty ->
                            FilterChip(selected = chosenDifficulty == difficulty, onClick = { chosenDifficulty = difficulty }, label = { Text(difficulty) })
                        }
                    }
                    Text("Daily practice goal", color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 10, 15).forEach { minutes ->
                            FilterChip(selected = dailyGoal == minutes, onClick = { dailyGoal = minutes }, label = { Text("$minutes min") })
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    learningPaths.forEach { path ->
                        Surface(color = if (chosenPath == path.id) softSurfaceColor else surfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shadowElevation = 1.dp) {
                            Column(Modifier.padding(16.dp)) {
                                Text(path.title, color = primaryTextColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text(path.subtitle, color = secondaryTextColor)
                                TextButton(onClick = { chosenPath = path.id }) { Text(if (chosenPath == path.id) "✓ Selected" else "Choose this path") }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        preferences.edit().putString("learning_path", chosenPath).putString("learner_age", learnerAge).putInt("daily_goal", dailyGoal).putString("learning_goal", learningGoal).putString("learning_difficulty", chosenDifficulty).putString("profile_name", profileName).apply()
                        showPathSetup = false
                        showPlacement = !placementDone
                        page = "Today"
                    }, enabled = chosenPath.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Start my learning journey →") }
                } else if (showPlacement) {
                    Text("Find your starting point", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                    Text("Three quick questions help us suggest a starting point. This is not a certified language assessment.", color = secondaryTextColor)
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(progress = { placementIndex.toFloat() / placementQuestions.size }, modifier = Modifier.fillMaxWidth())
                    Text("QUESTION ${placementIndex + 1} OF ${placementQuestions.size}", color = accentTextColor, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    val placementQuestion = placementQuestions[placementIndex]
                    Text(placementQuestion.prompt, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                    placementQuestion.choices.forEach { choice ->
                        OutlinedButton(
                            onClick = { placementSelected = choice },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (placementSelected == choice) softSurfaceColor else surfaceColor)
                        ) { Text(choice, color = primaryTextColor, modifier = Modifier.fillMaxWidth()) }
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val updated = placementAnswers + (placementSelected == placementQuestion.correct)
                            placementAnswers = updated
                            placementSelected = ""
                            if (placementIndex == placementQuestions.lastIndex) {
                                placementScore = updated.count { it }
                                placementDone = true
                                showPlacement = false
                                preferences.edit().putBoolean("placement_done", true)
                                    .putInt("placement_score", placementScore).apply()
                            } else placementIndex += 1
                        },
                        enabled = placementSelected.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(if (placementIndex == placementQuestions.lastIndex) "See my starting point" else "Next question →") }
                    TextButton(onClick = {
                        showPlacement = false
                        placementDone = true
                        placementScore = -1
                        preferences.edit().putBoolean("placement_done", true).putInt("placement_score", -1).apply()
                    }) { Text("Skip assessment for now") }
                } else when (page) {
                    "Today" -> {
                        Text(if (profileName.isBlank()) "Welcome back!" else "Welcome, $profileName!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Your focus: $learningGoal • $chosenDifficulty", color = accentTextColor)
                        Text("My colorful progress", style = MaterialTheme.typography.headlineMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Text("Your milestones grow as you practise.", color = secondaryTextColor)
                        Spacer(Modifier.height(10.dp))
                        val masteredGames = puzzleAwarded.size + riddleAwards.size + vocabularyMastered.size + grammarMastered.size + scenarioMastered.size + listeningMastered.size + readingMastered.size
                        val availableGames = lessons.size + riddles.size + vocabularyCards.size + grammarQuestions.size + conversationScenarios.size + listeningChecks.size + readingPassages.size
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            ProgressTile("Lessons", "${completedIds.size} / ${lessons.size}", completedIds.size.toFloat() / lessons.size.coerceAtLeast(1), Color(0xFF7456EF), Color(0xFFAC83FA), Modifier.weight(1f))
                            ProgressTile("Games", "$masteredGames / $availableGames", masteredGames.toFloat() / availableGames.coerceAtLeast(1), Color(0xFFFF6A70), Color(0xFFFFB267), Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            ProgressTile("Reading", "${readingMastered.size} / ${readingPassages.size}", readingMastered.size.toFloat() / readingPassages.size.coerceAtLeast(1), Color(0xFF178D8D), Color(0xFF50CDB7), Modifier.weight(1f))
                            ProgressTile("Listening", "${listeningMastered.size} / ${listeningChecks.size}", listeningMastered.size.toFloat() / listeningChecks.size.coerceAtLeast(1), Color(0xFF2770D5), Color(0xFF66A9FA), Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(10.dp))
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text("⭐ $xp XP earned", color = primaryTextColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text("$speakingAttempts speaking attempts • ${completedIds.size} lessons finished", color = primaryTextColor)
                                val dueCount = lessons.indices.count { it.toString() in completedIds && (revisionSchedule[it.toString()] ?: 0L) <= System.currentTimeMillis() }
                                Text("$dueCount revision lessons ready today", color = primaryTextColor)
                                TextButton(onClick = { page = "Review" }) { Text("Explore detailed progress →") }
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                        Text("Your English journey", style = MaterialTheme.typography.headlineMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Text(when (learnerAge) {
                            "Kids" -> "Play, discover words, and speak with confidence!"
                            "Teen" -> "Build your skills through challenges and real conversations."
                            else -> "Practical English for your everyday goals."
                        }, color = secondaryTextColor)
                        Spacer(Modifier.height(8.dp))
                        val activePath = learningPaths.firstOrNull { it.id == chosenPath }
                        Text("${activePath?.title ?: "My English journey"} • $learnerAge", color = accentTextColor, fontWeight = FontWeight.Bold)
                        Text(activePath?.nextStep ?: "Choose a lesson to begin.", color = secondaryTextColor)
                        Text("Your journey is personal: practise at your pace, revisit lessons, and build confidence.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Text("Daily goal: $dailyGoal minutes • ${completedIds.size} / ${lessons.size} lessons completed", color = primaryTextColor)
                        LinearProgressIndicator(progress = { completedIds.size.toFloat() / lessons.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).semantics { contentDescription = "${completedIds.size} of ${lessons.size} lessons completed" })
                        if (placementDone && placementScore >= 0) {
                            val recommendation = when (placementScore) {
                                0 -> "Suggested start: first words and everyday phrases"
                                1 -> "Suggested start: basic everyday conversations"
                                2 -> "Suggested start: sentence building and speaking practice"
                                else -> "Suggested start: speaking challenges and more advanced practice"
                            }
                            Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("YOUR STARTING POINT", color = accentTextColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                    Text(recommendation, color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick = {
                                        page = if (placementScore >= 2) "Speak" else "Learn"
                                        lessonIndex = if (placementScore >= 2) 2 else 0
                                        checked = false
                                        selected = ""
                                    }) { Text(if (placementScore >= 2) "Start speaking practice →" else "Start recommended lesson →") }
                                }
                            }
                        }
                        TextButton(onClick = {
                            placementIndex = 0
                            placementSelected = ""
                            placementAnswers = emptyList()
                            showPlacement = true
                        }) { Text(if (placementDone) "Retake starting assessment" else "Take starting assessment") }
                        TextButton(onClick = { page = "Profile" }) { Text("Manage my learning profile →") }
                        Spacer(Modifier.height(8.dp))
                        Text("$xp XP earned  •  $speakingAttempts speaking attempts", color = accentTextColor, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(12.dp))
                        val levels = listOf(0, 100, 300, 600, 1000, 1800, 3000, 5000, 8000, 12000)
                        val level = levels.indexOfLast { xp >= it } + 1
                        val levelNames = listOf("First Steps", "Word Explorer", "Sentence Builder", "Rising Speaker", "Confident Communicator", "Conversation Champion", "Fluent Explorer", "English Achiever", "Language Master", "SpeakUp Legend")
                        val currentFloor = levels[level - 1]
                        val nextTarget = levels.getOrNull(level)
                        Surface(color = surfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text("LEVEL $level • ${levelNames[level - 1]}", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                if (nextTarget != null) {
                                    LinearProgressIndicator(progress = { (xp - currentFloor).toFloat() / (nextTarget - currentFloor) }, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))
                                    Text("${nextTarget - xp} XP to Level ${level + 1}", color = secondaryTextColor)
                                } else Text("Maximum achievement level reached!", color = accentTextColor)
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Surface(color = Navy, shape = RoundedCornerShape(26.dp)) {
                            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                                Text("TODAY'S SPEAKING MISSION", color = Color(0xFFBEB5FF), fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                val nextLesson = lessons.indices.firstOrNull { it.toString() !in completedIds } ?: 0
                                Text(if (completedIds.size == lessons.size) "Keep your English strong" else lessons[nextLesson].title, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                Text(if (completedIds.size == lessons.size) "Review your skills or try a speaking challenge" else "Your next lesson is ready • ${lessons.size - completedIds.size} lessons remaining", color = Color(0xFFE4E3F6))
                                Spacer(Modifier.height(20.dp))
                                Button(onClick = {
                                    lessonIndex = lessons.indices.firstOrNull { it.toString() !in completedIds } ?: 0
                                    page = if (completedIds.size == lessons.size) "Review" else "Learn"
                                    checked = false
                                    selected = ""
                                }, modifier = Modifier.fillMaxWidth()) { Text(if (completedIds.size == lessons.size) "Review my skills →" else "Continue learning →") }
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Text("Explore your academy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Spacer(Modifier.height(12.dp))
                        FeatureCard("✦  Speaking Studio", "Practise a café conversation", { page = "Speak" })
                        FeatureCard("▣  My Learning Path", "Build practical everyday English", { page = "Learn" })
                        FeatureCard("↻  Smart Revision", "Revisit what you have learned", { page = "Review" })
                        FeatureCard("🧩  Word Builder Games", "Arrange words into useful English sentences", { page = "Games" })
                    }
                    "Learn" -> {
                        Text("Everyday English", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        LinearProgressIndicator(progress = { completedIds.size.toFloat() / lessons.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                        Text("LESSON ${lessonIndex + 1} OF ${lessons.size} • ${lesson.title.uppercase()}", color = accentTextColor, fontWeight = FontWeight.Bold)
                        Text("Finish one lesson to unlock the next. Previously completed lessons stay unlocked.", color = secondaryTextColor)
                        lessons.forEachIndexed { index, item ->
                            val unlocked = index == 0 || (index - 1).toString() in completedIds || index.toString() in completedIds
                            TextButton(onClick = { lessonIndex = index; selected = ""; checked = false }, enabled = unlocked, modifier = Modifier.fillMaxWidth()) {
                                Text("${if (index.toString() in completedIds) "✓" else if (unlocked) "○" else "🔒"} Level ${index + 1}: ${item.title}")
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                        Text(lesson.question, color = primaryTextColor, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(16.dp))
                        lesson.choices.forEach { choice ->
                            OutlinedButton(onClick = { selected = choice; checked = false }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selected == choice) softSurfaceColor else surfaceColor)) {
                                Text(choice, color = primaryTextColor, modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp))
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
                        Text("Speaking Studio", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Spacer(Modifier.height(12.dp))
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(22.dp)) {
                            Column(Modifier.fillMaxWidth().padding(22.dp)) {
                                Text("${lesson.title.uppercase()} • SPEAKING PRACTICE", color = accentTextColor, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                Text(lesson.question, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                                Spacer(Modifier.height(12.dp))
                                if (showSpeakingHint) Text("Practice response: $answer", color = primaryTextColor)
                                TextButton(onClick = { showSpeakingHint = !showSpeakingHint }) {
                                    Text(if (showSpeakingHint) "Hide example • independent challenge" else "Show example")
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("Speak or type your answer for this lesson. Keyword feedback is a practice aid, not a pronunciation score.", color = secondaryTextColor)
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
                        if (speechStatus.isNotBlank()) Text(speechStatus, color = secondaryTextColor)
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
                            onClick = {
                                if (!practiceChecked) {
                                    confidenceAttempts += 1
                                    if (!showSpeakingHint) independentReplies += 1
                                    preferences.edit().putInt("confidence_attempts", confidenceAttempts)
                                        .putInt("independent_replies", independentReplies).apply()
                                }
                                practiceChecked = true
                            },
                            enabled = practiceResponse.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Check my reply") }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { page = "Learn" }, modifier = Modifier.fillMaxWidth()) { Text("Open this lesson") }
                    }
                    "Games" -> {
                        Text("Brain Games", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Recommended for $learningGoal • $chosenDifficulty", color = accentTextColor, fontWeight = FontWeight.Bold)
                        val suggestedModes = when (learningGoal) {
                            "Start speaking" -> listOf("Word Builder", "Situations", "Listening")
                            "School & exams" -> listOf("Grammar", "Reading", "Vocabulary")
                            "Work & interviews" -> listOf("Situations", "Vocabulary", "Listening")
                            "Reading & writing" -> listOf("Reading", "Grammar", "Word Builder")
                            "Travel" -> listOf("Situations", "Listening", "Vocabulary")
                            else -> listOf("Vocabulary", "Situations", "Word Builder")
                        }
                        Text("Suggested: ${suggestedModes.joinToString(" • ")}", color = secondaryTextColor)
                        Text("Browse all games below. Difficulty changes your learning preference; graded question filtering is being expanded.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Word Builder", "Riddles", "Vocabulary", "Grammar", "Situations", "Listening", "Reading").forEach { mode ->
                                FilterChip(selected = gameMode == mode, onClick = { gameMode = mode }, label = { Text(mode) })
                            }
                        }
                        if (gameMode == "Word Builder") {
                        Text("Word Builder", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Build a natural English sentence by tapping the words in order.", color = secondaryTextColor)
                        Spacer(Modifier.height(12.dp))
                        Text("PUZZLE ${lessonIndex + 1} OF ${lessons.size} • ${lesson.title.uppercase()}", color = accentTextColor, fontWeight = FontWeight.Bold)
                        Text(lesson.question, style = MaterialTheme.typography.titleMedium, color = primaryTextColor)
                        Spacer(Modifier.height(14.dp))
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(if (puzzleWords.isEmpty()) "Tap words below to build your sentence" else puzzleWords.joinToString(" "), modifier = Modifier.padding(16.dp), color = primaryTextColor)
                        }
                        Spacer(Modifier.height(12.dp))
                        val tokens = remember(lessonIndex) { lesson.answer.split(" ").shuffled() }
                        val remaining = tokens.toMutableList()
                        puzzleWords.forEach { remaining.remove(it) }
                        remaining.forEach { word ->
                            OutlinedButton(onClick = { puzzleWords = puzzleWords + word; puzzleChecked = false }, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                                Text(word, color = primaryTextColor)
                            }
                        }
                        if (puzzleChecked) {
                            val correct = puzzleWords.joinToString(" ") == lesson.answer
                            Text(if (correct) "Great work! ${lesson.tip}" else "Not yet. Rearrange the words and try again.", color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { puzzleWords = puzzleWords.dropLast(1); puzzleChecked = false }, enabled = puzzleWords.isNotEmpty()) { Text("Undo") }
                            OutlinedButton(onClick = { puzzleWords = emptyList(); puzzleChecked = false }) { Text("Reset") }
                            Button(onClick = {
                                puzzleChecked = true
                                if (puzzleWords.joinToString(" ") == lesson.answer && lessonIndex.toString() !in puzzleAwarded) {
                                    puzzleAwarded = puzzleAwarded + lessonIndex.toString()
                                    puzzlesSolved += 1
                                    xp += 10
                                    preferences.edit().putInt("puzzles_solved", puzzlesSolved).putInt("total_xp", xp).putStringSet("puzzle_awards", puzzleAwarded).apply()
                                }
                            }, enabled = puzzleWords.isNotEmpty()) { Text("Check puzzle") }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("Puzzles solved: $puzzlesSolved • +10 XP once per unique puzzle", color = accentTextColor)
                        OutlinedButton(onClick = {
                            lessonIndex = (lessonIndex + 1) % lessons.size
                            puzzleWords = emptyList()
                            puzzleChecked = false
                        }, modifier = Modifier.fillMaxWidth()) { Text("Next puzzle →") }
                        } else if (gameMode == "Riddles") {
                            val riddle = riddles[riddleIndex]
                            Text("Riddle ${riddleIndex + 1} of ${riddles.size}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Text(riddle.question, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                            riddle.options.forEach { option ->
                                OutlinedButton(onClick = { riddleChoice = option; riddleChecked = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text((if (riddleChoice == option) "✓ " else "") + option)
                                }
                            }
                            if (riddleChecked) {
                                Text(if (riddleChoice == riddle.answer) "Correct! ${riddle.explanation}" else "Try again. Think about the clue.", color = if (riddleChoice == riddle.answer) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                riddleChecked = true
                                val id = riddleIndex.toString()
                                if (riddleChoice == riddle.answer && id !in riddleAwards) {
                                    riddleAwards = riddleAwards + id
                                    xp += 10
                                    preferences.edit().putStringSet("riddle_awards", riddleAwards).putInt("total_xp", xp).apply()
                                }
                            }, enabled = riddleChoice.isNotBlank()) { Text("Check answer") }
                            Text("Riddles mastered: ${riddleAwards.size} / ${riddles.size} • +10 XP per new correct riddle", color = accentTextColor)
                            OutlinedButton(onClick = {
                                riddleIndex = (riddleIndex + 1) % riddles.size
                                riddleChoice = ""
                                riddleChecked = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Next riddle →") }
                        } else if (gameMode == "Vocabulary") {
                            val card = vocabularyCards[vocabularyIndex]
                            Text("Vocabulary ${vocabularyIndex + 1} of ${vocabularyCards.size}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Text(card.word, style = MaterialTheme.typography.headlineMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                            Text("What does this word mean?", color = secondaryTextColor)
                            Spacer(Modifier.height(8.dp))
                            card.choices.forEach { choice ->
                                OutlinedButton(onClick = { vocabularyChoice = choice; vocabularyChecked = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text((if (vocabularyChoice == choice) "✓ " else "") + choice)
                                }
                            }
                            if (vocabularyChecked) {
                                val correct = vocabularyChoice == card.meaning
                                Text(if (correct) "Correct! ${card.example}" else "Not quite. Try another meaning.", color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                vocabularyChecked = true
                                val id = vocabularyIndex.toString()
                                if (vocabularyChoice == card.meaning && id !in vocabularyMastered) {
                                    vocabularyMastered = vocabularyMastered + id
                                    xp += 10
                                    preferences.edit().putStringSet("vocabulary_mastered", vocabularyMastered).putInt("total_xp", xp).apply()
                                }
                            }, enabled = vocabularyChoice.isNotBlank()) { Text("Check meaning") }
                            Text("Words mastered: ${vocabularyMastered.size} / ${vocabularyCards.size} • +10 XP for each new word", color = accentTextColor)
                            OutlinedButton(onClick = {
                                vocabularyIndex = (vocabularyIndex + 1) % vocabularyCards.size
                                vocabularyChoice = ""
                                vocabularyChecked = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Next word →") }
                        } else if (gameMode == "Grammar") {
                            val question = grammarQuestions[grammarIndex]
                            Text("Grammar ${grammarIndex + 1} of ${grammarQuestions.size}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Text(question.prompt, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                            question.options.forEach { choice ->
                                OutlinedButton(onClick = { grammarChoice = choice; grammarChecked = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text((if (grammarChoice == choice) "✓ " else "") + choice)
                                }
                            }
                            if (grammarChecked) {
                                val correct = grammarChoice == question.correct
                                Text(if (correct) "Correct! ${question.explanation}" else "Try again. Hint: ${question.explanation}", color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                grammarChecked = true
                                val id = grammarIndex.toString()
                                if (grammarChoice == question.correct && id !in grammarMastered) {
                                    grammarMastered = grammarMastered + id
                                    xp += 10
                                    preferences.edit().putStringSet("grammar_mastered", grammarMastered).putInt("total_xp", xp).apply()
                                }
                            }, enabled = grammarChoice.isNotBlank()) { Text("Check grammar") }
                            Text("Grammar mastered: ${grammarMastered.size} / ${grammarQuestions.size} • +10 XP per new correct answer", color = accentTextColor)
                            OutlinedButton(onClick = {
                                grammarIndex = (grammarIndex + 1) % grammarQuestions.size
                                grammarChoice = ""
                                grammarChecked = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Next grammar question →") }
                        } else if (gameMode == "Situations") {
                            val scenario = conversationScenarios[scenarioIndex]
                            Text("Situation ${scenarioIndex + 1} of ${conversationScenarios.size}: ${scenario.situation}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Text(scenario.prompt, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                            scenario.options.forEach { choice ->
                                OutlinedButton(onClick = { scenarioChoice = choice; scenarioChecked = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text((if (scenarioChoice == choice) "✓ " else "") + choice)
                                }
                            }
                            if (scenarioChecked) {
                                val correct = scenarioChoice == scenario.correct
                                Text(if (correct) "Great response! ${scenario.explanation}" else "Try again. ${scenario.explanation}", color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                scenarioChecked = true
                                val id = scenarioIndex.toString()
                                if (scenarioChoice == scenario.correct && id !in scenarioMastered) {
                                    scenarioMastered = scenarioMastered + id
                                    xp += 10
                                    preferences.edit().putStringSet("scenario_mastered", scenarioMastered).putInt("total_xp", xp).apply()
                                }
                            }, enabled = scenarioChoice.isNotBlank()) { Text("Check response") }
                            Text("Situations mastered: ${scenarioMastered.size} / ${conversationScenarios.size} • +10 XP for each new success", color = accentTextColor)
                            OutlinedButton(onClick = {
                                scenarioIndex = (scenarioIndex + 1) % conversationScenarios.size
                                scenarioChoice = ""
                                scenarioChecked = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Next situation →") }
                        } else if (gameMode == "Listening") {
                            val listening = listeningChecks[listeningIndex]
                            Text("Listening comprehension ${listeningIndex + 1} of ${listeningChecks.size}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            ListeningAudio(listening.phrase)
                            OutlinedButton(onClick = { showListeningTranscript = !showListeningTranscript }) { Text(if (showListeningTranscript) "Hide sentence" else "Show sentence") }
                            if (showListeningTranscript) {
                                Surface(color = softSurfaceColor, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                                    Text(listening.phrase, modifier = Modifier.padding(16.dp), color = primaryTextColor)
                                }
                            }
                            Text(listening.question, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                            listening.options.forEach { choice ->
                                OutlinedButton(onClick = { listeningChoice = choice; listeningChecked = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text((if (listeningChoice == choice) "✓ " else "") + choice)
                                }
                            }
                            if (listeningChecked) {
                                val correct = listeningChoice == listening.correct
                                Text(if (correct) "Correct! ${listening.explanation}" else "Try again. ${listening.explanation}", color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                listeningChecked = true
                                val id = listeningIndex.toString()
                                if (listeningChoice == listening.correct && id !in listeningMastered) {
                                    listeningMastered = listeningMastered + id
                                    xp += 10
                                    preferences.edit().putStringSet("listening_mastered", listeningMastered).putInt("total_xp", xp).apply()
                                }
                            }, enabled = listeningChoice.isNotBlank()) { Text("Check comprehension") }
                            Text("Checks mastered: ${listeningMastered.size} / ${listeningChecks.size} • +10 XP for a first correct answer", color = accentTextColor)
                            OutlinedButton(onClick = {
                                listeningIndex = (listeningIndex + 1) % listeningChecks.size
                                listeningChoice = ""
                                listeningChecked = false
                                showListeningTranscript = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Next comprehension check →") }
                        } else {
                            val passage = readingPassages[readingIndex]
                            Text("Reading ${readingIndex + 1} of ${readingPassages.size} • ${passage.level}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Text(passage.title, style = MaterialTheme.typography.titleLarge, color = primaryTextColor, fontWeight = FontWeight.Bold)
                            Surface(color = softSurfaceColor, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                                Text(passage.passage, modifier = Modifier.padding(16.dp), color = primaryTextColor)
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(passage.question, style = MaterialTheme.typography.titleMedium, color = primaryTextColor)
                            passage.options.forEach { choice ->
                                OutlinedButton(onClick = { readingChoice = choice; readingChecked = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text((if (readingChoice == choice) "✓ " else "") + choice)
                                }
                            }
                            if (readingChecked) {
                                val correct = readingChoice == passage.answer
                                Text(if (correct) "Correct! ${passage.explanation}" else "Try again. Re-read the passage and find the clue.", color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                readingChecked = true
                                val id = readingIndex.toString()
                                if (readingChoice == passage.answer && id !in readingMastered) {
                                    readingMastered = readingMastered + id
                                    xp += 10
                                    preferences.edit().putStringSet("reading_mastered", readingMastered).putInt("total_xp", xp).apply()
                                }
                            }, enabled = readingChoice.isNotBlank()) { Text("Check reading answer") }
                            Text("Passages mastered: ${readingMastered.size} / ${readingPassages.size} • +10 XP once per passage", color = accentTextColor)
                            OutlinedButton(onClick = {
                                readingIndex = (readingIndex + 1) % readingPassages.size
                                readingChoice = ""
                                readingChecked = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Next reading passage →") }
                        }
                    }
                    "Profile" -> {
                        Text("Learner profile", style = MaterialTheme.typography.headlineMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Text("Update who is learning and why. Your XP, achievements and lesson history will be retained.", color = secondaryTextColor)
                        OutlinedTextField(value = profileName, onValueChange = { profileName = it.take(40) }, label = { Text("Name or nickname") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        Text("Age group", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Kids", "Teen", "Adult").forEach { age ->
                                FilterChip(selected = learnerAge == age, onClick = { learnerAge = age; profileFeedback = "" }, label = { Text(age) })
                            }
                        }
                        Text("Learning purpose", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        learningGoals.forEach { goal ->
                            FilterChip(selected = learningGoal == goal, onClick = { learningGoal = goal; profileFeedback = "" }, label = { Text(goal) })
                        }
                        Text("Challenge difficulty", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Easy", "Medium", "Hard").forEach { d ->
                                FilterChip(selected = chosenDifficulty == d, onClick = { chosenDifficulty = d; profileFeedback = "" }, label = { Text(d) })
                            }
                        }
                        Text("Learning pathway", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        learningPaths.forEach { path ->
                            FilterChip(selected = chosenPath == path.id, onClick = { chosenPath = path.id; profileFeedback = "" }, label = { Text(path.title) })
                        }
                        Text("Daily goal", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(5, 10, 15).forEach { minutes ->
                                FilterChip(selected = dailyGoal == minutes, onClick = { dailyGoal = minutes }, label = { Text("$minutes min") })
                            }
                        }
                        Button(onClick = {
                            preferences.edit().putString("profile_name", profileName).putString("learner_age", learnerAge)
                                .putString("learning_goal", learningGoal).putString("learning_difficulty", chosenDifficulty)
                                .putString("learning_path", chosenPath).putInt("daily_goal", dailyGoal).apply()
                            profileFeedback = "Saved! Your recommendations are updated."
                        }, modifier = Modifier.fillMaxWidth()) { Text("Save profile changes") }
                        if (profileFeedback.isNotBlank()) Text(profileFeedback, color = Color(0xFF178D8D))
                        OutlinedButton(onClick = { placementIndex = 0; placementSelected = ""; placementAnswers = emptyList(); showPlacement = true }, modifier = Modifier.fillMaxWidth()) { Text("Retake placement assessment") }
                    }
                    "Review" -> {
                        Text("My Growth", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Spacer(Modifier.height(16.dp))
                        FeatureCard("✓  ${completedIds.size} of ${lessons.size} lessons completed", "Each new lesson earns 50 XP.", { page = "Learn" })
                        LinearProgressIndicator(progress = { completedIds.size.toFloat() / lessons.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Total XP: $xp  •  Speaking attempts: $speakingAttempts", color = accentTextColor, fontWeight = FontWeight.Bold)
                        Text("Reading passages mastered: ${readingMastered.size} of ${readingPassages.size}", color = secondaryTextColor)
                        Spacer(Modifier.height(12.dp))
                        Text("Speaking confidence practice", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("$independentReplies of $confidenceAttempts checked replies attempted without the example", color = secondaryTextColor)
                        if (confidenceAttempts > 0) {
                            LinearProgressIndicator(progress = { independentReplies.toFloat() / confidenceAttempts }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                        }
                        Text("This tracks independent practice, not pronunciation quality or a certified fluency score.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(12.dp))
                        Text("Quick revision", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Correct revision answers: $revisionCorrect", color = accentTextColor)
                        Spacer(Modifier.height(16.dp))
                        Text("Achievement badges", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        val badges = listOf(
                            Triple("First Steps", completedIds.isNotEmpty(), "Complete your first lesson"),
                            Triple("Lesson Explorer", completedIds.size >= 4, "Complete four lessons"),
                            Triple("Brave Speaker", speakingAttempts >= 1, "Try microphone speaking"),
                            Triple("Memory Starter", revisionCorrect >= 3, "Answer three revision questions correctly"),
                            Triple("XP Collector", xp >= 300, "Earn 300 XP")
                        )
                        badges.forEach { (name, earned, description) ->
                            Surface(color = if (earned) softSurfaceColor else surfaceColor, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("${if (earned) "🏅" else "🔒"} $name", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                    Text(description, color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        val reviewable = lessons.indices.filter { it.toString() in completedIds }
                        val now = System.currentTimeMillis()
                        val dueLessons = reviewable.filter { (revisionSchedule[it.toString()] ?: 0L) <= now }
                        Text("Due for review: ${dueLessons.size} of ${reviewable.size} completed lessons", color = accentTextColor, fontWeight = FontWeight.Bold)
                        if (reviewable.isEmpty()) {
                            Text("Finish a lesson to unlock revision questions.", color = secondaryTextColor)
                        } else {
                            if (dueLessons.isEmpty()) {
                                Text("All caught up! Your next review will appear when it is due.", color = secondaryTextColor)
                                TextButton(onClick = { reviewEarly = !reviewEarly; revisionIndex = 0; revisionAnswer = ""; revisionChecked = false }) {
                                    Text(if (reviewEarly) "Return to scheduled reviews" else "Practise early anyway")
                                }
                            } else if (reviewEarly) {
                                TextButton(onClick = { reviewEarly = false; revisionIndex = 0; revisionAnswer = ""; revisionChecked = false }) {
                                    Text("Show only due reviews")
                                }
                            }
                            val queue = if (reviewEarly) reviewable else dueLessons
                            if (queue.isNotEmpty()) {
                                val current = queue[revisionIndex % queue.size]
                                val reviewLesson = lessons[current]
                                val stage = revisionStages[current.toString()] ?: 0
                                Text("Lesson: ${reviewLesson.title} • Review stage ${stage.coerceIn(0, 3)} / 3", color = secondaryTextColor)
                                Text(reviewLesson.question, color = primaryTextColor, style = MaterialTheme.typography.titleMedium)
                                reviewLesson.choices.forEach { choice ->
                                    OutlinedButton(
                                        onClick = { if (!revisionChecked) revisionAnswer = choice },
                                        enabled = !revisionChecked,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (revisionAnswer == choice) softSurfaceColor else surfaceColor)
                                    ) { Text(choice, color = primaryTextColor) }
                                }
                                if (revisionChecked) {
                                    Text(
                                        if (revisionAnswer == reviewLesson.answer) "Correct! ${reviewLesson.tip}" else "Not quite. ${reviewLesson.tip}",
                                        color = if (revisionAnswer == reviewLesson.answer) Color(0xFF177B57) else Color(0xFF9D5417)
                                    )
                                    Text(
                                        if (revisionAnswer == reviewLesson.answer) "This lesson will return in ${listOf(1, 3, 7)[stage.coerceIn(0, 2)]} day(s)." else "This lesson stays due for another try.",
                                        color = secondaryTextColor
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
                                            val correct = revisionAnswer == reviewLesson.answer
                                            val nextStage = if (correct) (stage + 1).coerceAtMost(3) else 0
                                            val interval = listOf(1, 3, 7)[stage.coerceIn(0, 2)]
                                            val nextDue = if (correct) System.currentTimeMillis() + interval * 86_400_000L else 0L
                                            revisionStages = revisionStages + (current.toString() to nextStage)
                                            revisionSchedule = revisionSchedule + (current.toString() to nextDue)
                                            preferences.edit().putInt("revision_stage_$current", nextStage).putLong("revision_due_$current", nextDue).apply()
                                            revisionIndex = if (correct) 0 else revisionIndex + 1
                                            revisionAnswer = ""
                                            revisionChecked = false
                                        }
                                    },
                                    enabled = revisionChecked || revisionAnswer.isNotBlank(),
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text(if (revisionChecked) "Save review and continue" else "Check revision answer") }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Your progress and 1/3/7-day review schedule are saved on this device. Account sync is planned.", color = secondaryTextColor)
                    }
                }
            }
            if (!showPathSetup && !showPlacement) {
            NavigationBar(containerColor = surfaceColor, tonalElevation = 4.dp) {
                listOf("Today", "Learn", "Games", "Review", "Profile").forEach { tab ->
                    val symbol = when (tab) {
                        "Today" -> "⌂"
                        "Learn" -> "▣"
                        "Games" -> "✦"
                        "Review" -> "▤"
                        else -> "◉"
                    }
                    NavigationBarItem(
                        selected = page == tab,
                        onClick = { page = tab },
                        icon = { Text(symbol, modifier = Modifier.semantics { contentDescription = "$tab tab" }, style = MaterialTheme.typography.titleMedium) },
                        label = { Text(when (tab) { "Today" -> "Home"; "Review" -> "Progress"; else -> tab }, maxLines = 1) },
                        alwaysShowLabel = true
                    )
                }
            }
            }
        }
    }
}


@Composable
private fun ProgressTile(title: String, amount: String, progress: Float, first: Color, second: Color, modifier: Modifier = Modifier) {
    val animated by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), animationSpec = tween(durationMillis = 650), label = "progress-$title")
    Column(
        modifier = modifier.background(Brush.linearGradient(listOf(first, second)), RoundedCornerShape(20.dp)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(amount, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        LinearProgressIndicator(
            progress = { animated },
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.28f)
        )
    }
}

@Composable
private fun FeatureCard(title: String, subtitle: String, onClick: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shadowElevation = 2.dp) {
        Column(Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onClick) { Text("Explore →") }
        }
    }
}
