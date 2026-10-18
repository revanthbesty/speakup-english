package com.speakup.app

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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

private val Navy = Color(0xFF102A33)
private val Violet = Color(0xFF059669)
private val Cream = Color(0xFFF8FAFC)
private val Soft = Color(0xFFE2F7ED)
private val Night = Color(0xFF0F172A)
private val NightCard = Color(0xFF1E293B)
private val NightAccent = Color(0xFF6EE7B7)

private data class LearningPath(val id: String, val title: String, val subtitle: String, val nextStep: String)
private val learningPaths = listOf(
    LearningPath("zero", "English Explorer", "Letters, everyday words and first sentences", "Begin with friendly introductions and simple phrases."),
    LearningPath("basic", "Confidence Builder", "Build vocabulary, grammar and daily conversation", "Practise the four beginner conversation lessons."),
    LearningPath("speaking", "Speaking Accelerator", "Gain confidence through guided speaking", "Open Speaking Studio and practise one short response."),
    LearningPath("advanced", "English Pro", "Fluency, presentations and precise vocabulary", "Start with speaking practice while advanced courses are developed."),
    LearningPath("exams", "Exam Champion", "IELTS and other English exam pathways", "Practise reading and speaking while exam modules are developed.")
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
private data class GuidedDialogue(val id: String, val level: String, val topic: String, val other: String, val prompt: String, val options: List<String>, val correct: String, val tip: String)
private val guidedDialogues = listOf(
    GuidedDialogue("dlg-e-01", "Easy", "Greetings", "Hello! What's your name?", "Introduce yourself politely.", listOf("My name is Sam. Nice to meet you!", "You name is?", "Goodbye now."), "My name is Sam. Nice to meet you!", "Use My name is to introduce yourself."),
    GuidedDialogue("dlg-e-02", "Easy", "School", "Can I borrow your pencil?", "Agree to lend a pencil.", listOf("Yes, here you are.", "No pencil want.", "I am pencil."), "Yes, here you are.", "Here you are is natural when handing something to someone."),
    GuidedDialogue("dlg-e-03", "Easy", "Shopping", "Can I help you?", "Ask to see a blue shirt.", listOf("Yes, do you have this in blue?", "I no blue.", "Shirt yesterday."), "Yes, do you have this in blue?", "Do you have this in ...? helps you ask about an item."),
    GuidedDialogue("dlg-e-04", "Easy", "Food", "Would you like some juice?", "Accept a drink politely.", listOf("Yes, please. Thank you!", "Juice is window.", "I drinking yesterday."), "Yes, please. Thank you!", "Yes, please is a polite way to accept an offer."),
    GuidedDialogue("dlg-m-01", "Medium", "Directions", "The station is a few blocks away. Do you need directions?", "Ask for a clearer route.", listOf("Thanks! Should I turn left at the traffic lights?", "Station gone there me?", "I travel every day."), "Thanks! Should I turn left at the traffic lights?", "Ask a specific follow-up question to clarify directions."),
    GuidedDialogue("dlg-m-02", "Medium", "Work", "Could you send the report before lunch?", "Confirm when you will send it.", listOf("Certainly. I'll email it by eleven.", "Report is very report.", "I sent tomorrow yesterday."), "Certainly. I'll email it by eleven.", "A clear commitment includes a specific time."),
    GuidedDialogue("dlg-m-03", "Medium", "Travel", "I'm sorry, the train to Bristol has been delayed.", "Ask for the updated departure time.", listOf("Do you know what time it is expected to leave?", "Where yesterday?", "Train is reading."), "Do you know what time it is expected to leave?", "An indirect question is polite when asking staff for information."),
    GuidedDialogue("dlg-m-04", "Medium", "Social", "What do you usually do at the weekend?", "Describe a routine.", listOf("I often meet friends or go cycling.", "Yesterday tomorrow bike.", "Weekend are many."), "I often meet friends or go cycling.", "Use present simple for habits, with often or usually."),
    GuidedDialogue("dlg-h-01", "Hard", "Interview", "Can you describe a challenge you overcame at work?", "Give a concise answer with a result.", listOf("We faced a deadline, so I reorganised tasks and we delivered on time.", "There were challenges, I think.", "You should ask someone else."), "We faced a deadline, so I reorganised tasks and we delivered on time.", "Explain the problem, your action, and a measurable result."),
    GuidedDialogue("dlg-h-02", "Hard", "Debate", "Some colleagues believe remote work reduces collaboration. What do you think?", "Offer a balanced opinion.", listOf("While remote work can limit spontaneous discussion, scheduled check-ins can support teamwork.", "They are wrong and that is all.", "Remote has words."), "While remote work can limit spontaneous discussion, scheduled check-ins can support teamwork.", "Use while to acknowledge another view before offering your position."),
    GuidedDialogue("dlg-h-03", "Hard", "Customer service", "My order arrived damaged, and I need it for an event tomorrow.", "Acknowledge and propose a solution.", listOf("I'm sorry it arrived damaged. Let me check whether express replacement is possible.", "That happens. Goodbye.", "The event was yesterday."), "I'm sorry it arrived damaged. Let me check whether express replacement is possible.", "A useful response acknowledges the issue and offers a practical next step."),
    GuidedDialogue("dlg-h-04", "Hard", "Exam speaking", "What impact might public transport investment have on cities?", "Give an opinion and a supporting reason.", listOf("Investment could reduce congestion because reliable services may encourage fewer car journeys.", "Transport is good.", "I don't know any city."), "Investment could reduce congestion because reliable services may encourage fewer car journeys.", "Support your main claim with an explanation of cause and effect.")
)
private data class SkillPractice(val id: String, val difficulty: String, val skill: String, val goal: String, val prompt: String, val choices: List<String>, val answer: String, val tip: String, val ageGroup: String = "All")
private val skillPractices = listOf(
    SkillPractice("skill-e-01", "Easy", "Speaking", "Start speaking", "How do you greet someone in the morning?", listOf("Good morning!", "Good nighttime!", "Morning is mine."), "Good morning!", "Use Good morning before midday."),
    SkillPractice("skill-e-02", "Easy", "Listening", "Travel", "Someone says: The bus leaves at nine. When does the bus leave?", listOf("At nine", "At five", "Tomorrow evening"), "At nine", "Listen for the time expression nine."),
    SkillPractice("skill-e-03", "Easy", "Reading", "Reading & writing", "Read: Tom has a red bag. What colour is Tom's bag?", listOf("Blue", "Red", "Green"), "Red", "Find the descriptive word next to bag."),
    SkillPractice("skill-e-04", "Easy", "Grammar", "School & exams", "Choose the correct sentence.", listOf("He is happy.", "He are happy.", "He am happy."), "He is happy.", "He takes is in the present tense."),
    SkillPractice("skill-m-01", "Medium", "Speaking", "Work & interviews", "Your manager asks if a report will be ready today. What is a clear reply?", listOf("Yes, I'll send it by four o'clock.", "Yes report it maybe.", "It was tomorrow."), "Yes, I'll send it by four o'clock.", "A clear commitment includes a specific time."),
    SkillPractice("skill-m-02", "Medium", "Reading", "Reading & writing", "Read: The workshop was postponed because the presenter was ill. Why was it postponed?", listOf("The venue closed", "The presenter was ill", "Nobody registered"), "The presenter was ill", "Because introduces the reason."),
    SkillPractice("skill-m-03", "Medium", "Grammar", "School & exams", "Choose the correct conditional sentence.", listOf("If it rains, we will stay indoors.", "If it rain, we staying.", "If raining, we stays."), "If it rains, we will stay indoors.", "Use present simple after if for a likely future condition."),
    SkillPractice("skill-m-04", "Medium", "Listening", "Travel", "A guide says: Meet outside the museum at quarter past ten. At what time?", listOf("10:15", "10:45", "11:10"), "10:15", "Quarter past ten means 15 minutes after ten."),
    SkillPractice("skill-h-01", "Hard", "Speaking", "Work & interviews", "A client requests an unrealistic deadline. Which response balances empathy and boundaries?", listOf("I understand the urgency; we can deliver a draft by Friday and the final version Monday.", "That is impossible. Stop asking.", "We can do anything yesterday."), "I understand the urgency; we can deliver a draft by Friday and the final version Monday.", "Acknowledge the concern, explain options and agree on realistic milestones."),
    SkillPractice("skill-h-02", "Hard", "Reading", "Reading & writing", "Read: Despite initial reservations, the committee endorsed the proposal after reviewing the evidence. What changed?", listOf("The evidence persuaded the committee", "The committee cancelled the meeting", "The proposal was rejected unanimously"), "The evidence persuaded the committee", "Despite signals a contrast between initial concerns and the final decision."),
    SkillPractice("skill-h-03", "Hard", "Grammar", "School & exams", "Choose the correctly inverted conditional.", listOf("Had I known, I would have called.", "Had I knew, I will call.", "If had I known, I called."), "Had I known, I would have called.", "Had I known is a formal inversion of If I had known."),
    SkillPractice("skill-h-04", "Hard", "Listening", "Travel", "An announcement says: Boarding has been deferred until further notice. What does it mean?", listOf("Boarding is postponed with no new time given", "Boarding has finished", "Passengers must board immediately"), "Boarding is postponed with no new time given", "Deferred until further notice means delayed without a confirmed restart time."),
    SkillPractice("skill-k-01", "Easy", "Vocabulary", "Start speaking", "Which word means an animal that says meow?", listOf("Cat", "Cup", "Car"), "Cat", "A cat is a pet that may say meow.", "Kids"),
    SkillPractice("skill-k-02", "Medium", "Reading", "School & exams", "Read: Lina watered her small plant every day. Soon it grew new leaves. Why did the plant grow?", listOf("Lina cared for it", "It ate cake", "It went on holiday"), "Lina cared for it", "The passage links Lina's regular care with new leaves.", "Kids"),
    SkillPractice("skill-t-01", "Easy", "Speaking", "Everyday conversation", "A classmate says: Want to join our study group? What is a friendly reply?", listOf("Sure, I'd love to!", "Study no yes.", "Go away, group."), "Sure, I'd love to!", "I'd love to is a natural way to accept an invitation.", "Teen"),
    SkillPractice("skill-t-02", "Hard", "Reading", "School & exams", "Your group disagrees about a project idea. Which response encourages discussion?", listOf("Let's compare the evidence before choosing.", "My idea is always right.", "We must stop the project."), "Let's compare the evidence before choosing.", "Use respectful language to invite evidence-based discussion.", "Teen"),
    SkillPractice("skill-a-01", "Medium", "Speaking", "Work & interviews", "A coworker asks for a schedule update. Which reply is clearest?", listOf("The meeting will begin at 3 pm in Room 2.", "Meeting it is stuff.", "I am calendar."), "The meeting will begin at 3 pm in Room 2.", "A precise update includes the time and location.", "Adult"),
    SkillPractice("skill-a-02", "Hard", "Writing", "Work & interviews", "Choose the clearest professional email sentence.", listOf("Please find the updated proposal attached for your review.", "Proposal here look.", "I attach did yesterday tomorrow."), "Please find the updated proposal attached for your review.", "Professional writing is clear, courteous and grammatical.", "Adult")
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PremiumSpeakUp() }
    }
}

@Composable
fun PremiumSpeakUp() {
    var selected by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val preferences = remember(context) { context.getSharedPreferences(MintIdentity.legacyPreferencesFile, android.content.Context.MODE_PRIVATE) }
    var page by remember { mutableStateOf(MintUiNavigation.restoredTab(preferences.getString("mint_selected_tab", "Today"))) }
    LaunchedEffect(page) { if (page in MintUiNavigation.tabs) preferences.edit().putString("mint_selected_tab", page).apply() }
    var chosenPath by remember { mutableStateOf(preferences.getString("learning_path", "") ?: "") }
    var learnerAge by remember { mutableStateOf(preferences.getString("learner_age", "Adult") ?: "Adult") }
    var learnerAgeBand by remember { mutableStateOf(preferences.getString("mint_age_band", MintExperiencePolicy.initialAgeBand(learnerAge)) ?: MintExperiencePolicy.initialAgeBand(learnerAge)) }
    var dailyGoal by remember { mutableIntStateOf(preferences.getInt("daily_goal", 10)) }
    var learningGoal by remember { mutableStateOf(preferences.getString("learning_goal", "Everyday conversation") ?: "Everyday conversation") }
    var chosenDifficulty by remember { mutableStateOf(preferences.getString("learning_difficulty", "Easy") ?: "Easy") }
    var profileName by remember { mutableStateOf(preferences.getString("profile_name", "") ?: "") }
    var profileFeedback by remember { mutableStateOf("") }
    val learningGoals = listOf("Start speaking", "Everyday conversation", "School & exams", "Work & interviews", "Reading & writing", "Travel")
    val followSystemDark = isSystemInDarkTheme()
    var themePreference by remember { mutableStateOf(preferences.getString("mint_theme_mode", if (preferences.getBoolean("dark_appearance", false)) "dark" else "system") ?: "system") }
    var reduceMotion by remember { mutableStateOf(preferences.getBoolean("mint_reduce_motion", false)) }
    var eyeComfort by remember { mutableStateOf(preferences.getBoolean("mint_eye_comfort", false)) }
    val accessibilityScale = android.provider.Settings.Global.getFloat(context.contentResolver, android.provider.Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
    val motionReduced = MintExperiencePolicy.reducedMotion(reduceMotion, accessibilityScale == 0f)
    val localHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val darkAppearance = MintExperiencePolicy.nightModeEnabled(themePreference, followSystemDark, localHour)
    var showPathSetup by remember { mutableStateOf(chosenPath.isBlank()) }
    var placementDone by remember { mutableStateOf(preferences.getBoolean("placement_done", false)) }
    var placementScore by remember { mutableIntStateOf(preferences.getInt("placement_score", -1)) }
    var placementIndex by remember { mutableIntStateOf(0) }
    var placementSelected by remember { mutableStateOf("") }
    var placementAnswers by remember { mutableStateOf(listOf<Boolean>()) }
    var placementAttempt by remember { mutableIntStateOf(preferences.getInt("placement_attempt", 0)) }
    var placementLevel by remember { mutableStateOf(preferences.getString("placement_level", "") ?: "") }
    val suggestedPlacementLevel = MintPlacementBank.startingLevel(chosenPath, chosenDifficulty, learningGoal)
    val selectedPlacementLevel = placementLevel.ifBlank { suggestedPlacementLevel }
    val activePlacementQuestions = remember(selectedPlacementLevel, placementAttempt) {
        MintPlacementBank.session(selectedPlacementLevel, placementAttempt)
    }
    var showPlacement by remember { mutableStateOf(false) }
    var storyOpenedFromGames by remember { mutableStateOf(false) }
    var completedIds by remember { mutableStateOf((preferences.getStringSet("completed_lessons", emptySet()) ?: emptySet()).toSet() + if (preferences.getBoolean("cafe_lesson_complete", false)) setOf("0") else emptySet()) }
    var lessonIndex by remember { mutableIntStateOf(0) }
    val lesson = lessons[lessonIndex]
    var practiceResponse by remember { mutableStateOf("") }
    var practiceChecked by remember { mutableStateOf(false) }
    var confidenceAttempts by remember { mutableIntStateOf(preferences.getInt("confidence_attempts", 0)) }
    var independentReplies by remember { mutableIntStateOf(preferences.getInt("independent_replies", 0)) }
    var showSpeakingHint by remember { mutableStateOf(true) }
    var showProfileSettings by remember { mutableStateOf(false) }
    var showGameFilters by remember { mutableStateOf(false) }
    var showOnboardingOptions by remember { mutableStateOf(false) }
    var showJourneyPicker by remember { mutableStateOf(false) }
    var showProfileAdvanced by remember { mutableStateOf(false) }
    var showHomeHelp by remember { mutableStateOf(false) }
    var speakingView by remember { mutableStateOf("Conversations") }
    var guidedVoiceReceived by remember { mutableStateOf(false) }
    var showSpeechConsent by remember { mutableStateOf(false) }
    var pendingSpeechPrompt by remember { mutableStateOf("") }
    var completedGuidedVoiceTurns by remember {
        mutableStateOf((preferences.getStringSet("mint_guided_voice_turns", emptySet()) ?: emptySet()).toSet())
    }
    var learnMode by remember { mutableStateOf("Lessons") }
    var skillPracticeIndex by remember { mutableIntStateOf(0) }
    var skillPracticeAnswer by remember { mutableStateOf("") }
    var skillPracticeChecked by remember { mutableStateOf(false) }
    var skillMastered by remember { mutableStateOf((preferences.getStringSet("skill_practice_mastered", emptySet()) ?: emptySet()).toSet()) }
    var blockedMintClips by remember {
        mutableStateOf((preferences.getStringSet("mint_hidden_clips", emptySet()) ?: emptySet()).toSet())
    }
    var reportedMintClips by remember {
        mutableStateOf((preferences.getStringSet("mint_local_clip_reports", emptySet()) ?: emptySet()).toSet())
    }
    var reactedMintClips by remember {
        mutableStateOf((preferences.getStringSet("mint_private_reactions", emptySet()) ?: emptySet()).toSet())
    }
    var mintStoryCompletions by remember {
        mutableStateOf((preferences.getStringSet("mint_story_completed", emptySet()) ?: emptySet()).toSet())
    }
    var gameMode by remember { mutableStateOf("Mint Quests") }
    BackHandler(enabled = !showPathSetup && !showPlacement && (page != "Today" || (page == "Games" && gameMode != "Mint Quests"))) {
        when {
            page == "Games" && gameMode != "Mint Quests" -> { gameMode = "Mint Quests"; showGameFilters = false }
            page == "FiveWord" -> page = "Feed"
            page == "Feed" && storyOpenedFromGames -> { page = "Games"; storyOpenedFromGames = false }
            page == "Help" || page == "Privacy" || page == "Membership" -> page = "Profile"
            else -> page = "Today"
        }
    }

    var mintDifficulty by remember { mutableStateOf(when (chosenDifficulty) { "Hard" -> MintDifficulty.HARD; "Medium" -> MintDifficulty.MEDIUM; else -> MintDifficulty.EASY }) }
    var mintMechanic by remember { mutableStateOf(MintMechanic.CHOOSE_REPLY) }
    var mintGameId by remember { mutableStateOf("") }
    var mintChoice by remember { mutableStateOf("") }
    var mintSelectedTokens by remember { mutableStateOf(listOf<String>()) }
    var mintChecked by remember { mutableStateOf(false) }
    var mintShowHint by remember { mutableStateOf(false) }
    var mintRewardLedger by remember { mutableStateOf((preferences.getStringSet("mint_game_awards", emptySet()) ?: emptySet()).toSet()) }
    var mintGameFeedback by remember { mutableStateOf("") }
    var mintGameXpAward by remember { mutableIntStateOf(0) }
    var mintSpeakingMission by remember { mutableStateOf("") }

    var dialogueIndex by remember { mutableIntStateOf(0) }
    var dialogueChoice by remember { mutableStateOf("") }
    var dialogueChecked by remember { mutableStateOf(false) }
    var dialogueCompleted by remember { mutableStateOf((preferences.getStringSet("dialogue_mastered", emptySet()) ?: emptySet()).toSet()) }
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
                guidedVoiceReceived = true
                practiceChecked = false
                speakingAttempts += 1
                preferences.edit().putInt("speaking_attempts", speakingAttempts).apply()
                speechStatus = MintSpeechSafety.feedback(MintSpeechOutcome.RECOGNIZED)
            } else { guidedVoiceReceived = false; speechStatus = "No words recognized. Please retry or type your response; no voice attempt was recorded." }
        } else { guidedVoiceReceived = false; speechStatus = MintSpeechSafety.feedback(MintSpeechOutcome.CANCELLED) }
    }
    val launchSpeechRecognition: (String) -> Unit = { prompt ->
        speechStatus = ""
        guidedVoiceReceived = false
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
        }
        try { speechLauncher.launch(intent) }
        catch (_: android.content.ActivityNotFoundException) {
            speechStatus = MintSpeechSafety.feedback(MintSpeechOutcome.UNAVAILABLE)
        } catch (_: SecurityException) {
            speechStatus = "Microphone access was denied. No voice attempt was recorded; type your response instead."
        }
    }
    val answer = lesson.answer
    val backgroundColor = if (darkAppearance) Night else Cream
    val primaryTextColor = if (darkAppearance) Color.White else Navy
    val secondaryTextColor = if (darkAppearance) Color(0xFFD2D9EA) else Color.DarkGray
    val accentTextColor = if (darkAppearance) NightAccent else Violet
    val surfaceColor = if (darkAppearance) NightCard else Color.White
    val softSurfaceColor = if (darkAppearance) NightCard else Soft
    MaterialTheme(colorScheme = if (darkAppearance) darkColorScheme(primary = NightAccent, onPrimary = Color(0xFF062B24), background = Night, onBackground = Color(0xFFE8F3F3), surface = NightCard, onSurface = Color(0xFFE8F3F3), surfaceVariant = Color(0xFF253643), onSurfaceVariant = Color(0xFFB5CACF), surfaceContainer = NightCard, surfaceContainerLow = NightCard, primaryContainer = Color(0xFF20534A), onPrimaryContainer = Color.White, outline = Color(0xFF9DB9BE)) else lightColorScheme(primary = Violet, onPrimary = Color.White, background = Cream, onBackground = Navy, surface = Color.White, onSurface = Navy, surfaceVariant = Color(0xFFE5F0EF), onSurfaceVariant = Color(0xFF46606A), surfaceContainer = Color(0xFFF0F6F5), surfaceContainerLow = Color(0xFFF6FAFA), primaryContainer = Soft, onPrimaryContainer = Navy, outline = Color(0xFF607D82))) {
        if (showSpeechConsent) {
            AlertDialog(
                onDismissRequest = { showSpeechConsent = false },
                title = { Text("Optional speech recognition") },
                text = { Text("Your device's speech recognition provider may process spoken audio. English Mint does not save raw recordings or upload them to its own server in this mode. You can cancel or use quiet text practice.") },
                confirmButton = {
                    TextButton(onClick = {
                        showSpeechConsent = false
                        launchSpeechRecognition(pendingSpeechPrompt)
                    }) { Text("Continue to speech") }
                },
                dismissButton = { TextButton(onClick = { showSpeechConsent = false; speechStatus = MintSpeechSafety.feedback(MintSpeechOutcome.SKIPPED) }) { Text("Use text instead") } }
            )
        }
        Surface(color = backgroundColor, contentColor = primaryTextColor) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(MintIdentity.displayName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = primaryTextColor)
                        Text(MintIdentity.tagline, color = accentTextColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }

                }
                Spacer(Modifier.height(12.dp))
                if (showPathSetup) {
                    MintWelcomeFlow(
                        ageBand = learnerAgeBand,
                        goal = learningGoal,
                        name = profileName,
                        onAge = { band ->
                            learnerAgeBand = band
                            learnerAge = MintExperiencePolicy.legacyAge(band)
                        },
                        onGoal = { learningGoal = it },
                        onName = { profileName = it },
                        onFinish = { takePlacement ->
                            chosenPath = when (learningGoal) {
                                "Start speaking" -> "speaking"
                                "School & exams" -> "exams"
                                "Work & interviews" -> "advanced"
                                "Reading & writing" -> "basic"
                                else -> "zero"
                            }
                            preferences.edit()
                                .putString("learning_path", chosenPath)
                                .putString("learner_age", learnerAge)
                                .putString("mint_age_band", learnerAgeBand)
                                .putInt("daily_goal", dailyGoal)
                                .putString("learning_goal", learningGoal)
                                .putString("learning_difficulty", chosenDifficulty)
                                .putString("profile_name", profileName)
                                .putBoolean("mint_onboarding_complete", true)
                                .apply()
                            showPathSetup = false
                            showPlacement = takePlacement && !placementDone
                            page = "Today"
                        }
                    )
                } else if (showPlacement) {
                    Text("Find your starting point", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                    Text("Three short questions. You can skip this.", color = secondaryTextColor)
                    Text("Assessment level: $selectedPlacementLevel", color = accentTextColor, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Easy", "Medium", "Hard", "Exam").forEach { level ->
                            FilterChip(
                                selected = selectedPlacementLevel == level,
                                onClick = {
                                    placementLevel = level
                                    placementIndex = 0
                                    placementSelected = ""
                                    placementAnswers = emptyList()
                                },
                                label = { Text(level) }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(progress = { placementIndex.toFloat() / activePlacementQuestions.size }, modifier = Modifier.fillMaxWidth())
                    Text("QUESTION ${placementIndex + 1} OF ${activePlacementQuestions.size}", color = accentTextColor, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    val placementQuestion = activePlacementQuestions[placementIndex]
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
                            if (placementIndex == activePlacementQuestions.lastIndex) {
                                placementScore = updated.count { it }
                                placementLevel = selectedPlacementLevel
                                placementDone = true
                                showPlacement = false
                                preferences.edit().putBoolean("placement_done", true)
                                    .putInt("placement_score", placementScore)
                                    .putString("placement_level", placementLevel)
                                    .putInt("placement_attempt", placementAttempt + 1).apply()
                                placementAttempt += 1
                            } else placementIndex += 1
                        },
                        enabled = placementSelected.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(if (placementIndex == activePlacementQuestions.lastIndex) "See my starting point" else "Next question →") }
                    TextButton(onClick = {
                        showPlacement = false
                        placementDone = true
                        placementScore = -1
                        placementLevel = selectedPlacementLevel
                        preferences.edit().putBoolean("placement_done", true).putInt("placement_score", -1).putString("placement_level", placementLevel).apply()
                    }) { Text("Skip assessment for now") }
                } else when (page) {
                    "Today" -> {
                        Text(if (profileName.isBlank()) "Good to see you! 👋" else "Hi, $profileName! 👋",
                            color = primaryTextColor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("${MintExperiencePolicy.selectedJourney(chosenPath)} · $xp XP earned", color = secondaryTextColor)
                        Spacer(Modifier.height(14.dp))
                        Surface(color = Color(0xFF087665), shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("TODAY'S ADVENTURE", color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                Text("Speak about your day", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                                Text("One short game, then a friendly conversation.", color = Color.White)
                                Button(onClick = {
                                    showGameFilters = false
                                    gameMode = "Mint Quests"
                                    page = "Games"
                                }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF075847))) {
                                    Text("Start today's adventure →", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        OutlinedButton(onClick = {
                            lessonIndex = lessons.indices.firstOrNull { it.toString() !in completedIds } ?: 0
                            selected = ""
                            checked = false
                            page = if (completedIds.size >= lessons.size) "Review" else "Learn"
                        }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                            Text("Continue learning →")
                        }
                        Spacer(Modifier.height(14.dp))
                        Text("Explore", color = primaryTextColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Games" to "Games", "Clips" to "Feed").forEach { (label, route) ->
                                OutlinedButton(onClick = { page = route }, modifier = Modifier.weight(1f)) { Text(label) }
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { page = "Speak" }, modifier = Modifier.weight(1f)) { Text("Speak") }
                            OutlinedButton(onClick = { page = "Review" }, modifier = Modifier.weight(1f)) { Text("My progress") }
                        }
                        Spacer(Modifier.height(12.dp))
                        Surface(color = surfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Your learning path", color = primaryTextColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("${completedIds.size} of ${lessons.size} lessons completed · $dailyGoal min daily goal", color = secondaryTextColor)
                                LinearProgressIndicator(progress = { completedIds.size.toFloat() / lessons.size.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth())
                                TextButton(onClick = { page = "Learn" }) { Text("See next lesson →") }
                            }
                        }
                        OutlinedButton(onClick = { page = "Professional" }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                            Text("Explore Professional English →")
                        }
                        TextButton(onClick = { showHomeHelp = !showHomeHelp }) { Text(if (showHomeHelp) "Hide guidance" else "Where do I start?") }
                        if (showHomeHelp) {
                            Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("Start with today's adventure, play a short game and practise speaking. You can also browse games, clips or continue a lesson.", color = primaryTextColor)
                                    TextButton(onClick = { page = "Speak" }) { Text("Try speaking →") }
                                }
                            }
                        }
                    }
                    "Professional" -> {
                        MintProfessionalHub(
                            completedLessons = completedIds.size.coerceAtMost(lessons.size),
                            totalLessons = lessons.size,
                            dailyGoal = dailyGoal,
                            learningGoal = learningGoal,
                            onBack = { page = "Today" },
                            onLessons = { page = "Learn"; learnMode = "Lessons" },
                            onRoleplay = { page = "Speak"; speakingView = "Conversations" },
                            onReview = { page = "Review" }
                        )
                    }
                    "Learn" -> {
                        Text("Everyday English", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Your personalised path • $chosenDifficulty • $learningGoal", color = accentTextColor, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Lessons", "Skill practice").forEach { mode ->
                                FilterChip(selected = learnMode == mode, onClick = { learnMode = mode }, label = { Text(mode) })
                            }
                        }
                        if (learnMode == "Lessons") {
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
                        } else {
                            val tier = skillPractices.filter { it.difficulty == chosenDifficulty && (it.ageGroup == "All" || it.ageGroup == learnerAge) }
                            val ordered = tier.sortedWith(compareBy<SkillPractice> { it.ageGroup != learnerAge }.thenBy { it.goal != learningGoal }.thenBy { it.id })
                            val availablePosition = ordered.indexOfFirst { it.id !in skillMastered }.let { if (it < 0) ordered.lastIndex else it }
                            val currentPosition = skillPracticeIndex.coerceIn(0, availablePosition)
                            val exercise = ordered[currentPosition]
                            Text("Your $chosenDifficulty skill practice", style = MaterialTheme.typography.titleLarge, color = primaryTextColor, fontWeight = FontWeight.Bold)
                            Text("For $learnerAge learners • Focus: $learningGoal", color = secondaryTextColor)
                            Text("Stage ${currentPosition + 1} of ${ordered.size} • Complete each challenge to unlock the next.", color = accentTextColor)
                            LinearProgressIndicator(progress = { skillMastered.count { id -> ordered.any { it.id == id } }.toFloat() / ordered.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                            Text("${exercise.skill} • ${exercise.goal}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Text(exercise.prompt, style = MaterialTheme.typography.titleMedium, color = primaryTextColor)
                            exercise.choices.forEach { choice ->
                                OutlinedButton(onClick = { skillPracticeAnswer = choice; skillPracticeChecked = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text((if (skillPracticeAnswer == choice) "✓ " else "") + choice)
                                }
                            }
                            if (skillPracticeChecked) {
                                val correct = skillPracticeAnswer == exercise.answer
                                Text(if (correct) "Correct! ${exercise.tip}" else "Try again. ${exercise.tip}",
                                    color = if (correct) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                skillPracticeChecked = true
                                if (skillPracticeAnswer == exercise.answer && exercise.id !in skillMastered) {
                                    skillMastered = skillMastered + exercise.id
                                    xp += 10
                                    preferences.edit().putStringSet("skill_practice_mastered", skillMastered).putInt("total_xp", xp).apply()
                                }
                            }, enabled = skillPracticeAnswer.isNotBlank()) { Text("Check answer") }
                            Text("Skills mastered: ${skillMastered.count { id -> ordered.any { it.id == id } }} / ${ordered.size} at $chosenDifficulty", color = accentTextColor)
                            OutlinedButton(onClick = {
                                skillPracticeIndex = (currentPosition + 1).coerceAtMost(ordered.lastIndex)
                                skillPracticeAnswer = ""
                                skillPracticeChecked = false
                            }, enabled = exercise.id in skillMastered && currentPosition < ordered.lastIndex, modifier = Modifier.fillMaxWidth()) { Text(if (exercise.id in skillMastered && currentPosition == ordered.lastIndex) "All stages completed!" else "Next unlocked stage →") }
                        }
                    }
                    "Speak" -> {
                        Text("Speak with Mint Buddy", style = MaterialTheme.typography.headlineMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Text("Try a short, private English conversation.", color = secondaryTextColor)
                        Spacer(Modifier.height(10.dp))
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Your practice", style = MaterialTheme.typography.titleMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                                Text("${completedGuidedVoiceTurns.size} guided speaking turns completed on this device", color = secondaryTextColor)
                                Text("Voice recognition uses your device's speech provider. Quiet text is available without microphone permission.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(selected = speakingView == "Conversations", onClick = {
                                speakingView = "Conversations"
                                practiceResponse = ""
                                guidedVoiceReceived = false
                            }, label = { Text("Roleplay conversations") })
                            FilterChip(selected = speakingView == "Lessons", onClick = {
                                speakingView = "Lessons"
                                guidedVoiceReceived = false
                                practiceResponse = ""
                            }, label = { Text("Practice a lesson") })
                        }
                        if (speakingView == "Conversations") {
                            val learnerMinimumAge = when (learnerAgeBand) {
                                "4–7" -> 4; "8–12" -> 8; "13–17" -> 13; else -> 18
                            }
                            MintGuidedConversation(
                                difficulty = chosenDifficulty,
                                learnerAge = learnerMinimumAge,
                                response = practiceResponse,
                                onResponseChange = { practiceResponse = it; guidedVoiceReceived = false },
                                voiceReceived = guidedVoiceReceived,
                                requestMicrophone = { prompt ->
                                    pendingSpeechPrompt = prompt
                                    showSpeechConsent = true
                                },
                                completedKeys = completedGuidedVoiceTurns,
                                onVoiceTurnCompleted = { turnKey ->
                                    if (turnKey !in completedGuidedVoiceTurns) {
                                        completedGuidedVoiceTurns = completedGuidedVoiceTurns + turnKey
                                        preferences.edit().putStringSet("mint_guided_voice_turns", completedGuidedVoiceTurns).apply()
                                    }
                                }
                            )
                            if (speechStatus.isNotBlank()) Text(speechStatus, color = secondaryTextColor)
                        } else {
                        if (mintSpeakingMission.isNotBlank()) {
                            Surface(color = softSurfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("Mint Quest · Speaking Mission", style = MaterialTheme.typography.titleLarge, color = primaryTextColor, fontWeight = FontWeight.Bold)
                                    Text(mintSpeakingMission, color = primaryTextColor)
                                    Text("Use the microphone or quiet-mode text. This is private rehearsal, not a certified pronunciation assessment.", color = secondaryTextColor)
                                    TextButton(onClick = { mintSpeakingMission = ""; practiceChecked = false }) { Text("Return to standard speaking practice") }
                                }
                            }
                        }
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
                        Text("Listen and respond by voice or text.", color = secondaryTextColor)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                pendingSpeechPrompt = if (mintSpeakingMission.isNotBlank()) mintSpeakingMission else "Speak your answer for ${lesson.title}"
                                showSpeechConsent = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("🎙 Start speaking") }
                        if (speechStatus.isNotBlank()) Text(speechStatus, color = secondaryTextColor)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = practiceResponse,
                            onValueChange = { practiceResponse = it; guidedVoiceReceived = false; practiceChecked = false },
                            label = { Text("Your spoken or typed response") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                        if (practiceChecked && mintSpeakingMission.isNotBlank()) {
                            Spacer(Modifier.height(10.dp))
                            Text("Great effort practising your response. Re-read your words, try a clearer phrase, or speak again. Speech recognition may vary by accent and environment.", color = primaryTextColor)
                        }
                        if (practiceChecked && mintSpeakingMission.isBlank()) {
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
                    }
                    "FiveWord" -> {
                        val eligibleAge = when (learnerAgeBand) {
                            "4–7" -> 4; "8–12" -> 8; "13–17" -> 13; else -> 18
                        }
                        MintFiveWordStoryScreen(
                            age = eligibleAge,
                            onBack = { page = "Feed" },
                            onSpeaking = { prompt ->
                                mintSpeakingMission = prompt
                                speakingView = "Lessons"
                                practiceResponse = ""
                                practiceChecked = false
                                page = "Speak"
                            }
                        )
                    }
                    "Feed" -> {
                        OutlinedButton(onClick = { page = "FiveWord" }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                            Text("📖 Today's Five-Word Stories →")
                        }
                        val learnerMinimumAge = when (learnerAgeBand) {
                            "4–7" -> 4; "8–12" -> 8; "13–17" -> 13; else -> 18
                        }
                        MintFeedScreen(
                            learnerAge = learnerMinimumAge,
                            reducedMotion = motionReduced,
                            blockedIds = blockedMintClips,
                            reportedKeys = reportedMintClips,
                            reactionIds = reactedMintClips,
                            completedChallengeIds = mintStoryCompletions,
                            onBlock = { id ->
                                blockedMintClips = blockedMintClips + id
                                preferences.edit().putStringSet("mint_hidden_clips", blockedMintClips).apply()
                            },
                            onReport = { id, reason ->
                                val report = MintContentReport(id, reason, System.currentTimeMillis())
                                if (MintSocialSafety.validReport(report, MintFeedCatalog.clips.map { it.id }.toSet())) {
                                    reportedMintClips = reportedMintClips + MintSocialSafety.reportKey(report)
                                    preferences.edit().putStringSet("mint_local_clip_reports", reportedMintClips).apply()
                                }
                            },
                            onReact = { id ->
                                reactedMintClips = if (id in reactedMintClips) reactedMintClips - id else reactedMintClips + id
                                preferences.edit().putStringSet("mint_private_reactions", reactedMintClips).apply()
                            },
                            onChallengeCompleted = { id ->
                                mintStoryCompletions = mintStoryCompletions + id
                                preferences.edit().putStringSet("mint_story_completed", mintStoryCompletions).apply()
                            },
                            onSpeakingPrompt = { prompt ->
                                mintSpeakingMission = prompt
                                speakingView = "Lessons"
                                practiceResponse = ""
                                practiceChecked = false
                                page = "Speak"
                            }
                        )
                    }
                    "Games" -> {
                        Text("Games Hub", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Play, learn, grow.", color = secondaryTextColor)
                        if (!showGameFilters && gameMode == "Mint Quests") {
                            Surface(color = softSurfaceColor, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("🌟  Today's featured game", color = primaryTextColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                    Text("Mint Quests", color = primaryTextColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Text("A quick challenge from your existing game collection.", color = secondaryTextColor)
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { gameMode = "Grammar"; showGameFilters = false; grammarChecked = false; grammarChoice = "" }, modifier = Modifier.weight(1f)) { Text("Grammar") }
                            OutlinedButton(onClick = { storyOpenedFromGames = true; page = "Feed" }, modifier = Modifier.weight(1f)) { Text("Stories") }
                        }
                        TextButton(onClick = { showGameFilters = !showGameFilters }) { Text(if (showGameFilters) "Hide categories" else "All games & levels →") }
                        if (showGameFilters) {
                        Text("Recommended for $learningGoal • $chosenDifficulty", color = accentTextColor, fontWeight = FontWeight.Bold)
                        val suggestedModes = when (learningGoal) {
                            "Start speaking" -> listOf("Dialogues", "Word Builder", "Listening")
                            "School & exams" -> listOf("Grammar", "Reading", "Vocabulary")
                            "Work & interviews" -> listOf("Dialogues", "Situations", "Vocabulary")
                            "Reading & writing" -> listOf("Reading", "Grammar", "Word Builder")
                            "Travel" -> listOf("Dialogues", "Situations", "Listening")
                            else -> listOf("Vocabulary", "Situations", "Word Builder")
                        }
                        Text("Suggested: ${suggestedModes.joinToString(" • ")}", color = secondaryTextColor)
                        Text("Select a game to practise.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Mint Quests", "Word Builder", "Riddles", "Vocabulary", "Grammar", "Situations", "Listening", "Reading", "Dialogues").forEach { mode ->
                                FilterChip(selected = gameMode == mode, onClick = { gameMode = mode }, label = { Text(mode) })
                            }
                        }
                        }
                        if (gameMode == "Mint Quests") {
                            
                            if (showGameFilters) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                MintDifficulty.entries.forEach { level ->
                                    FilterChip(selected = mintDifficulty == level, onClick = {
                                        mintDifficulty = level
                                        mintGameId = ""
                                        mintChoice = ""
                                        mintSelectedTokens = emptyList()
                                        mintChecked = false
                                        mintShowHint = false
                                    }, label = { Text(level.name.lowercase().replaceFirstChar { it.uppercase() }) })
                                }
                            }
                            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                MintMechanic.entries.forEach { mechanic ->
                                    val label = when(mechanic) {
                                        MintMechanic.CHOOSE_REPLY -> "Choose Reply"
                                        MintMechanic.MISSING_WORD -> "Missing Word"
                                        MintMechanic.WORD_SCRAMBLE -> "Word Scramble"
                                        MintMechanic.SENTENCE_ORDER -> "Sentence Builder"
                                        MintMechanic.LISTEN_AND_CHOOSE -> "Listening Quest"
                                        MintMechanic.STORY_DECISION -> "Story Decisions"
                                        MintMechanic.WORD_CONNECT -> "Word Connect"
                                        MintMechanic.PICTURE_MATCH -> "Picture Match"
                                    }
                                    FilterChip(selected = mintMechanic == mechanic, onClick = {
                                        mintMechanic = mechanic
                                        mintGameId = ""
                                        mintChoice = ""
                                        mintSelectedTokens = emptyList()
                                        mintChecked = false
                                        mintShowHint = false
                                    }, label = { Text(label) })
                                }
                            }
                            }
                            val learnerMinAge = when (learnerAgeBand) {
                                "4–7" -> 4; "8–12" -> 8; "13–17" -> 13; else -> 18
                            }
                            val suggestedQuest = MintDailyQuest.select(
                                mintDifficulty, learnerMinAge,
                                java.time.LocalDate.now().toEpochDay(), mintRewardLedger
                            )
                            if (suggestedQuest != null) {
                                Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text("Fresh daily pick: ${suggestedQuest.title}", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                        Text("An age-appropriate suggestion. No streak penalty or pressure to finish.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                                        TextButton(onClick = {
                                            mintDifficulty = suggestedQuest.difficulty
                                            mintMechanic = suggestedQuest.mechanic
                                            mintGameId = suggestedQuest.id
                                            mintChoice = ""
                                            mintSelectedTokens = emptyList()
                                            mintChecked = false
                                            mintShowHint = false
                                            mintGameFeedback = ""
                                        }) { Text("Play today's suggestion →") }
                                    }
                                }
                            }
                            val availableQuests = remember(mintDifficulty, mintMechanic, learnerMinAge) {
                                MintGameCatalog.listFor(mintDifficulty, learnerMinAge).filter { it.mechanic == mintMechanic }
                            }
                            val activeQuest = availableQuests.firstOrNull { it.id == mintGameId } ?: availableQuests.firstOrNull()
                            if (activeQuest == null) {
                                Text("This combination does not have an age-appropriate pack yet. Choose another difficulty or game category.", color = secondaryTextColor)
                            } else {
                                val completed = availableQuests.count { "mint_game_${it.id}_v${it.version}" in mintRewardLedger }
                                LinearProgressIndicator(progress = { completed.toFloat() / availableQuests.size.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth())
                                Text("$completed of ${availableQuests.size} quests mastered", color = accentTextColor)
                                Surface(color = surfaceColor, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text(activeQuest.title, color = primaryTextColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                        if (activeQuest.mechanic == MintMechanic.PICTURE_MATCH) {
                                            Text(activeQuest.prompt.substringBefore(" "), style = MaterialTheme.typography.displayLarge)
                                        }
                                        Text(activeQuest.prompt, color = primaryTextColor)
                                        if (activeQuest.mechanic == MintMechanic.LISTEN_AND_CHOOSE) {
                                            val listeningLine = activeQuest.listeningLine
                                            if (!listeningLine.isNullOrBlank()) {
                                                Text("Listen to the spoken line, then choose the best answer.", color = secondaryTextColor)
                                                ListeningAudio(listeningLine)
                                                var revealAudioText by remember(activeQuest.id) { mutableStateOf(false) }
                                                TextButton(onClick = { revealAudioText = !revealAudioText }) {
                                                    Text(if (revealAudioText) "Hide transcript" else "Read accessible transcript")
                                                }
                                                if (revealAudioText) {
                                                    Text("Transcript: $listeningLine", color = secondaryTextColor)
                                                }
                                            }
                                        }
                                        if (activeQuest.mechanic == MintMechanic.STORY_DECISION) {
                                            Text("Think through the situation before selecting your response.", color = secondaryTextColor)
                                        }
                                        val isTapOrder = activeQuest.mechanic == MintMechanic.WORD_SCRAMBLE || activeQuest.mechanic == MintMechanic.SENTENCE_ORDER
                                        if (isTapOrder) {
                                            val composed = if (activeQuest.mechanic == MintMechanic.WORD_SCRAMBLE) mintSelectedTokens.joinToString("") else mintSelectedTokens.joinToString(" ")
                                            Text(if (composed.isBlank()) "Tap the pieces to build your answer." else composed, color = accentTextColor, style = MaterialTheme.typography.titleMedium)
                                            val availableTokens = activeQuest.tokens.toMutableList()
                                            mintSelectedTokens.forEach { availableTokens.remove(it) }
                                            availableTokens.forEach { token ->
                                                OutlinedButton(onClick = { mintSelectedTokens = mintSelectedTokens + token; mintChecked = false; mintGameFeedback = "" }, modifier = Modifier.fillMaxWidth()) {
                                                    Text(token, color = primaryTextColor)
                                                }
                                            }
                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                OutlinedButton(onClick = { mintSelectedTokens = mintSelectedTokens.dropLast(1); mintChecked = false }, enabled = mintSelectedTokens.isNotEmpty()) { Text("Undo") }
                                                OutlinedButton(onClick = { mintSelectedTokens = emptyList(); mintChecked = false; mintGameFeedback = "" }) { Text("Reset") }
                                            }
                                        } else {
                                            activeQuest.tokens.forEach { option ->
                                                OutlinedButton(onClick = { mintChoice = option; mintChecked = false; mintGameFeedback = "" },
                                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = if (mintChoice == option) softSurfaceColor else surfaceColor),
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text("${if (mintChoice == option) "✓ " else ""}$option", color = primaryTextColor, modifier = Modifier.fillMaxWidth())
                                                }
                                            }
                                        }
                                        TextButton(onClick = { mintShowHint = !mintShowHint }) { Text(if (mintShowHint) "Hide hint" else "Show a hint") }
                                        if (mintShowHint) Text(activeQuest.hint, color = secondaryTextColor)
                                        Button(
                                            onClick = {
                                                val response = if (isTapOrder) {
                                                    if (activeQuest.mechanic == MintMechanic.WORD_SCRAMBLE) mintSelectedTokens.joinToString("") else mintSelectedTokens.joinToString(" ")
                                                } else mintChoice
                                                val result = MintGameEngine.reward(activeQuest, response, mintRewardLedger)
                                                mintChecked = true
                                                mintGameXpAward = result.xpAward
                                                mintGameFeedback = if (result.correct) "Correct! ${activeQuest.explanation}" else "Not quite. Try again or reveal a hint."
                                                if (result.xpAward > 0 && result.rewardKey != null) {
                                                    mintRewardLedger = mintRewardLedger + result.rewardKey
                                                    xp += result.xpAward
                                                    preferences.edit().putStringSet("mint_game_awards", mintRewardLedger).putInt("total_xp", xp).apply()
                                                }
                                            },
                                            enabled = if (isTapOrder) mintSelectedTokens.isNotEmpty() else mintChoice.isNotBlank(),
                                            modifier = Modifier.fillMaxWidth()
                                        ) { Text("Check answer") }
                                        if (mintChecked) {
                                            Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                                                Text(mintGameFeedback, modifier = Modifier.padding(16.dp), color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                                            }
                                            if (activeQuest.evaluate(if (isTapOrder) {
                                                    if (activeQuest.mechanic == MintMechanic.WORD_SCRAMBLE) mintSelectedTokens.joinToString("") else mintSelectedTokens.joinToString(" ")
                                                } else mintChoice)) {
                                                Text(if (mintGameXpAward > 0) "+$mintGameXpAward XP earned!" else "Already mastered · no additional XP", color = accentTextColor, fontWeight = FontWeight.Bold)
                                                Text("Speak next: ${activeQuest.speakingPrompt}", color = primaryTextColor)
                                                OutlinedButton(onClick = {
                                                    mintSpeakingMission = activeQuest.speakingPrompt
                                                    practiceResponse = ""
                                                    practiceChecked = false
                                                    page = "Speak"
                                                    showSpeakingHint = false
                                                }, modifier = Modifier.fillMaxWidth()) { Text("Try the speaking mission →") }
                                            }
                                        }
                                        OutlinedButton(onClick = {
                                            val nextIndex = (availableQuests.indexOf(activeQuest) + 1) % availableQuests.size
                                            mintGameId = availableQuests[nextIndex].id
                                            mintChoice = ""
                                            mintSelectedTokens = emptyList()
                                            mintChecked = false
                                            mintShowHint = false
                                            mintGameFeedback = ""
                                        }, modifier = Modifier.fillMaxWidth()) { Text("Next curated quest →") }
                                    }
                                }
                            }
                        } else if (gameMode == "Word Builder") {
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
                            Text("Grammar Arena", style = MaterialTheme.typography.titleLarge, color = primaryTextColor, fontWeight = FontWeight.Bold)
                            Text("${grammarQuestions.size} available practice questions", color = secondaryTextColor)
                            LinearProgressIndicator(progress = { (grammarIndex + 1f) / grammarQuestions.size.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth())
                            val question = grammarQuestions[grammarIndex]
                            Text("Grammar ${grammarIndex + 1} of ${grammarQuestions.size}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Text(question.prompt, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                            question.options.forEachIndexed { index, choice ->
                                OutlinedButton(onClick = { grammarChoice = choice; grammarChecked = false }, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = if (grammarChoice == choice) softSurfaceColor else surfaceColor)) {
                                    Text(MintGrammarUiContract.optionLabel(index, choice), color = primaryTextColor, modifier = Modifier.fillMaxWidth())
                                }
                            }
                            if (grammarChecked) {
                                val correct = grammarChoice == question.correct
                                Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(if (correct) "✓ Correct!" else "Not quite — try another answer", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                        Text("WHY?", color = accentTextColor, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                                        Text(question.explanation, color = primaryTextColor)
                                    }
                                }
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
                            Text("Mastered ${grammarMastered.size} of ${grammarQuestions.size} · XP once per new correct answer", color = accentTextColor)
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
                        } else if (gameMode == "Reading") {
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
                        } else {
                            val availableDialogues = guidedDialogues.filter { it.level == chosenDifficulty }
                            val dialogue = availableDialogues[dialogueIndex % availableDialogues.size]
                            Text("Role-play dialogues • $chosenDifficulty", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                            Text("Choose a natural reply, then try saying it aloud in Speaking Studio.", color = secondaryTextColor)
                            Text("Dialogue ${dialogueIndex % availableDialogues.size + 1} of ${availableDialogues.size} • ${dialogue.topic}", color = accentTextColor, fontWeight = FontWeight.Bold)
                            Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("Conversation partner", color = accentTextColor, fontWeight = FontWeight.Bold)
                                    Text(dialogue.other, style = MaterialTheme.typography.titleMedium, color = primaryTextColor)
                                }
                            }
                            Text(dialogue.prompt, color = primaryTextColor, style = MaterialTheme.typography.titleMedium)
                            dialogue.options.forEach { option ->
                                OutlinedButton(
                                    onClick = { dialogueChoice = option; dialogueChecked = false },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = if (dialogueChoice == option) softSurfaceColor else surfaceColor)
                                ) { Text(option, color = primaryTextColor) }
                            }
                            if (dialogueChecked) {
                                Text(if (dialogueChoice == dialogue.correct) "Well done! ${dialogue.tip}" else "Try again. ${dialogue.tip}",
                                    color = if (dialogueChoice == dialogue.correct) Color(0xFF177B57) else Color(0xFF9D5417))
                            }
                            Button(onClick = {
                                dialogueChecked = true
                                if (dialogueChoice == dialogue.correct && dialogue.id !in dialogueCompleted) {
                                    dialogueCompleted = dialogueCompleted + dialogue.id
                                    xp += 10
                                    preferences.edit().putStringSet("dialogue_mastered", dialogueCompleted).putInt("total_xp", xp).apply()
                                }
                            }, enabled = dialogueChoice.isNotBlank()) { Text("Check reply") }
                            Text("Mastered: ${dialogueCompleted.count { id -> availableDialogues.any { it.id == id } }} / ${availableDialogues.size} at $chosenDifficulty", color = accentTextColor)
                            OutlinedButton(onClick = {
                                dialogueIndex += 1
                                dialogueChoice = ""
                                dialogueChecked = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Next conversation →") }
                        }
                    }
                    "Membership" -> {
                        MintMembershipScreen(onReturn = { page = "Profile" })
                    }
                    "Privacy" -> {
                        MintPrivacyScreen(
                            onBack = { page = "Profile" },
                            onPrivateInteractionsCleared = {
                                blockedMintClips = emptySet()
                                reportedMintClips = emptySet()
                                reactedMintClips = emptySet()
                                mintStoryCompletions = emptySet()
                            }
                        )
                    }
                    "Help" -> {
                        MintHelpScreen(
                            onBack = { page = "Profile" },
                            onGames = { page = "Games" },
                            onSpeak = { page = "Speak" },
                            onClips = { page = "Feed" }
                        )
                    }
                    "Profile" -> {
                        Text("My learning space", color = primaryTextColor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(if (profileName.isBlank()) "Your learning space" else "Learning as $profileName", color = secondaryTextColor)
                        Text("${MintExperiencePolicy.selectedJourney(chosenPath)} · $chosenDifficulty · $dailyGoal minutes per day", color = secondaryTextColor)
                        Surface(color = surfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Your progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = primaryTextColor)
                                Text("$xp XP earned · ${completedIds.size} of ${lessons.size} lessons completed", color = secondaryTextColor)
                                LinearProgressIndicator(progress = { completedIds.size.toFloat() / lessons.size.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth())
                                OutlinedButton(onClick = { page = "Review" }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("View progress & achievements →") }
                            }
                        }
                        OutlinedButton(onClick = { page = "Help" }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Help & guidance →") }
                        Button(onClick = { showProfileSettings = !showProfileSettings }, modifier = Modifier.fillMaxWidth()) {
                            Text(if (showProfileSettings) "Close settings" else "Edit profile and settings →")
                        }
                        if (showProfileSettings) {
                        Surface(color = surfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(Modifier.padding(14.dp)) {
                                Text("Privacy & local data", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                Text("Export saved learning history or manage local feed interactions.", color = secondaryTextColor)
                                OutlinedButton(onClick = { page = "Privacy" }) { Text("Open privacy controls →") }
                            }
                        }
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(Modifier.padding(16.dp)) {
                                Text("English Mint Free", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                                Text("Core games, speaking and progress stay free.", color = secondaryTextColor)
                                OutlinedButton(onClick = { page = "Membership" }) { Text("Compare Free, Plus and Pro →") }
                            }
                        }
                        Text("Appearance", style = MaterialTheme.typography.titleMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("system" to "System", "light" to "Light", "dark" to "Dark", "night" to "Night").forEach { (mode, label) ->
                                FilterChip(
                                    selected = themePreference == mode,
                                    onClick = {
                                        themePreference = mode
                                        preferences.edit().putString("mint_theme_mode", mode).apply()
                                    },
                                    label = { Text(label) }
                                )
                            }
                        }
                        Text("Scheduled Night: 8 PM–6 AM device time. Light and Dark overrides remain available.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Eye comfort: softer hero colors", color = primaryTextColor, modifier = Modifier.weight(1f))
                            Switch(checked = eyeComfort, onCheckedChange = {
                                eyeComfort = it
                                preferences.edit().putBoolean("mint_eye_comfort", it).apply()
                            })
                        }
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Reduce animations", color = primaryTextColor, modifier = Modifier.weight(1f))
                            Switch(checked = reduceMotion, onCheckedChange = {
                                reduceMotion = it
                                preferences.edit().putBoolean("mint_reduce_motion", it).apply()
                            })
                        }
                        Text("Respect device brightness, rest breaks and accessibility settings. Dark mode does not prevent eye damage.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Text("Safety and community", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Only moderated studio-authored offline reels are available. Public comments, strangers, friend requests, direct messages, uploads and sharing are disabled.", color = secondaryTextColor)
                        Text("Hidden stories: ${blockedMintClips.size} • Local reports: ${reportedMintClips.size} • Private reactions: ${reactedMintClips.size}", color = primaryTextColor)
                        OutlinedButton(onClick = {
                            blockedMintClips = emptySet()
                            preferences.edit().remove("mint_hidden_clips").apply()
                        }) { Text("Restore locally hidden studio stories") }
                        Text("Local reports have not been submitted to moderators; no public social account has been created.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        TextButton(onClick = { showProfileAdvanced = !showProfileAdvanced }) { Text(if (showProfileAdvanced) "Close learner details" else "Edit learner details") }
                        if (showProfileAdvanced) {
                        Text("Learner profile", style = MaterialTheme.typography.headlineMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Text("Update who is learning and why. Your XP, achievements and lesson history will be retained.", color = secondaryTextColor)
                        OutlinedTextField(value = profileName, onValueChange = { profileName = it.take(40) }, label = { Text("Name or nickname") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        Text("Age group", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            MintExperiencePolicy.ageBands.forEach { band ->
                                FilterChip(selected = learnerAgeBand == band, onClick = {
                                    learnerAgeBand = band
                                    learnerAge = MintExperiencePolicy.legacyAge(band)
                                    profileFeedback = ""
                                }, label = { Text(band) })
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
                        }
                    }
                    "Review" -> {
                        val progressEvidence = MintProgressEvidence(
                            xp = xp,
                            completedLessons = completedIds.size,
                            puzzlesMastered = puzzleAwarded.size + riddleAwards.size,
                            mintQuestsMastered = MintRewards.uniqueMintEventKeys(mintRewardLedger).size,
                            speakingAttempts = speakingAttempts,
                            guidedVoiceTurns = completedGuidedVoiceTurns.size,
                            revisionCorrect = revisionCorrect,
                            listeningMastered = listeningMastered.size,
                            readingMastered = readingMastered.size,
                            independentReplies = independentReplies
                        )
                        val progressLevel = MintRewards.level(xp)
                        val coins = MintRewards.cosmeticCoins(mintRewardLedger)
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
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Level ${progressLevel.number} • ${progressLevel.title}", color = primaryTextColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                LinearProgressIndicator(progress = { progressLevel.progress(xp) }, modifier = Modifier.fillMaxWidth().semantics {
                                    contentDescription = "Level ${progressLevel.number}, ${xp} total experience points"
                                })
                                Text(if (progressLevel.nextTarget == null) "You've reached the top participation level." else "${progressLevel.nextTarget!! - xp.coerceAtLeast(0)} XP to your next level", color = secondaryTextColor)
                                Text("✦ ${xp} XP  •  ◈ ${coins} cosmetic coins", color = accentTextColor, fontWeight = FontWeight.Bold)
                                Text("Coins are earned once for each unique Mint quest. They are not money or a paid wallet.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Your skill activity map", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        MintRewards.skills(progressEvidence).forEach { skill ->
                            Surface(color = surfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(skill.name, color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                                    Text(skill.evidence, color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        Text("Activity counts are not CEFR levels, accuracy percentages or verified skill mastery.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(14.dp))
                        Text("Achievement badges", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        val badges = MintRewards.badges(progressEvidence)
                        Text("${badges.count { it.earned }} / ${badges.size} earned • badges never expire", color = accentTextColor)
                        badges.forEach { badge ->
                            Surface(color = if (badge.earned) softSurfaceColor else surfaceColor, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("${if (badge.earned) "🏅" else "🔒"} ${badge.name}", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                    Text(badge.description, color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Text("Certificates and assessments", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        val certificate = MintRewards.certificate(progressEvidence)
                        Surface(color = surfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(if (certificate.eligibleForAssessment) "Assessment interest unlocked" else "Certificates locked", color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                                Text(certificate.message, color = secondaryTextColor)
                                Text("No platform-issued or externally accredited certificate is available yet.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(Modifier.height(14.dp))
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
            NavigationBar(containerColor = surfaceColor, tonalElevation = 6.dp) {
                MintUiNavigation.tabs.forEach { tab ->
                    val symbol = when (tab) {
                        "Today" -> "⌂"
                        "Speak" -> "♩"
                        "Feed" -> "▷"
                        "Games" -> "♧"
                        "Review" -> "▥"
                        else -> "◉"
                    }
                    val isActive = MintUiNavigation.mainTab(page) == tab
                    val iconColor by animateColorAsState(
                        targetValue = if (isActive) accentTextColor else secondaryTextColor,
                        animationSpec = tween(durationMillis = if (motionReduced) 0 else 200), label = "nav-$tab"
                    )
                    NavigationBarItem(
                        selected = isActive,
                        onClick = { page = tab },
                        icon = { Text(symbol, modifier = Modifier.semantics { contentDescription = "$tab navigation" }, color = iconColor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                        label = { Text(MintUiNavigation.label(tab), maxLines = 1, style = MaterialTheme.typography.labelSmall) },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = accentTextColor,
                            selectedTextColor = accentTextColor,
                            indicatorColor = if (darkAppearance) Color(0xFF134E4A) else Color(0xFFD1FAE5),
                            unselectedIconColor = secondaryTextColor,
                            unselectedTextColor = secondaryTextColor
                        )
                    )
                }
            }
            }
        }
        }
    }
}


@Composable
private fun ProgressTile(title: String, amount: String, progress: Float, first: Color, second: Color, modifier: Modifier = Modifier, reducedMotion: Boolean = false) {
    val animated by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), animationSpec = tween(durationMillis = if (reducedMotion) 0 else 500), label = "progress-$title")
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
