package com.speakup.app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

internal data class MintPetOption(val id: String, val name: String, val personality: String, val color: Color)
internal object MintPetCatalog {
    val pets = listOf(
        MintPetOption("mint_fox", "Mint Fox", "Curious explorer", Color(0xFF10B981)),
        MintPetOption("buddy_panda", "Buddy Panda", "Calm supporter", Color(0xFF64748B)),
        MintPetOption("spark_cat", "Spark Cat", "Playful inventor", Color(0xFF8B5CF6)),
        MintPetOption("wise_owl", "Wise Owl", "Thoughtful guide", Color(0xFFF59E0B)),
        MintPetOption("joy_rabbit", "Joy Rabbit", "Energetic encourager", Color(0xFFEC4899)),
        MintPetOption("tiny_dragon", "Tiny Dragon", "Imaginative companion", Color(0xFF06B6D4))
    )
    fun normalized(id: String?): String = id?.takeIf { key -> pets.any { it.id == key } } ?: ""
    fun find(id: String?): MintPetOption? = pets.firstOrNull { it.id == id }
}

/** Pose profiles keep the six companions distinctive even before final vector assets arrive. */
internal object MintPetMotion {
    fun encouragementRotation(id: String): Float = when (id) {
        "mint_fox" -> 7f
        "buddy_panda" -> -4f
        "spark_cat" -> -9f
        "wise_owl" -> 5f
        "joy_rabbit" -> -7f
        "tiny_dragon" -> 11f
        else -> 0f
    }
    fun celebrationRotation(id: String): Float = when (id) {
        "mint_fox" -> -11f
        "buddy_panda" -> 5f
        "spark_cat" -> 14f
        "wise_owl" -> -6f
        "joy_rabbit" -> 9f
        "tiny_dragon" -> -15f
        else -> 0f
    }
}
/** Original simple geometric character previews; final illustrated art is a separate acceptance gate. */
@Composable
internal fun MintPetPortrait(pet: MintPetOption, reducedMotion: Boolean, modifier: Modifier = Modifier, celebrate: Boolean = false,
                             encourage: Boolean = false) {
    val pose = remember(pet.id) { Animatable(0f) }
    LaunchedEffect(pet.id, celebrate, encourage, reducedMotion) {
        pose.snapTo(0f)
        if (celebrate && !reducedMotion) {
            pose.animateTo(1f, animationSpec = keyframes {
                durationMillis = 520
                0.2f at 90
                1f at 255
                0.55f at 390
                0f at 520
            })
        } else if (encourage && !reducedMotion) {
            pose.animateTo(1f, animationSpec = keyframes {
                durationMillis = 440
                0.25f at 110
                1f at 230
                0.45f at 330
                0f at 440
            })
        }
    }
    val growth = animateFloatAsState(1f, tween(if (reducedMotion) 0 else 300), label = "pet-portrait")
    Canvas(modifier.size(74.dp).graphicsLayer {
        rotationZ = (if (encourage && !celebrate) MintPetMotion.encouragementRotation(pet.id)
                     else MintPetMotion.celebrationRotation(pet.id)) * pose.value
        translationY = (if (encourage && !celebrate) 3.dp.toPx() else -7.dp.toPx()) * pose.value
        scaleX = 1f + .06f * pose.value
        scaleY = 1f + .06f * pose.value
    }.semantics { contentDescription = pet.name + " companion illustration" }) {
        val radius = size.minDimension * .31f * growth.value
        val center = Offset(size.width / 2, size.height * .54f)
        drawCircle(pet.color.copy(alpha = .2f), size.minDimension * .48f, center)
        drawCircle(pet.color, radius, center)
        // Character-specific appendages; made from native shapes so they render offline.
        when (pet.id) {
            "mint_fox" -> {
                val left = androidx.compose.ui.graphics.Path().apply {
                    moveTo(center.x-radius*.92f, center.y-radius*.48f)
                    lineTo(center.x-radius*.78f, center.y-radius*1.52f)
                    lineTo(center.x-radius*.16f, center.y-radius*.91f)
                    close()
                }
                val right = androidx.compose.ui.graphics.Path().apply {
                    moveTo(center.x+radius*.92f, center.y-radius*.48f)
                    lineTo(center.x+radius*.78f, center.y-radius*1.52f)
                    lineTo(center.x+radius*.16f, center.y-radius*.91f)
                    close()
                }
                drawPath(left, pet.color)
                drawPath(right, pet.color)
                drawCircle(Color.White.copy(alpha=.75f), radius*.24f,
                    Offset(center.x, center.y+radius*.53f))
            }
            "buddy_panda" -> {
                drawCircle(Color(0xFF334155), radius*.29f,
                    Offset(center.x-radius*.48f,center.y-radius*.12f))
                drawCircle(Color(0xFF334155), radius*.29f,
                    Offset(center.x+radius*.48f,center.y-radius*.12f))
                drawCircle(Color.White, radius*.16f,
                    Offset(center.x-radius*.42f,center.y-radius*.12f))
                drawCircle(Color.White, radius*.16f,
                    Offset(center.x+radius*.42f,center.y-radius*.12f))
            }
            "spark_cat" -> {
                drawCircle(Color(0xFFFDA4AF), radius*.14f,
                    Offset(center.x,center.y+radius*.27f))
                drawLine(Color.White, Offset(center.x-radius*.45f,center.y+radius*.37f),
                    Offset(center.x-radius*1.1f,center.y+radius*.25f), 1.5.dp.toPx())
                drawLine(Color.White, Offset(center.x+radius*.45f,center.y+radius*.37f),
                    Offset(center.x+radius*1.1f,center.y+radius*.25f), 1.5.dp.toPx())
            }
            "wise_owl" -> {
                drawCircle(Color(0xFFFFF7ED),radius*.38f,
                    Offset(center.x-radius*.37f, center.y-radius*.08f))
                drawCircle(Color(0xFFFFF7ED),radius*.38f,
                    Offset(center.x+radius*.37f, center.y-radius*.08f))
                val beak = androidx.compose.ui.graphics.Path().apply {
                    moveTo(center.x,center.y+radius*.2f)
                    lineTo(center.x-radius*.17f,center.y+radius*.43f)
                    lineTo(center.x+radius*.17f,center.y+radius*.43f)
                    close()
                }
                drawPath(beak,Color(0xFFF59E0B))
            }
            "joy_rabbit" -> {
                drawOval(pet.color,topLeft=Offset(center.x-radius*.65f,center.y-radius*2.03f),
                    size=androidx.compose.ui.geometry.Size(radius*.44f,radius*1.24f))
                drawOval(pet.color,topLeft=Offset(center.x+radius*.2f,center.y-radius*2.03f),
                    size=androidx.compose.ui.geometry.Size(radius*.44f,radius*1.24f))
                drawCircle(Color.White,radius*.16f,
                    Offset(center.x,center.y+radius*.57f))
            }
            "tiny_dragon" -> {
                val wing = androidx.compose.ui.graphics.Path().apply {
                    moveTo(center.x-radius*.85f,center.y+radius*.23f)
                    lineTo(center.x-radius*1.6f,center.y-radius*.52f)
                    lineTo(center.x-radius*1.34f,center.y+radius*.56f)
                    close()
                }
                drawPath(wing,pet.color.copy(alpha=.8f))
                drawCircle(Color(0xFFFDE68A),radius*.12f,
                    Offset(center.x,center.y+radius*.36f))
            }
        }
        val earOffset = when(pet.id) {
            "joy_rabbit" -> size.height * .31f
            "wise_owl" -> size.height * .12f
            else -> size.height * .18f
        }
        val earRadius = if (pet.id == "buddy_panda") radius * .5f else radius * .32f
        drawCircle(pet.color, earRadius, Offset(center.x - radius * .7f, center.y - earOffset))
        drawCircle(pet.color, earRadius, Offset(center.x + radius * .7f, center.y - earOffset))
        drawCircle(Color.White, radius * .19f, Offset(center.x-radius*.38f, center.y-radius*.08f))
        drawCircle(Color.White, radius * .19f, Offset(center.x+radius*.38f, center.y-radius*.08f))
        drawCircle(Color(0xFF111827), radius*.09f, Offset(center.x-radius*.38f,center.y-radius*.08f))
        drawCircle(Color(0xFF111827), radius*.09f, Offset(center.x+radius*.38f,center.y-radius*.08f))
        // Expressive features are tied to the answer state and remain static when motion is reduced.
        if (celebrate) {
            drawCircle(Color(0xFFFFD166), radius * .16f,
                Offset(center.x + radius * .78f, center.y + radius * .52f))
            drawCircle(Color(0xFFFFD166), radius * .10f,
                Offset(center.x - radius * .85f, center.y - radius * .48f))
        }
        if (encourage && !celebrate) {
            drawCircle(Color(0xFFFFB4C8), radius * .15f,
                Offset(center.x - radius * .72f, center.y + radius * .36f))
            drawCircle(Color(0xFFFFB4C8), radius * .15f,
                Offset(center.x + radius * .72f, center.y + radius * .36f))
        }
        // Emotions change eye and mouth geometry, not only the pet's movement.
        if (celebrate) {
            drawArc(Color.White, 15f, 155f, false,
                topLeft=Offset(center.x-radius*.35f,center.y+radius*.03f),
                size=androidx.compose.ui.geometry.Size(radius*.7f,radius*.62f),
                style=Stroke(width=2.5.dp.toPx()))
            drawCircle(Color.White, radius*.045f,
                Offset(center.x-radius*.43f,center.y-radius*.25f))
            drawCircle(Color.White, radius*.045f,
                Offset(center.x+radius*.43f,center.y-radius*.25f))
        } else if (encourage) {
            drawArc(Color.White, 40f, 100f, false,
                topLeft=Offset(center.x-radius*.18f,center.y+radius*.23f),
                size=androidx.compose.ui.geometry.Size(radius*.36f,radius*.19f),
                style=Stroke(width=2.dp.toPx()))
        } else {
            drawArc(Color.White, 25f, 130f, false,
                topLeft=Offset(center.x-radius*.28f,center.y+radius*.06f),
                size=androidx.compose.ui.geometry.Size(radius*.56f,radius*.52f),
                style=Stroke(width=2.dp.toPx()))
        }
    }
}

@Composable
internal fun MintPetPicker(selectedId: String, onSelect: (String) -> Unit,
                           reducedMotion: Boolean, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Choose your learning companion", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold)
        Text("All six are free. You can change your companion later or choose Not now.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        MintPetCatalog.pets.chunked(2).forEach { pair ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                pair.forEach { pet ->
                    val selected = selectedId == pet.id
                    Surface(
                        onClick = { onSelect(pet.id) },
                        modifier = Modifier.weight(1f).heightIn(min = 154.dp)
                            .semantics { contentDescription = pet.name + ", " + pet.personality +
                                if (selected) ", selected" else ", tap to select" },
                        shape = RoundedCornerShape(20.dp),
                        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(if (selected) 2.dp else 1.dp,
                            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            MintPetPortrait(pet, reducedMotion)
                            Text(pet.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Text(pet.personality, style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (selected) Text("✓ Selected", color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
        MintSecondaryAction("Not now — continue without a pet", onClick = { onSelect("") })
    }
}
