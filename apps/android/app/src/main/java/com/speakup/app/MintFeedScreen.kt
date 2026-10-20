package com.speakup.app

import androidx.compose.animation.animateContentSize
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    savedWords: Set<String>,
    onToggleWord: (String) -> Unit,
    restoredClipId: String? = null,
    restoredFrameIndex: Int = 0,
    restoredLevel: MintClipLevel = MintClipLevel.EASY,
    onSpeakingPrompt: (String, String, Int, MintClipLevel) -> Unit
) {
    var level by rememberSaveable { mutableStateOf(restoredLevel) }
    var clipIndex by rememberSaveable { mutableIntStateOf(0) }
    var frameIndex by rememberSaveable { mutableIntStateOf(restoredFrameIndex) }
    var playing by remember { mutableStateOf(false) }
    var captionVisible by rememberSaveable { mutableStateOf(true) }
    var largeCaptions by rememberSaveable { mutableStateOf(false) }
    var choice by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var reportOpen by remember { mutableStateOf(false) }
    var infoOpen by remember { mutableStateOf(false) }
    var showListening by remember { mutableStateOf(false) }
    var wordSheetOpen by remember { mutableStateOf(false) }
    var speakMomentOpen by rememberSaveable { mutableStateOf(false) }
    var suggestedReplyVisible by rememberSaveable { mutableStateOf(false) }
    val visibleClips = MintClipRecommendations.ordered(MintFeedCatalog.visible(learnerAge, level, blockedIds), completedChallengeIds, reactionIds)
    var restoredIdConsumed by rememberSaveable { mutableStateOf(false) }
    val currentIndex = if (restoredIdConsumed) MintClipsPlayback.safeIndex(clipIndex, visibleClips.size)
        else MintClipsPlayback.restoreIndex(visibleClips.map { it.id }, restoredClipId, clipIndex)
    val clip = visibleClips.getOrNull(currentIndex)

    fun reset(next: Int) {
        restoredIdConsumed = true
        clipIndex = MintClipsPlayback.safeIndex(next, visibleClips.size)
        frameIndex = 0
        playing = false
        choice = ""
        checked = false
        showListening = false
        wordSheetOpen = false
        speakMomentOpen = false
        suggestedReplyVisible = false
    }

    LaunchedEffect(clip?.id, frameIndex, playing, reducedMotion, wordSheetOpen, speakMomentOpen, infoOpen, reportOpen) {
        if (clip != null && MintClipsPlayback.mayAutoAdvance(playing, reducedMotion, wordSheetOpen || speakMomentOpen || infoOpen || reportOpen)) {
            delay(2800L)
            if (frameIndex < clip.frames.lastIndex) frameIndex = MintClipsPlayback.next(frameIndex, clip.frames.size) else playing = false
        }
    }

    Surface(color = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.onBackground) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape=RoundedCornerShape(24.dp),
            color=MaterialTheme.colorScheme.primaryContainer,modifier=Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment=androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement=Arrangement.spacedBy(14.dp)) {
                Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                    Text("Mint Clips",style=MaterialTheme.typography.headlineSmall,
                        color=MaterialTheme.colorScheme.onPrimaryContainer,fontWeight=FontWeight.ExtraBold)
                    Text("Watch a story. Learn a phrase. Practise speaking.",
                        color=MaterialTheme.colorScheme.onPrimaryContainer,
                        style=MaterialTheme.typography.bodyMedium)
                }
                Text("🎬",style=MaterialTheme.typography.displayMedium)
            }
        }
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
            val frame = clip.frames[MintClipsPlayback.safeIndex(frameIndex, clip.frames.size)]
            var dragDistance by remember(clip.id) { mutableFloatStateOf(0f) }
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth().pointerInput(clip.id, currentIndex, visibleClips.size) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { _, dragAmount -> dragDistance += dragAmount },
                        onDragEnd = {
                            val target = MintClipsPlayback.swipeTarget(currentIndex, visibleClips.size, dragDistance)
                            if (target != currentIndex) reset(target)
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
                    ).padding(22.dp).then(if (reducedMotion) Modifier else Modifier.animateContentSize()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("ENGLISH MINT ORIGINAL • CAPTIONED STORY", color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelSmall)
                    Box(Modifier.fillMaxWidth().height(128.dp).semantics { contentDescription = frame.caption }, contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text(frame.visual, style = MaterialTheme.typography.displayLarge)
                    }
                    Text(if (captionVisible) frame.caption else "Captions hidden",
                        color = Color.White, style = if (largeCaptions) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(frame.spokenLine, color = Color.White.copy(alpha = 0.9f))
                    LinearProgressIndicator(progress = { MintClipsPlayback.progress(frameIndex, clip.frames.size) },
                        modifier = Modifier.fillMaxWidth(), color = Color(0xFF7FE5C8))
                    Text("Scene ${frameIndex + 1} of ${clip.frames.size}", color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { frameIndex = MintClipsPlayback.previous(frameIndex, clip.frames.size); playing = false },
                            enabled = frameIndex > 0) { Text("Back") }
                        Button(onClick = { if (frameIndex < clip.frames.lastIndex) {
                            frameIndex = MintClipsPlayback.next(frameIndex, clip.frames.size); playing = false
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
                    Text("Swipe up for next story or use Next Story", color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.labelSmall)
                }
            }
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { captionVisible = !captionVisible }) {
                    Text(if (captionVisible) "Hide captions" else "Show captions")
                }
                OutlinedButton(onClick = { largeCaptions = !largeCaptions }, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(if (largeCaptions) "Standard captions" else "Larger captions")
                }
                OutlinedButton(onClick = { playing = false; showListening = !showListening }) {
                    Text(if (showListening) "Hide audio" else "Listen to line")
                }
            }
            OutlinedButton(onClick = { playing = false; wordSheetOpen = true }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text("Words from this scene")
            }
            if (wordSheetOpen) AlertDialog(
                onDismissRequest = { wordSheetOpen = false },
                title = { Text("Words in context") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(frame.spokenLine, color = MaterialTheme.colorScheme.onSurface)
                        Text("Read or listen to the sentence. Offline dictionary definitions aren't available yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        val candidates = frame.spokenLine.lowercase().split(Regex("[^a-z]+")).mapNotNull { MintSavedVocabulary.normalized(it) }.distinct().take(8)
                        candidates.forEach { word ->
                            OutlinedButton(onClick = { onToggleWord(word) }, modifier = Modifier.fillMaxWidth()) {
                                Text(if (word in savedWords) "✓ $word · remove saved word" else "+ Save $word")
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { wordSheetOpen = false }) { Text("Back to clip") } }
            )
            if (showListening) ListeningAudio(frame.spokenLine)
            Text(clip.description, style = MaterialTheme.typography.bodyMedium)
            Text("Original offline story · no public chat", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { playing = false; onReact(clip.id) }) {
                    Text(if (clip.id in reactionIds) "♥ Saved reaction" else "♡ React privately")
                }
                OutlinedButton(onClick = { playing = false; infoOpen = true }) { Text("Safety tools") }
            }
            Text("Story Challenge", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
            OutlinedButton(onClick = {
                playing = false
                suggestedReplyVisible = false
                speakMomentOpen = true
            }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("🎙 Pause & speak (optional)") }
            if (speakMomentOpen) AlertDialog(
                onDismissRequest = { speakMomentOpen = false },
                title = { Text("Your speaking moment") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Tell the story in one or two English sentences.", color = MaterialTheme.colorScheme.onSurface)
                        if (suggestedReplyVisible) Text(
                            "Suggested reply: ${frame.spokenLine}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text("Speaking is optional. Skipping does not count as spoken practice.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        OutlinedButton(onClick = { suggestedReplyVisible = !suggestedReplyVisible },
                            modifier = Modifier.fillMaxWidth()) {
                            Text(if (suggestedReplyVisible) "Hide example" else "Suggested reply")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        speakMomentOpen = false
                        onSpeakingPrompt("Explain what happened in '${clip.title}' using one or two English sentences.", clip.id, frameIndex, level)
                    }) { Text("Speak or type") }
                },
                dismissButton = {
                    TextButton(onClick = { speakMomentOpen = false }) { Text("Skip") }
                }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { reset(MintClipsPlayback.previous(currentIndex, visibleClips.size)) }, enabled = currentIndex > 0) { Text("Previous story") }
                OutlinedButton(onClick = { reset(MintClipsPlayback.next(currentIndex, visibleClips.size)) }, enabled = currentIndex < visibleClips.lastIndex) { Text("Next story") }
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
}
