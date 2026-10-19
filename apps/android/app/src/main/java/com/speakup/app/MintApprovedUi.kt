package com.speakup.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.ProgressBarRangeInfo

/** Shared UI primitives from the approved English Mint mint/teal/purple direction.
 * These preserve the parent screen's existing navigation and activity behavior.
 */
internal object MintDesign {
    val Mint = Color(0xFF10B981)
    val Purple = Color(0xFF8B5CF6)
    val Teal = Color(0xFF06B6D4)
    val Navy = Color(0xFF111827)
    val OffWhite = Color(0xFFF8FAFC)
    val Pink = Color(0xFFEC4899)
    val Amber = Color(0xFFF59E0B)
    val MinimumTouchTarget = 48.dp
    val CardRadius = 24.dp
}

@Composable
internal fun MintScreenHeading(title: String, subtitle: String, eyebrow: String = "ENGLISH MINT") {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        Text(eyebrow, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold, color = MintDesign.Mint)
        Text(title, style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
internal fun MintSectionCard(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            content()
        }
    }
}

@Composable
internal fun MintAnswerChoice(label: String, selected: Boolean, onClick: () -> Unit,
                              modifier: Modifier = Modifier, enabled: Boolean = true) {
    Surface(onClick = onClick, enabled = enabled, modifier = modifier.fillMaxWidth().heightIn(min = 60.dp)
            .semantics { stateDescription = if (selected) "Selected answer" else "Answer not selected" },
        shape = RoundedCornerShape(19.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(if (selected) 2.dp else 1.dp,
            if (selected) MintDesign.Mint else MaterialTheme.colorScheme.outlineVariant)) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(if (selected) "✓" else "○", fontWeight = FontWeight.Bold,
                color = if (selected) MintDesign.Mint else MaterialTheme.colorScheme.onSurfaceVariant)
            Text(label, style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
internal fun MintPrimaryAction(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MintDesign.Mint,
            contentColor = Color(0xFF063A2B))) {
        Text(label, fontWeight = FontWeight.Bold)
    }
}

/** Sprint 1 primitives shared by all subsequent UI sprints. */
@Composable
internal fun MintSecondaryAction(label: String, onClick: () -> Unit, enabled: Boolean = true) {
    OutlinedButton(
        onClick = onClick, enabled = enabled,
        modifier = Modifier.fillMaxWidth().heightIn(min = MintDesign.MinimumTouchTarget),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) { Text(label, fontWeight = FontWeight.SemiBold) }
}

@Composable
internal fun MintLearningProgress(completed: Int, total: Int, label: String = "Learning progress") {
    val safeTotal = total.coerceAtLeast(1)
    val safeCompleted = completed.coerceIn(0, safeTotal)
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("$label · $safeCompleted of $safeTotal",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        LinearProgressIndicator(
            progress = { safeCompleted.toFloat() / safeTotal },
            modifier = Modifier.fillMaxWidth().heightIn(min = 8.dp).semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    safeCompleted.toFloat(), 0f..safeTotal.toFloat()
                )
            },
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
internal fun MintStatusMessage(
    title: String,
    detail: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    isError: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(MintDesign.CardRadius),
        color = if (isError) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isError) MaterialTheme.colorScheme.onErrorContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant)
            Text(detail, style = MaterialTheme.typography.bodyMedium,
                color = if (isError) MaterialTheme.colorScheme.onErrorContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant)
            if (actionLabel != null && onAction != null) {
                MintSecondaryAction(actionLabel, onClick = onAction)
            }
        }
    }
}

/** Shared Sprint 1 selection and input controls; state remains owned by the caller. */
@Composable
internal fun MintChoiceChip(label: String, selected: Boolean, onClick: () -> Unit,
                            enabled: Boolean = true) {
    FilterChip(
        selected = selected, onClick = onClick, enabled = enabled,
        label = { Text(label) },
        modifier = Modifier.heightIn(min = MintDesign.MinimumTouchTarget),
        shape = RoundedCornerShape(16.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
internal fun MintTextEntry(value: String, onValueChange: (String) -> Unit,
                           label: String, modifier: Modifier = Modifier,
                           enabled: Boolean = true, singleLine: Boolean = true) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, enabled = enabled,
        singleLine = singleLine, label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    )
}

/** Accessible, non-blocking loading state; no shimmer when reduced motion is requested. */
@Composable
internal fun MintLoadingState(message: String = "Loading your activity") {
    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
        Text(message, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface)
    }
}

/** A reusable inline feedback banner; the caller supplies truthful outcomes. */
@Composable
internal fun MintFeedbackBanner(title: String, explanation: String, correct: Boolean) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (correct) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.errorContainer
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (correct) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer)
            Text(explanation, style = MaterialTheme.typography.bodyMedium,
                color = if (correct) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer)
        }
    }
}

/** Reward presentation only. XP and award eligibility must come from the existing reward engine. */
@Composable
internal fun MintAchievementCelebration(
    visible: Boolean,
    title: String,
    message: String,
    reduceMotion: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier.fillMaxWidth(),
        enter = if (reduceMotion) fadeIn(animationSpec = androidx.compose.animation.core.snap())
                else fadeIn(animationSpec = androidx.compose.animation.core.tween(280)) +
                     expandVertically(animationSpec = androidx.compose.animation.core.tween(320)),
        exit = if (reduceMotion) fadeOut(animationSpec = androidx.compose.animation.core.snap())
               else fadeOut(animationSpec = androidx.compose.animation.core.tween(180)) +
                    shrinkVertically(animationSpec = androidx.compose.animation.core.tween(200))
    ) {
        Surface(shape = RoundedCornerShape(MintDesign.CardRadius),
            color = MaterialTheme.colorScheme.primaryContainer) {
            Row(Modifier.fillMaxWidth().padding(18.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text("★", style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(title, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(message, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}
