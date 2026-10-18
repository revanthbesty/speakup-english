package com.speakup.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
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
internal fun MintBrandHeader() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Image(painter = painterResource(id = R.drawable.mint_mark),
            contentDescription = "English Mint logo", modifier = Modifier.size(44.dp))
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text("English Mint", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
            Text("Fresh Games. Fluent Voices.", style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
        }
    }
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

@Composable
internal fun MintTabGlyph(tab: String, color: Color) {
    Canvas(Modifier.size(25.dp)) {
        val u = size.minDimension / 24f
        val outline = Stroke(width = 2.2f*u, cap = StrokeCap.Round)
        when(tab) {
            "Today" -> {
                val house = Path().apply {
                    moveTo(3*u,11*u); lineTo(12*u,3*u); lineTo(21*u,11*u)
                    lineTo(21*u,21*u); lineTo(14*u,21*u); lineTo(14*u,15*u)
                    lineTo(10*u,15*u); lineTo(10*u,21*u); lineTo(3*u,21*u); close()
                }
                drawPath(house, color, style = outline)
            }
            "Games" -> {
                drawRoundRect(color, topLeft=Offset(2*u,7*u),
                    size=androidx.compose.ui.geometry.Size(20*u,13*u),
                    cornerRadius=CornerRadius(5*u), style=outline)
                drawLine(color,Offset(7*u,10*u),Offset(7*u,16*u),strokeWidth=2*u,cap=StrokeCap.Round)
                drawLine(color,Offset(4*u,13*u),Offset(10*u,13*u),strokeWidth=2*u,cap=StrokeCap.Round)
                drawCircle(color,1.3f*u,Offset(16*u,12*u))
                drawCircle(color,1.3f*u,Offset(19*u,15*u))
            }
            "Feed" -> {
                drawRoundRect(color,topLeft=Offset(2*u,4*u),
                    size=androidx.compose.ui.geometry.Size(20*u,16*u),
                    cornerRadius=CornerRadius(3*u), style=outline)
                val play=Path().apply {
                    moveTo(10*u,8*u);lineTo(16*u,12*u);lineTo(10*u,16*u);close()
                }
                drawPath(play,color)
            }
            "Speak" -> {
                drawRoundRect(color, topLeft=Offset(8*u,3*u),
                    size=androidx.compose.ui.geometry.Size(8*u,13*u),
                    cornerRadius=CornerRadius(4*u),style=outline)
                val stand=Path().apply {
                    moveTo(5*u,12*u);lineTo(5*u,14*u)
                    cubicTo(5*u,20*u,19*u,20*u,19*u,14*u)
                    lineTo(19*u,12*u)
                }
                drawPath(stand,color,style=outline)
                drawLine(color,Offset(12*u,20*u),Offset(12*u,23*u),strokeWidth=2*u)
                drawLine(color,Offset(8*u,23*u),Offset(16*u,23*u),strokeWidth=2*u)
            }
            else -> {
                drawCircle(color,4.3f*u,Offset(12*u,8*u),style=outline)
                val shoulders=Path().apply {
                    moveTo(3*u,21*u)
                    cubicTo(3*u,12*u,21*u,12*u,21*u,21*u)
                }
                drawPath(shoulders,color,style=outline)
            }
        }
    }
}

@Composable
internal fun MintChoiceTile(label: String, subtitle: String, symbol: String,
    selected: Boolean, onClick: () -> Unit
) {
    Surface(onClick=onClick, modifier=Modifier.fillMaxWidth().heightIn(min=68.dp),
        shape=RoundedCornerShape(18.dp),
        color=if(selected)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tonalElevation=if(selected)3.dp else 1.dp, shadowElevation=if(selected)2.dp else 0.dp
    ) {
        Row(Modifier.fillMaxWidth().padding(13.dp),
            horizontalArrangement=Arrangement.spacedBy(12.dp),
            verticalAlignment=Alignment.CenterVertically) {
            Surface(color=MintVisualTokens.violet.copy(alpha=0.13f),shape=RoundedCornerShape(12.dp)) {
                Text(symbol, modifier=Modifier.padding(horizontal=10.dp,vertical=6.dp),
                    style=MaterialTheme.typography.titleLarge)
            }
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(2.dp)) {
                Text(label,style=MaterialTheme.typography.titleSmall,fontWeight=FontWeight.Bold)
                Text(subtitle,style=MaterialTheme.typography.labelSmall,
                    color=MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if(selected) Text("✓", color=MaterialTheme.colorScheme.primary,
                fontWeight=FontWeight.Bold, style=MaterialTheme.typography.titleLarge)
        }
    }
}
@Composable
internal fun MintProgressBar(value:Float, modifier:Modifier=Modifier) {
    Box(modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.onSurface.copy(alpha=0.1f))) {
        val fraction=value.coerceIn(0f,1f)
        if(fraction>0f) {
            Box(Modifier.fillMaxWidth(fraction).fillMaxHeight()
                .background(Brush.horizontalGradient(
                    listOf(MintVisualTokens.mint,MintVisualTokens.blue))))
        }
    }
}

@Composable
internal fun MintProfileHero(displayName:String,journey:String,lessons:Int,totalLessons:Int,xp:Int) {
    Surface(color=MaterialTheme.colorScheme.primaryContainer,
        shape=RoundedCornerShape(24.dp),modifier=Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically,
                horizontalArrangement=Arrangement.spacedBy(14.dp)) {
                Surface(color=MaterialTheme.colorScheme.surface,
                    shape=RoundedCornerShape(22.dp)) {
                    Image(painter=painterResource(R.drawable.mint_mark),
                        contentDescription="English Mint learner profile",
                        modifier=Modifier.size(64.dp).padding(8.dp))
                }
                Column {
                    Text(displayName.ifBlank{"English learner"},
                        style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
                    Text(journey, style=MaterialTheme.typography.bodySmall,
                        color=MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                Surface(shape=RoundedCornerShape(16.dp),
                    color=MaterialTheme.colorScheme.surface,modifier=Modifier.weight(1f)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("$lessons/$totalLessons",fontWeight=FontWeight.ExtraBold,
                            style=MaterialTheme.typography.titleLarge,color=MaterialTheme.colorScheme.primary)
                        Text("Lessons",style=MaterialTheme.typography.labelSmall)
                    }
                }
                Surface(shape=RoundedCornerShape(16.dp),
                    color=MaterialTheme.colorScheme.surface,modifier=Modifier.weight(1f)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("$xp",fontWeight=FontWeight.ExtraBold,
                            style=MaterialTheme.typography.titleLarge,color=MaterialTheme.colorScheme.primary)
                        Text("Earned XP",style=MaterialTheme.typography.labelSmall)
                    }
                }
            }
            MintProgressBar(lessons.toFloat()/totalLessons.coerceAtLeast(1))
        }
    }
}
