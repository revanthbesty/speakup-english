package com.speakup.app

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Offline guided dialogue UI. Replies stay in Compose memory; saved history stores
 * only unique completed-turn keys, never raw transcripts or microphone audio.
 */
@Composable
internal fun MintGuidedConversation(
    difficulty: String,
    learnerAge: Int,
    response: String,
    onResponseChange: (String) -> Unit,
    voiceReceived: Boolean,
    requestMicrophone: (String) -> Unit,
    completedKeys: Set<String>,
    onVoiceTurnCompleted: (String) -> Unit
) {
    var chosenLevel by remember(difficulty) {
        mutableStateOf(MintConversationEngine.recommendedLevel(difficulty))
    }
    var selectedScenarioId by remember { mutableStateOf("") }
    var showMoreChoices by remember { mutableStateOf(false) }
    var turnIndex by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf<MintVoiceFeedback?>(null) }
    var hintShown by remember { mutableStateOf(false) }
    var showTranscript by remember { mutableStateOf(false) }
    val options = MintConversationEngine.scenarios(chosenLevel, learnerAge)
    val scenario = options.firstOrNull { it.id == selectedScenarioId } ?: options.firstOrNull()
    val turn = scenario?.turns?.getOrNull(turnIndex)
    Surface(color = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.onBackground) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Step 2 · Speak with Mia", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Listen and reply. You can type instead of speaking.")
        TextButton(onClick = { showMoreChoices = !showMoreChoices }) { Text(if (showMoreChoices) "Hide conversation choices" else "Change level or topic") }
        if (showMoreChoices) {
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MintConversationLevel.entries.forEach { level ->
                FilterChip(
                    selected = chosenLevel == level,
                    onClick = {
                        chosenLevel = level
                        selectedScenarioId = ""
                        turnIndex = 0
                        result = null
                        hintShown = false
                        onResponseChange("")
                    },
                    label = { Text(level.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }
        }
        if (options.isEmpty()) {
            Text("No age-appropriate conversation is available at this difficulty. Choose another level.")
        } else {
            if (showMoreChoices) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { item ->
                    FilterChip(
                        selected = scenario?.id == item.id,
                        onClick = {
                            selectedScenarioId = item.id
                            turnIndex = 0
                            result = null
                            hintShown = false
                            onResponseChange("")
                        },
                        label = { Text(item.title) }
                    )
                }
            }
            }
            if (scenario != null && turn != null) {
                val progress = (turnIndex + 1).toFloat() / scenario.turns.size
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                Text("TURN ${turnIndex + 1} / ${scenario.turns.size} • ${scenario.title}", fontWeight = FontWeight.Bold)
                Text(scenario.description)
                Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceContainer) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("${turn.speaker} says:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text(turn.prompt, style = MaterialTheme.typography.titleLarge)
                        Text(when (turn.mode) {
                            MintVoiceMode.SPEAK_TO_COMPLETE -> "Speak to complete"
                            MintVoiceMode.LISTEN_THEN_SPEAK -> "Listen, then speak"
                            MintVoiceMode.PLAY_THEN_SPEAK -> "Play, then speak"
                        }, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (turn.mode == MintVoiceMode.LISTEN_THEN_SPEAK) {
                    ListeningAudio(turn.listeningText)
                } else {
                    TextButton(onClick = { showTranscript = !showTranscript }) {
                        Text(if (showTranscript) "Hide prompt transcript" else "Review prompt transcript")
                    }
                    if (showTranscript) Text(turn.listeningText)
                }
                OutlinedButton(onClick = { hintShown = !hintShown }) { Text(if (hintShown) "Hide hint" else "Show hint") }
                if (hintShown) Text(turn.hint)
                Button(onClick = { requestMicrophone(turn.prompt) }, modifier = Modifier.fillMaxWidth()) {
                    Text("🎙 Speak privately")
                }
                Text("Microphone is optional. Your device speech provider may process audio.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = response,
                    onValueChange = { onResponseChange(it); result = null },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Recognized words or quiet-mode text") },
                    supportingText = { Text(if (voiceReceived) "Speech-to-text result — check for mistakes" else "Quiet text practice — does not count as speaking") },
                    minLines = 2
                )
                Button(
                    onClick = {
                        val feedback = MintConversationEngine.evaluate(
                            turn, response, if (voiceReceived) MintResponseSource.SPEECH else MintResponseSource.TEXT)
                        result = feedback
                        if (feedback.speakingCounted) onVoiceTurnCompleted("${scenario.id}:${turn.id}")
                    },
                    enabled = response.isNotBlank(), modifier = Modifier.fillMaxWidth()
                ) { Text("Get supportive feedback") }
                if (result != null) {
                    val currentResult = result!!
                    Text(currentResult.message)
                    Text("Try this: ${currentResult.ideaSuggestion}")
                    Text(if (currentResult.speakingCounted) "Spoken turn recorded as participation." else "Quiet rehearsal: no spoken assessment credit.")
                    Text("Recognition confidence and pronunciation accuracy were not measured.")
                    Button(onClick = {
                        turnIndex = (turnIndex + 1) % scenario.turns.size
                        result = null
                        hintShown = false
                        showTranscript = false
                        onResponseChange("")
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(if (turnIndex == scenario.turns.lastIndex) "Finish and restart quest" else "Next dialogue turn →")
                    }
                }
                val finished = scenario.turns.count { "${scenario.id}:${it.id}" in completedKeys }
                Text("$finished / ${scenario.turns.size} spoken turns practised in this quest")
                TextButton(onClick = { turnIndex = 0; result = null; onResponseChange("") }) {
                    Text("Restart this dialogue")
                }
            }
        }
    }
    }
}
