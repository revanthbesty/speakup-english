package com.speakup.app

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

/** Shared UI primitives from the approved English Mint mint/teal/purple direction.
 * These preserve the parent screen's existing navigation and activity behavior.
 */
internal object MintDesign {
    val Mint = Color(0xFF10B981)
    val Purple = Color(0xFF8B5CF6)
    val Teal = Color(0xFF06B6D4)
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
