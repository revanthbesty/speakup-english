package com.speakup.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Shared celebration for verified mastery, with an immediate reduced-motion fallback. */
@Composable
internal fun MintActivityCompletion(
    activity: String,
    newXp: Int,
    nextLabel: String,
    onNext: () -> Unit,
    onDashboard: () -> Unit,
    reducedMotion: Boolean = false
) {
    val context = LocalContext.current
    val petPreferences = remember(context) {
        context.getSharedPreferences(MintIdentity.legacyPreferencesFile, android.content.Context.MODE_PRIVATE)
    }
    val selectedPet = MintPetCatalog.find(petPreferences.getString("mint_pet_id", null))
    val collectible = MintRewardUniverse.displayName(activity)
    val fresh = MintRewardUniverse.isNewReward(newXp)
    var revealed by remember(activity, newXp) { mutableStateOf(false) }
    LaunchedEffect(activity, newXp) { revealed = true }
    val scale by animateFloatAsState(
        targetValue = if (revealed || reducedMotion) 1f else 0.85f,
        label = "Reward reveal")
    Surface(color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(if (fresh) "✨ Activity completed!" else "✓ Previously completed",
                style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(collectible.symbol, modifier = Modifier.scale(if (reducedMotion) 1f else scale),
                style = MaterialTheme.typography.displayLarge)
            Text(if (fresh) "${collectible.title} unlocked!" else "${collectible.title} already in your garden",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium)
            Text(if (fresh) "Your learning garden is growing." else "Review is always welcome. No duplicate reward.",
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            if (selectedPet != null) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MintPetPortrait(selectedPet, reducedMotion)
                    Column(Modifier.weight(1f)) {
                        Text(selectedPet.name + " says:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(if (fresh) "That was a real achievement! Our garden can grow."
                             else "Welcome back! Practice is valuable, too.",
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
            MintPrimaryAction(nextLabel, onClick = onNext)
            OutlinedButton(onClick = onDashboard, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text("View My Mint Garden")
            }
        }
    }
}
