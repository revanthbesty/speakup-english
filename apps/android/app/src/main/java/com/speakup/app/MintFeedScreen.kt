package com.speakup.app

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Finite feed of captioned animated illustrated reels.
 * No third-party video player/stream, public posts, user-uploaded media or strangers.
 */
@Composable
internal fun MintFeedScreen(
    learnerAge: Int,
    reducedMotion: Boolean,
    blockedIds: Set<String>,
    reportedKeys: Set<String>,
    reactionIds: Set<String>,
    completedChallengeIds: Set<String>,
    onBlock: (String) -> Unit,
    onReport: (String, MintReportReason) -> Unit,
    onReact: (String) -> Unit,
    onChallengeCompleted: (String) -> Unit,
    onSpeakingPrompt: (String) -> Unit
) {
    var level by remember { mutableStateOf(MintClipLevel.EASY) }
    var clipIndex by remember { mutableIntStateOf(0) }
    var frameIndex by remember { mutableIntStateOf(0) }
    var playing by remember { mutableStateOf(false) }
    var captionVisible by remember { mutableStateOf(true) }
    var choice by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var reportOpen by remember { mutableStateOf(false) }
    var infoOpen by remember { mutableStateOf(false) }
    var showListening by remember { mutableStateOf(false) }
    val visibleClips = MintFeedCatalog.visible(learnerAge, level, blockedIds)
    val currentIndex = clipIndex.coerceIn(0, (visibleClips.size - 1).coerceAtLeast(0))
    val clip = visibleClips.getOrNull(currentIndex)

    fun reset(next: Int) {
        clipIndex = next
        frameIndex = 0
        playing = false
        choice = ""
        checked = false
        showListening = false
    }

    LaunchedEffect(clip?.id, frameIndex, playing, reducedMotion) {
        if (playing && !reducedMotion && clip != null) {
            delay(2800L)
            if (frameIndex < clip.frames.lastIndex) frameIndex += 1 else playing = false
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mint Reels", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("Short illustrated English stories, not an endless algorithmic feed. Watch, read, play, then speak.")
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MintClipLevel.entries.forEach { option ->
                FilterChip(selected = level == option, onClick = { level = option; reset(0) },
                    label = { Text(option.name.lowercase().replaceFirstChar { it.uppercase() }) })
            }
        }
        if (clip == null) {
            Text("No age-appropriate stories are available in this level. Try another level.")
            OutlinedButton(onClick = { level = MintClipLevel.EASY; reset(0) }) { Text("Explore beginner stories") }
        } else {
            Text("STORY ${currentIndex + 1} / ${visibleClips.size} · ${clip.title}", fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary)
            val frame = clip.frames[frameIndex.coerceIn(0, clip.frames.lastIndex)]
            var dragDistance by remember(clip.id) { mutableFloatStateOf(0f) }
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth().pointerInput(clip.id, currentIndex, visibleClips.size) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { _, dragAmount -> dragDistance += dragAmount },
                        onDragEnd = {
                            if (dragDistance < -70f && currentIndex < visibleClips.lastIndex) reset(currentIndex + 1)
                            if (dragDistance > 70f && currentIndex > 0) reset(currentIndex - 1)
                            dragDistance = 0f
                        },
                        onDragCancel = { dragDistance = 0f }
                    )
                },
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    Modifier.background(
                        Brush.verticalGradient(listOf(Color(0xFF0A6D68), Color(0xFF133B4C), Color(0xFF10202C))),
                        RoundedCornerShape(28.dp)
                    ).padding(22.dp).animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("ENGLISH MINT ORIGINAL • CAPTIONED STORY", color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelSmall)
                    Box(Modifier.fillMaxWidth().height(128.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text(frame.visual, style = MaterialTheme.typography.displayLarge)
                    }
                    Text(if (captionVisible) frame.caption else "Captions hidden",
                        color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(frame.spokenLine, color = Color.White.copy(alpha = 0.9f))
                    LinearProgressIndicator(progress = { (frameIndex + 1).toFloat() / clip.frames.size },
                        modifier = Modifier.fillMaxWidth(), color = Color(0xFF7FE5C8))
                    Text("Scene ${frameIndex + 1} of ${clip.frames.size}", color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { frameIndex = (frameIndex - 1).coerceAtLeast(0); playing = false },
                            enabled = frameIndex > 0) { Text("Back") }
                        Button(onClick = { if (frameIndex < clip.frames.lastIndex) {
                            frameIndex += 1; playing = false
                        } else playing = false }, enabled = frameIndex < clip.frames.lastIndex) {
                            Text("Next scene")
                        }
                    }
                    if (!reducedMotion) {
                        OutlinedButton(onClick = {
                            if (frameIndex == clip.frames.lastIndex) frameIndex = 0
                            playing = !playing
                        }) { Text(if (playing) "Pause scenes" else "Play scenes") }
                    } else Text("Reduced motion: manual scene controls are enabled.", color = Color.White)
                    Text("Swipe up for next story · swipe down for previous story", color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.labelSmall)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { captionVisible = !captionVisible }) {
                    Text(if (captionVisible) "Hide captions" else "Show captions")
                }
                OutlinedButton(onClick = { showListening = !showListening }) {
                    Text(if (showListening) "Hide audio" else "Listen to line")
                }
            }
            if (showListening) ListeningAudio(frame.spokenLine)
            Text(clip.description, style = MaterialTheme.typography.bodyMedium)
            Text("Studio-authored content • no public comments or strangers", style = MaterialTheme.typography.labelMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onReact(clip.id) }) {
                    Text(if (clip.id in reactionIds) "♥ Saved reaction" else "♡ React privately")
                }
                OutlinedButton(onClick = { infoOpen = true }) { Text("Safety tools") }
            }
            Text("Quick English challenge", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(clip.challengePrompt)
            clip.choices.forEach { option ->
                OutlinedButton(
                    onClick = { choice = option; checked = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor =
                        if (choice == option) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                ) { Text(option, modifier = Modifier.fillMaxWidth()) }
            }
            Button(onClick = {
                checked = true
                if (choice == clip.challengeAnswer) onChallengeCompleted(clip.id)
            }, enabled = choice.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Check my answer") }
            if (checked) Text(if (choice == clip.challengeAnswer) "Correct! You've understood this story." else
                "Give it another try. You can replay the scenes and read the captions.")
            if (clip.id in completedChallengeIds) Text("✓ Story challenge completed", color = MaterialTheme.colorScheme.primary)
            OutlinedButton(onClick = { onSpeakingPrompt("Explain what happened in '${clip.title}' using one or two English sentences.") },
                modifier = Modifier.fillMaxWidth()) { Text("🎙 Try the speaking challenge") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { reset(currentIndex - 1) }, enabled = currentIndex > 0) { Text("Previous story") }
                OutlinedButton(onClick = { reset(currentIndex + 1) }, enabled = currentIndex < visibleClips.lastIndex) { Text("Next story") }
            }
            if (currentIndex == visibleClips.lastIndex) {
                Text("You're all caught up! Take a break, or revisit a story. No infinite autoplay.",
                    fontWeight = FontWeight.SemiBold)
            }
            if (infoOpen) AlertDialog(
                onDismissRequest = { infoOpen = false },
                title = { Text("Your feed controls") },
                text = { Text("Hide this story or report its content on this device. Reports cannot reach moderators until a secure moderation service is connected; public comments and messaging remain disabled.") },
                confirmButton = { TextButton(onClick = { infoOpen = false; onBlock(clip.id); reset(0) }) { Text("Hide this story") } },
                dismissButton = { TextButton(onClick = { infoOpen = false; reportOpen = true }) { Text("Report story") } }
            )
            if (reportOpen) AlertDialog(
                onDismissRequest = { reportOpen = false },
                title = { Text("Report this story") },
                text = {
                    Column {
                        Text("Reports are saved locally only; they have not been sent to an online moderator.")
                        MintReportReason.entries.forEach { reason ->
                            TextButton(onClick = {
                                onReport(clip.id, reason)
                                reportOpen = false
                            }) { Text(reason.name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }) }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { reportOpen = false }) { Text("Cancel") } }
            )
            val localReports = reportedKeys.count { it.startsWith("${clip.id}:") }
            if (localReports > 0) Text("This story has $localReports locally saved report type(s). No moderation submission has occurred.")
        }
    }
}
