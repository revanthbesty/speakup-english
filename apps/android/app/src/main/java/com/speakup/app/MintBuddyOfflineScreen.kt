package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Text-only deterministic practice. No conversation is persisted or transmitted. */
@Composable
internal fun MintBuddyOfflineScreen() {
    var level by remember { mutableStateOf(MintBuddyLevel.EASY) }
    var typed by remember { mutableStateOf("") }
    var lastReply by remember { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        MintScreenHeading("Chat with Mint Buddy", "Short, private English practice at your pace.", "SPEAK • OFFLINE")
        Text("Choose a level and write a sentence. Replies are scripted practice prompts, not generative AI or proficiency scores.",
            color=MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            MintBuddyLevel.entries.forEach { option ->
                FilterChip(selected=level==option,onClick={level=option;lastReply=null},
                    label={Text(option.name.lowercase().replaceFirstChar { it.uppercase() })})
            }
        }
        OutlinedTextField(
            value=typed,
            onValueChange={typed=it.take(240);lastReply=null},
            label={Text("Your practice sentence")},
            supportingText={Text("${typed.length}/240 characters · not saved or sent")},
            modifier=Modifier.fillMaxWidth(),
            minLines=2
        )
        MintPrimaryAction("Get a practice prompt", onClick = { lastReply=MintBuddyOffline.response(level,typed) })
        lastReply?.let { reply ->
            Surface(color=MaterialTheme.colorScheme.secondaryContainer,
                shape=androidx.compose.foundation.shape.RoundedCornerShape(14.dp)) {
                Text(reply,modifier=Modifier.fillMaxWidth().padding(14.dp))
            }
        }
        OutlinedButton(onClick={typed="";lastReply=null},modifier=Modifier.fillMaxWidth()) {
            Text("Clear conversation")
        }
        Text("No audio, on-device language model, paid tokens, cloud message or retained transcript is used.",
            style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
