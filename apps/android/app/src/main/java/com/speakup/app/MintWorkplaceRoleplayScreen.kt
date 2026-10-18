package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MintWorkplaceRoleplayScreen(onVerified: (String) -> Unit = {}) {
    var scenario by remember { mutableIntStateOf(0) }
    var step by remember { mutableIntStateOf(0) }
    var choice by remember { mutableIntStateOf(-1) }
    var checked by remember { mutableStateOf(false) }
    val story = MintWorkplaceRoleplays.samples[scenario]
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Workplace conversation practice", style = MaterialTheme.typography.titleLarge)
        Text("Private, offline practice. No live AI, scores or certificates.")
        MintWorkplaceRoleplays.samples.forEachIndexed { index, sample ->
            OutlinedButton(onClick = {
                scenario = index; step = 0; choice = -1; checked = false
            }, modifier = Modifier.fillMaxWidth()) { Text(sample.title) }
        }
        if (step < story.turns.size) {
            val turn = story.turns[step]
            Text("Turn ${step + 1} of ${story.turns.size}")
            Text("${turn.speaker}: ${turn.prompt}")
            turn.options.forEachIndexed { i, option ->
                OutlinedButton(onClick = { choice = i; checked = false }, modifier = Modifier.fillMaxWidth()) {
                    Text("${i + 1}. $option", modifier = Modifier.fillMaxWidth())
                }
            }
            if (checked) {
                Text(if (choice == turn.answer) "Correct." else "Try another reply.")
                Text(turn.explanation)
            }
            Button(
                enabled = choice >= 0,
                onClick = {
                    if (checked && choice == turn.answer) {
                        val nextStep = MintWorkplaceRoleplays.advance(story, step, choice)
                        if (nextStep == story.turns.size && step == story.turns.lastIndex) {
                            onVerified(story.id)
                        }
                        step = nextStep
                        choice = -1
                        checked = false
                    } else checked = true
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (checked && choice == turn.answer) "Next turn" else "Check reply") }
        } else {
            Text("Conversation completed. Well done!")
            Button(onClick = { step = 0; choice = -1; checked = false }) { Text("Replay") }
        }
        Text("No Mint Bloom is awarded for this preview.")
    }
}
