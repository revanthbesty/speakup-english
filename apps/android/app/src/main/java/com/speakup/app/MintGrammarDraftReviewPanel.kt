package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Non-scored authoring preview of the 500-question draft bank.
 * Never writes learner progress, rewards, or activity completion.
 */
@Composable
internal fun MintGrammarDraftReviewPanel() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var index by remember { mutableIntStateOf(0) }
    var chosen by remember { mutableIntStateOf(-1) }
    var reveal by remember { mutableStateOf(false) }
    var topicOnly by remember { mutableStateOf(false) }

    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Grammar Arena — 500 draft review", style = MaterialTheme.typography.titleMedium)
            Text("Editorial preview only. No points, achievements, or learning progress are recorded.", style = MaterialTheme.typography.bodySmall)
            OutlinedButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Close draft review" else "Preview draft questions")
            }
            if (expanded) {
                val records = remember(context) {
                    runCatching { MintGrammarDraftPreview.load(context) }
                }
                records.onFailure { Text("Draft bank unavailable: ${it.message}", color = MaterialTheme.colorScheme.error) }
                records.onSuccess { bank ->
                    if (bank.isEmpty()) {
                        Text("No draft records found.")
                    } else {
                        val question = bank[index.coerceIn(bank.indices)]
                        Text("Draft ${index + 1} / ${bank.size} • ${question.topicId}", style = MaterialTheme.typography.labelLarge)
                        Text(question.prompt)
                        question.options.forEachIndexed { optionIndex, option ->
                            OutlinedButton(
                                onClick = { chosen = optionIndex; reveal = false },
                                enabled = !reveal,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${'A' + optionIndex}. $option" + if (chosen == optionIndex) " ✓" else "")
                            }
                        }
                        Button(onClick = { reveal = true }, enabled = chosen in 0..2) {
                            Text("Explain my answer")
                        }
                        if (reveal && chosen in 0..2) {
                            val correct = question.correctKey - 'A'
                            Text(if (chosen == correct) "Correct choice" else "Not the expected answer", style = MaterialTheme.typography.titleSmall)
                            Text(question.feedback[chosen])
                            if (chosen != correct) {
                                Text("Expected: ${question.correctKey}. ${question.options[correct]}")
                                Text(question.feedback[correct])
                            }
                            Text("Grammar rule: ${question.grammarRule}", style = MaterialTheme.typography.bodySmall)
                            Text("Example: ${question.exampleSentence}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { index = (index - 1).coerceAtLeast(0); chosen = -1; reveal = false },
                                enabled = index > 0
                            ) { Text("Previous") }
                            Button(
                                onClick = { index = (index + 1).coerceAtMost(bank.lastIndex); chosen = -1; reveal = false },
                                enabled = index < bank.lastIndex
                            ) { Text("Next draft") }
                        }
                    }
                }
            }
        }
    }
}
