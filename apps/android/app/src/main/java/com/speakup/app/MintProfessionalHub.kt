package com.speakup.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Local professional-English route. Unpublished courses are visibly unavailable,
 * not represented as enrolled courses or live AI.
 */
@Composable
internal fun MintProfessionalHub(
    completedLessons: Int,
    totalLessons: Int,
    dailyGoal: Int,
    learningGoal: String,
    onBack: () -> Unit,
    onLessons: () -> Unit,
    onRoleplay: () -> Unit,
    onReview: () -> Unit
) {
    var section by remember { mutableStateOf("Hub") }
    var industry by remember { mutableStateOf("Business") }
    var industryQuery by remember { mutableStateOf("") }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var answerChecked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val progressPreferences = remember(context) {
        context.getSharedPreferences("speakup_learning", Context.MODE_PRIVATE)
    }
    var confirmClearPractice by remember { mutableStateOf(false) }
    var completedPracticeIds by remember(progressPreferences) {
        mutableStateOf(
            (progressPreferences.getStringSet(MintProfessionalReview.storageKey, emptySet()) ?: emptySet()).toSet()
        )
    }
    val markVerified: (String) -> Unit = { id ->
        val updated = MintProfessionalReview.recordVerified(completedPracticeIds, id, true)
        if (updated != completedPracticeIds) {
            completedPracticeIds = updated
            progressPreferences.edit().putStringSet(MintProfessionalReview.storageKey, updated).apply()
        }
    }

    BackHandler(enabled = section != "Hub") {
        section = if (section == "Industry") "Industries" else "Hub"
    }
    val industries = listOf("Business", "IT & Technology", "Healthcare", "Finance", "Customer Service", "Education", "Hospitality", "Retail")
    if (confirmClearPractice) {
        AlertDialog(
            onDismissRequest = { confirmClearPractice = false },
            title = { Text("Delete Professional practice history?") },
            text = { Text("Only saved verified Professional practice IDs will be deleted on this device. General lesson progress and Mint rewards remain unchanged.") },
            confirmButton = {
                TextButton(onClick = {
                    progressPreferences.edit().remove(MintProfessionalReview.storageKey).apply()
                    completedPracticeIds = emptySet()
                    confirmClearPractice = false
                }) { Text("Delete practice history") }
            },
            dismissButton = {
                TextButton(onClick = { confirmClearPractice = false }) { Text("Cancel") }
            }
        )
    }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        TextButton(onClick = { if (section == "Hub") onBack() else section = "Hub" }) { Text("← " + if (section == "Hub") "Home" else "Professional English") }
        when (section) {
            "Hub" -> {
                Text("Professional English", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Practice English for work, at your own pace.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Core text practice works without a paid AI account or internet. Optional audio availability depends on the installed device engine.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Your next step", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("Start with an existing lesson or rehearse a conversation.", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Button(onClick = onLessons, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Continue learning →") }
                    }
                }
                OutlinedButton(onClick = { section = "Industries" }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Explore career topics →") }
                OutlinedButton(onClick = onRoleplay, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Practise a workplace conversation →") }
                OutlinedButton(onClick = { section = "Workplace" }, modifier = Modifier.fillMaxWidth()) { Text("Offline workplace roleplay pilot →") }
                OutlinedButton(onClick = { section = "Interviews" }, modifier = Modifier.fillMaxWidth()) { Text("Interview preparation pilot →") }
                OutlinedButton(onClick = { section = "Meetings" }, modifier = Modifier.fillMaxWidth()) { Text("Meetings & presentations pilot →") }
                OutlinedButton(onClick = { section = "Writing" }, modifier = Modifier.fillMaxWidth()) { Text("Email & business writing pilot →") }
                OutlinedButton(onClick = { section = "Negotiation" }, modifier = Modifier.fillMaxWidth()) { Text("Negotiation & problem-solving pilot →") }
                Surface(shape = RoundedCornerShape(16.dp),color=MaterialTheme.colorScheme.surfaceVariant) {
                    Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                        Text("Your professional practice suggestion",fontWeight=FontWeight.Bold)
                        Text("Your goal: $learningGoal. Suggestions prioritize original exercises not yet completed on this device; they do not estimate proficiency.")
                        Text("Verified original practice: ${MintProfessionalReview.completed(completedPracticeIds)} of ${MintProfessionalReview.validIds.size} items on this device.")
                        Text("Suggested next: ${MintProfessionalReview.label(MintProfessionalReview.next(completedPracticeIds,null))}",style=MaterialTheme.typography.bodySmall)
                        Text("Practice completion is recorded after checked-correct answers; workplace roleplays require all three turns. No XP, badges or certificates are granted.",style=MaterialTheme.typography.bodySmall)
                        if (completedPracticeIds.isNotEmpty()) {
                            TextButton(onClick = { confirmClearPractice = true }) {
                                Text("Delete Professional practice history")
                            }
                        }
                    }
                }

                OutlinedButton(onClick = { section = "AI Life" }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Everyday real-life scenarios →") }
                Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surface) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Recommended for your goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(learningGoal, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Based on your selected learning goal; not an AI-generated recommendation.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        TextButton(onClick = onRoleplay) { Text("Try guided speaking →") }
                    }
                }
                Text("Your progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$completedLessons of $totalLessons existing lessons completed · $dailyGoal-minute daily goal")
                LinearProgressIndicator(progress = { completedLessons.toFloat() / totalLessons.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth())
                TextButton(onClick = onReview) { Text("Review my learning →") }
            }
            "Workplace" -> { MintWorkplaceRoleplayScreen(onVerified = markVerified) }
            "Interviews" -> { MintInterviewPrepScreen(onVerified = markVerified) }
            "Meetings" -> { MintMeetingPracticeScreen(onVerified = markVerified) }
            "Writing" -> { MintBusinessWritingScreen(onVerified = markVerified) }
            "Negotiation" -> { MintNegotiationScreen(onVerified = markVerified) }
            "Industries" -> {
                Text("Choose your industry", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Browse possible professional pathways. Only existing general lessons are playable today.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(value = industryQuery, onValueChange = { industryQuery = it.take(60) }, label = { Text("Search industries") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                val matchingIndustries = industries.filter { it.contains(industryQuery.trim(), ignoreCase = true) }
                if (matchingIndustries.isEmpty()) Text("No matching industries. Try another search.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                matchingIndustries.forEach { name ->
                    OutlinedButton(onClick = { industry = name; section = "Industry"; selectedAnswer = -1; answerChecked = false }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("$name →", modifier = Modifier.fillMaxWidth()) }
                }
            }
            "Industry" -> {
                Text(industry, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("A dedicated $industry course is not published yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                val pilotItem = MintProfessionalPilot.items.firstOrNull { it.industry == industry }
                if (pilotItem != null) {
                    Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Original offline practice · ${pilotItem.situation}", fontWeight = FontWeight.Bold)
                            Text(pilotItem.prompt)
                            pilotItem.answers.forEachIndexed { index, answer ->
                                OutlinedButton(onClick = { selectedAnswer = index; answerChecked = false },
                                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                                    Text("${if (selectedAnswer == index) "●" else "○"} ${index + 1}. $answer", modifier = Modifier.fillMaxWidth())
                                }
                            }
                            Button(onClick = { answerChecked = true; if (selectedAnswer == pilotItem.correctIndex) markVerified(pilotItem.id) }, enabled = selectedAnswer >= 0,
                                modifier = Modifier.fillMaxWidth()) { Text("Check answer") }
                            if (answerChecked) {
                                Text(if (selectedAnswer == pilotItem.correctIndex) "Correct!" else "Try another reply.")
                                Text(pilotItem.explanation)
                            }
                            Text("Preview only. No XP, certificate or new Mint Bloom is awarded.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Available now", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Use the existing English lessons and guided conversations for transferable communication practice.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Button(onClick = onLessons, modifier = Modifier.fillMaxWidth()) { Text("Open existing lessons") }
                        OutlinedButton(onClick = onRoleplay, modifier = Modifier.fillMaxWidth()) { Text("Practise speaking") }
                    }
                }
                TextButton(onClick = { section = "Industries" }) { Text("← All industries") }
            }
            else -> {
                Text("Real-life practice", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Live AI scenarios are not enabled. Practise with existing guided conversations or regular lessons instead.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = onRoleplay, modifier = Modifier.fillMaxWidth()) { Text("Open guided conversations") }
                OutlinedButton(onClick = onLessons, modifier = Modifier.fillMaxWidth()) { Text("Open lessons") }
            }
        }
    }
}
