package com.speakup.app

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** UI-only reaction to a checked answer. Never awards XP, plays audio or assumes mastery. */
internal object MintPetReactionCopy {
    fun message(correct: Boolean, petId: String): String {
        val pet = MintPetCatalog.find(petId) ?: return ""
        if (!correct) return when (pet.id) {
            "wise_owl" -> "Let's look for a clue and try another way."
            "buddy_panda" -> "Keep going gently. You can try again."
            "spark_cat" -> "What a puzzle! Let's test another idea."
            "joy_rabbit" -> "One more try! I'm cheering for your effort."
            "tiny_dragon" -> "Every attempt lights the way forward."
            else -> "We'll explore another path together."
        }
        return when (pet.id) {
            "wise_owl" -> "You noticed the clue. Well reasoned!"
            "buddy_panda" -> "Steady progress. Nicely done!"
            "spark_cat" -> "Your idea clicked! Great work!"
            "joy_rabbit" -> "You did it! What a joyful discovery!"
            "tiny_dragon" -> "A new spark of understanding!"
            else -> "A fresh path unlocked in your learning!"
        }
    }
}

@Composable
internal fun MintPetAnswerReaction(
    checked: Boolean,
    correct: Boolean,
    reducedMotion: Boolean
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember(context) {
        context.getSharedPreferences(MintIdentity.legacyPreferencesFile, Context.MODE_PRIVATE)
    }
    val pet = MintPetCatalog.find(prefs.getString("mint_pet_id", null))
    if (pet != null) {
        AnimatedVisibility(
            visible = checked,
            enter = fadeIn(if (reducedMotion) snap() else tween(220)),
            exit = fadeOut(if (reducedMotion) snap() else tween(150))
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MintPetPortrait(pet, reducedMotion, celebrate = correct)
                Column(Modifier.weight(1f)) {
                    Text(pet.name, style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                    Text(MintPetReactionCopy.message(correct, pet.id),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
