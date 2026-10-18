package com.speakup.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Reusable UI-only components based on the approved English Mint visual boards.
 * Keep app data, completion rules, permissions and navigation callbacks in screen owners.
 */
internal object MintVisualTokens {
    val mint = Color(0xFF10B981)
    val deepMint = Color(0xFF047857)
    val violet = Color(0xFF7C3AED)
    val blue = Color(0xFF2563EB)
    val pink = Color(0xFFDB2777)
    val orange = Color(0xFFF59E0B)
    val ink = Color(0xFF101A35)
    val light = Color(0xFFF8FAFC)
}

@Composable
internal fun MintSectionHeading(title: String, description: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground)
        if (!description.isNullOrBlank()) {
            Text(description, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
internal fun MintDashboardHero(
    kicker: String, title: String, description: String,
    action: String, onAction: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(listOf(MintVisualTokens.violet, MintVisualTokens.blue, MintVisualTokens.deepMint)))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(kicker.uppercase(), style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.86f))
                Text(title, style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Surface(color = Color.White.copy(alpha = 0.17f), shape = RoundedCornerShape(22.dp)) {
                Image(painter = painterResource(id = R.drawable.mint_mark),
                    contentDescription = "English Mint leaf logo",
                    modifier = Modifier.size(86.dp).padding(8.dp))
            }
        }
        Text(description, style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.93f))
        Button(
            onClick = onAction,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White,
                contentColor = MintVisualTokens.deepMint),
            shape = RoundedCornerShape(18.dp)
        ) { Text(action, fontWeight = FontWeight.Bold) }
    }
}

@Composable
internal fun MintDestinationTile(
    title: String, description: String, symbol: String, tint: Color,
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 114.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp, shadowElevation = 2.dp
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Surface(shape = RoundedCornerShape(12.dp), color = tint.copy(alpha = 0.16f)) {
                Text(symbol, modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.titleLarge)
            }
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(description, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
internal fun MintInfoBanner(title: String, message: String, tint: Color = MintVisualTokens.violet) {
    Surface(shape = RoundedCornerShape(18.dp), color = tint.copy(alpha = 0.12f)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(message, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
internal fun MintGrammarEntry(
    mastered: Int, total: Int, onStart: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        MintSectionHeading("Grammar Arena", "Practise real, locally authored questions. Choose a pack to start.")
        MintInfoBanner("3 beginner practice packs", "$total pilot questions are available offline. Full reviewed content is still in preparation.")
        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("$mastered of $total questions mastered", fontWeight = FontWeight.Bold)
                LinearProgressIndicator(progress = { mastered.toFloat() / total.coerceAtLeast(1) },
                    modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp)))
                (0..2).forEach { pack ->
                    OutlinedButton(
                        onClick = { onStart(pack) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) { Text("Pack ${pack + 1} · 6 questions  →", modifier = Modifier.fillMaxWidth()) }
                }
            }
        }
        Text("Practice results show learning activity, not certified grammar proficiency.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
