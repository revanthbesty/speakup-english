package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Self-contained Home companion presentation. No ownership of learner state or XP. */
@Composable
internal fun MintHomeCompanionCard(selectedPetId: String, reducedMotion: Boolean) {
    val pet = MintPetCatalog.find(selectedPetId) ?: return
    MintSectionCard("Your companion • " + pet.name) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MintPetPortrait(pet, reducedMotion)
            Spacer(Modifier.width(10.dp))
            Text("We can learn together, one small step at a time.",
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
