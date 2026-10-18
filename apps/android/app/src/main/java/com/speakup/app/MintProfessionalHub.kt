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
                MintDashboardHero(
                    kicker="Professional English",
                    title="Grow your career with English",
                    description="Practical, private workplace lessons and short guided exercises.",
                    action="Continue learning  →",
                    onAction=onLessons
                )
                MintSectionHeading("Practise workplace skills","Choose a short session")
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    MintDestinationTile("Workplace", "Guided dialogues", "💬",MintVisualTokens.blue,
                        Modifier.weight(1f)) { section="Workplace" }
                    MintDestinationTile("Interviews", "Prepare confidently", "🎤",MintVisualTokens.violet,
                        Modifier.weight(1f)) { section="Interviews" }
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    MintDestinationTile("Meetings", "Present & discuss", "📊",MintVisualTokens.orange,
                        Modifier.weight(1f)) { section="Meetings" }
                    MintDestinationTile("Email writing", "Polite work messages", "✉",MintVisualTokens.pink,
                        Modifier.weight(1f)) { section="Writing" }
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    MintDestinationTile("Negotiation", "Resolve challenges", "🤝",MintVisualTokens.mint,
                        Modifier.weight(1f)) { section="Negotiation" }
                    MintDestinationTile("Industries", "Find your profession", "💼",MintVisualTokens.blue,
                        Modifier.weight(1f)) { section="Industries" }
                }
                OutlinedButton(onClick=onRoleplay,modifier=Modifier.fillMaxWidth().heightIn(min=50.dp)) {
                    Text("Guided speaking studio  →")
                }
                OutlinedButton(onClick={section="AI Life"},modifier=Modifier.fillMaxWidth().heightIn(min=50.dp)) {
                    Text("Explore real-life learning  →")
                }
                MintInfoBanner("Your practice journey",
                    "${MintProfessionalReview.completed(completedPracticeIds)} of ${MintProfessionalReview.validIds.size} original activities completed on this device • ${completedLessons.coerceAtMost(totalLessons)} of $totalLessons existing lessons.")
                Text("Next practice: ${MintProfessionalReview.label(MintProfessionalReview.next(completedPracticeIds,null))}",
                    color=MaterialTheme.colorScheme.onSurfaceVariant,style=MaterialTheme.typography.bodySmall)
                TextButton(onClick=onReview) { Text("View my learning progress  →") }
                Text("These pilot activities are not certified professional courses or AI hiring assessments.",
                    style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
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
