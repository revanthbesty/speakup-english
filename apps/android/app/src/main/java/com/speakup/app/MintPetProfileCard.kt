package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Isolated from MainActivity's large navigation composition to avoid JVM method limits. */
@Composable
internal fun MintPetProfileCard(
    selectedId: String,
    reducedMotion: Boolean,
    onSelect: (String) -> Unit
) {
    var editing by remember { mutableStateOf(false) }
    MintSectionCard("My Mint Pet") {
        val currentPet = MintPetCatalog.find(selectedId)
        if (!editing) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (currentPet != null) MintPetPortrait(currentPet, reducedMotion)
                Column(Modifier.weight(1f)) {
                    Text(currentPet?.name ?: "No companion selected",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Text(currentPet?.personality ?: "You can add a companion whenever you like.",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
            MintSecondaryAction("Change companion", onClick = { editing = true })
        } else {
            MintPetPicker(selectedId = selectedId, onSelect = { id ->
                onSelect(id)
                editing = false
            }, reducedMotion = reducedMotion)
            MintSecondaryAction("Cancel", onClick = { editing = false })
        }
    }
}
