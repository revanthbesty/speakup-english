package com.speakup.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Visual onboarding rebuilt to follow the approved English Mint UI sprint board.
 * Activity still owns existing profile, privacy and placement persistence.
 */
@Composable
internal fun MintWelcomeFlow(
    ageBand: String, goal: String, name: String,
    onAge: (String) -> Unit, onGoal: (String) -> Unit,
    onName: (String) -> Unit, onFinish: (Boolean) -> Unit
) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    val ink = MaterialTheme.colorScheme.onBackground
    val subtle = MaterialTheme.colorScheme.onSurfaceVariant
    val mint = Color(0xFF10B981)
    val purple = Color(0xFF8B5CF6)
    val goals = listOf(
        "Start speaking" to "🎤", "Everyday conversation" to "💬",
        "School & exams" to "🎓", "Work & interviews" to "💼",
        "Reading & writing" to "📖", "Travel" to "🌍"
    )
    Column(Modifier.fillMaxWidth().padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,
            horizontalArrangement=Arrangement.SpaceBetween) {
            Text("YOUR ENGLISH ADVENTURE",style=MaterialTheme.typography.labelMedium,
                color=mint,fontWeight=FontWeight.Bold)
            Text("STEP ${step+1} OF 4",color=subtle,style=MaterialTheme.typography.labelMedium)
        }
        LinearProgressIndicator(progress={(step+1)/4f},modifier=Modifier.fillMaxWidth().height(7.dp),
            color=mint,trackColor=MaterialTheme.colorScheme.surfaceVariant)
        if(step==0) {
            Box(
                modifier=Modifier.fillMaxWidth().height(252.dp).background(
                    Brush.linearGradient(listOf(Color(0xFFBDF9E5),Color(0xFFE7DAFF),Color(0xFFCCEAFF))),
                    RoundedCornerShape(28.dp)),
                contentAlignment=Alignment.Center
            ) {
                Column(horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    Text("🌱",style=MaterialTheme.typography.displayLarge)
                    Text("💬      🎮      🎧",style=MaterialTheme.typography.headlineLarge)
                    Text("FRESH GAMES. FLUENT VOICES.",color=Color(0xFF164637),
                        style=MaterialTheme.typography.labelLarge,fontWeight=FontWeight.Bold)
                }
            }
            Text("Play. Practice. Speak.",style=MaterialTheme.typography.headlineLarge,
                fontWeight=FontWeight.ExtraBold,color=ink)
            Text("Learn English the fun way with games, stories, speaking practice and daily discoveries.",
                style=MaterialTheme.typography.bodyLarge,color=subtle)
            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(7.dp)) {
                listOf("🎮 Games","📚 Stories","🎤 Speak").forEach { text ->
                    Surface(color=MaterialTheme.colorScheme.primaryContainer,
                        shape=RoundedCornerShape(30.dp)) {
                        Text(text,Modifier.padding(horizontal=12.dp,vertical=9.dp),
                            style=MaterialTheme.typography.labelMedium)
                    }
                }
            }
        } else if(step==1) {
            Text("Who is learning?",style=MaterialTheme.typography.headlineLarge,
                fontWeight=FontWeight.Bold,color=ink)
            Text("Choose an age group for suitable activities and privacy options.",color=subtle)
            listOf(
                "4–7" to "🌈", "8–12" to "🎒", "13–17" to "🎧",
                "18–49" to "💼", "50+" to "🌟"
            ).forEach { (band,symbol) ->
                val picked=ageBand==band
                Surface(onClick={onAge(band)},shape=RoundedCornerShape(20.dp),
                    color=if(picked)MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    border=androidx.compose.foundation.BorderStroke(
                        if(picked)2.dp else 1.dp,
                        if(picked)mint else MaterialTheme.colorScheme.outline.copy(alpha=0.4f))
                ) {
                    Row(Modifier.fillMaxWidth().heightIn(min=54.dp).padding(horizontal=17.dp),
                        verticalAlignment=Alignment.CenterVertically,
                        horizontalArrangement=Arrangement.spacedBy(16.dp)) {
                        Text(symbol,style=MaterialTheme.typography.headlineSmall)
                        Text(band,modifier=Modifier.weight(1f),color=ink,fontWeight=FontWeight.SemiBold)
                        if(picked) Text("✓",color=mint,fontWeight=FontWeight.Bold)
                    }
                }
            }
            if(ageBand in listOf("4–7","8–12","13–17"))
                Text("Young learners stay private. A parent or guardian should guide setup. Public messaging is unavailable.",
                    style=MaterialTheme.typography.bodySmall,color=subtle)
        } else if(step==2) {
            Text("What's your goal?",style=MaterialTheme.typography.headlineLarge,
                fontWeight=FontWeight.Bold,color=ink)
            Text("Pick your first learning adventure. You can change this any time.",color=subtle)
            // Compact two-column choices keep the primary CTA reachable on phones.
            goals.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    pair.forEach { (option, symbol) ->
                        val picked = goal == option
                        Surface(onClick = { onGoal(option) },
                            modifier = Modifier.weight(1f).heightIn(min = 94.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = if (picked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(if (picked) 2.dp else 1.dp,
                                if (picked) mint else MaterialTheme.colorScheme.outlineVariant)) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(symbol, style = MaterialTheme.typography.titleLarge)
                                Text(option, style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (picked) FontWeight.Bold else FontWeight.Medium,
                                    color = ink)
                                if (picked) Text("✓ Selected", style = MaterialTheme.typography.labelSmall, color = mint)
                            }
                        }
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxWidth().height(170.dp).background(
                Brush.linearGradient(listOf(Color(0xFFBDF9E5),Color(0xFFE7DAFF))),
                RoundedCornerShape(24.dp)),contentAlignment=Alignment.Center) {
                Text("🌱  ✨  🏆",style=MaterialTheme.typography.displayMedium)
            }
            Text("Your adventure begins! ✨",style=MaterialTheme.typography.headlineLarge,
                fontWeight=FontWeight.ExtraBold,color=ink)
            Text("Your first adventure is ready. You can start with games and practise speaking whenever you like.",color=subtle)
            OutlinedTextField(value=name,onValueChange={onName(it.take(40))},
                singleLine=true,label={Text("Your name (optional)")},
                modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp))
            Text("Optional: take a short level check, or head straight to Home.",
                style=MaterialTheme.typography.bodySmall,color=subtle)
        }
        MintPrimaryAction(if (step == 0) "Let\'s begin →" else if (step == 3) "Start my adventure →" else "Continue →",
            onClick = { if (step == 3) onFinish(false) else step++ })
        if(step==3) {
            TextButton(onClick={onFinish(true)},modifier=Modifier.fillMaxWidth()) {
                Text("Take the optional level check",color=purple)
            }
        } else {
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,
                verticalAlignment=Alignment.CenterVertically) {
                TextButton(onClick={ if(step>0) step-- },enabled=step>0) {Text("← Back")}
                TextButton(onClick={step=3}) {Text("Skip to my adventure")}
            }
        }
    }
}
