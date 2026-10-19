package com.speakup.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Renders existing settings only; shared state and preference writes stay in MainActivity. */
@Composable
internal fun MintProfileSettings(
    primaryTextColor: Color, secondaryTextColor: Color,
    softSurfaceColor: Color, surfaceColor: Color,
    onOpenPrivacy: () -> Unit, onOpenMembership: () -> Unit,
    themePreference: String, onThemeChange: (String) -> Unit,
    eyeComfort: Boolean, onEyeComfortChange: (Boolean) -> Unit,
    petSoundsEnabled: Boolean, onPetSoundsEnabledChange: (Boolean) -> Unit,
    reduceMotion: Boolean, onReduceMotionChange: (Boolean) -> Unit,
    onSoundPreview: (MintPetSound.Cue) -> Unit,
    hiddenStoriesCount: Int, localReportsCount: Int, privateReactionsCount: Int,
    onRestoreHiddenStories: () -> Unit,
    showProfileAdvanced: Boolean, onToggleAdvanced: () -> Unit,
    profileName: String, onNameChange: (String) -> Unit,
    learnerAgeBand: String, onAgeBandChange: (String) -> Unit,
    learningGoals: List<String>, learningGoal: String, onLearningGoalChange: (String) -> Unit,
    chosenDifficulty: String, onDifficultyChange: (String) -> Unit,
    learningPaths: List<Pair<String, String>>, chosenPath: String, onPathChange: (String) -> Unit,
    dailyGoal: Int, onDailyGoalChange: (Int) -> Unit,
    profileFeedback: String, onSaveProfile: () -> Unit, onRetakePlacement: () -> Unit
) {
    MintProfileResources(primaryTextColor, secondaryTextColor, softSurfaceColor, surfaceColor,
        onOpenPrivacy, onOpenMembership)
    MintProfileAppearance(primaryTextColor, secondaryTextColor,
        themePreference, onThemeChange, eyeComfort, onEyeComfortChange,
        petSoundsEnabled, onPetSoundsEnabledChange, reduceMotion, onReduceMotionChange,
        onSoundPreview)
    MintProfileSafety(primaryTextColor, secondaryTextColor,
        hiddenStoriesCount, localReportsCount, privateReactionsCount,
        onRestoreHiddenStories, showProfileAdvanced, onToggleAdvanced)
    if (showProfileAdvanced) {
        MintProfileLearnerEditor(primaryTextColor, secondaryTextColor,
            profileName, onNameChange, learnerAgeBand, onAgeBandChange,
            learningGoals, learningGoal, onLearningGoalChange,
            chosenDifficulty, onDifficultyChange,
            learningPaths, chosenPath, onPathChange,
            dailyGoal, onDailyGoalChange,
            profileFeedback, onSaveProfile, onRetakePlacement)
    }
}

@Composable
private fun MintProfileResources(
    primaryTextColor: Color, secondaryTextColor: Color,
    softSurfaceColor: Color, surfaceColor: Color,
    onOpenPrivacy: () -> Unit, onOpenMembership: () -> Unit
) {
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("🛡 Beta readiness", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                Text("Development build only · no production launch authorized.", color = secondaryTextColor)
                                Text("${MintBetaReadiness.required.size} release safeguards must be verified separately. An APK build alone does not mean the app is ready to publish.",
                                    color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Surface(color = surfaceColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(Modifier.padding(14.dp)) {
                                Text("Privacy & local data", color = primaryTextColor, fontWeight = FontWeight.Bold)
                                Text("Export saved learning history or manage local feed interactions.", color = secondaryTextColor)
                                OutlinedButton(onClick = { onOpenPrivacy() }) { Text("Open privacy controls →") }
                            }
                        }
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(Modifier.padding(16.dp)) {
                                Text("English Mint Free", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = primaryTextColor)
                                Text("Core games, speaking and progress stay free.", color = secondaryTextColor)
                                OutlinedButton(onClick = { onOpenMembership() }) { Text("Compare Free, Plus and Pro →") }
                            }
                        }
                        Surface(color = softSurfaceColor, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("📴 Offline & low-data guide", style = MaterialTheme.typography.titleMedium,
                                    color = primaryTextColor, fontWeight = FontWeight.Bold)
                                Text("Text-based learning continues without a connection. This list describes features available in this build, not a downloaded media library.",
                                    color = secondaryTextColor)
                                MintOfflineGuide.features.forEach { item ->
                                    Text("${if (item.offline) "✓ Offline text" else "ⓘ Availability varies"} · ${item.title}",
                                        color = primaryTextColor, fontWeight = FontWeight.SemiBold)
                                    Text(item.fallback, color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                                }
                                Text("Accessibility: use device text-size controls, turn on Reduce animations below, and use typed responses whenever voice isn't available.",
                                    color = secondaryTextColor)
                            }
                        }

}

@Composable
private fun MintProfileAppearance(
    primaryTextColor: Color, secondaryTextColor: Color,
    themePreference: String, onThemeChange: (String) -> Unit,
    eyeComfort: Boolean, onEyeComfortChange: (Boolean) -> Unit,
    petSoundsEnabled: Boolean, onPetSoundsEnabledChange: (Boolean) -> Unit,
    reduceMotion: Boolean, onReduceMotionChange: (Boolean) -> Unit,
    onSoundPreview: (MintPetSound.Cue) -> Unit
) {
                        Text("Appearance", style = MaterialTheme.typography.titleMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("system" to "System", "light" to "Light", "dark" to "Dark", "night" to "Night").forEach { (mode, label) ->
                                FilterChip(
                                    selected = themePreference == mode,
                                    onClick = {
                                        onThemeChange(mode)
                                    },
                                    label = { Text(label) }
                                )
                            }
                        }
                        Text("Scheduled Night: 8 PM–6 AM device time. Light and Dark overrides remain available.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Eye comfort: softer hero colors", color = primaryTextColor, modifier = Modifier.weight(1f))
                            Switch(checked = eyeComfort, onCheckedChange = {
                                onEyeComfortChange(it)
                            })
                        }
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Pet sound effects (optional)", color = primaryTextColor, modifier = Modifier.weight(1f))
                            Switch(checked = petSoundsEnabled, onCheckedChange = {
                                onPetSoundsEnabledChange(it)
                            })
                        }
                        Text("Short offline chimes. Off by default; respects silent mode. Background music is not enabled yet.",
                            color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        if (petSoundsEnabled) {
                            MintSecondaryAction("Preview pet welcome sound", onClick = {
                                onSoundPreview(MintPetSound.Cue.GREETING)
                            })
                            MintSecondaryAction("Preview encouragement sound", onClick = {
                                onSoundPreview(MintPetSound.Cue.ENCOURAGEMENT)
                            })
                            MintSecondaryAction("Preview achievement sound", onClick = {
                                onSoundPreview(MintPetSound.Cue.ACHIEVEMENT)
                            })
                        }
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Reduce animations", color = primaryTextColor, modifier = Modifier.weight(1f))
                            Switch(checked = reduceMotion, onCheckedChange = {
                                onReduceMotionChange(it)
                            })
                        }
                        Text("Respect device brightness, rest breaks and accessibility settings. Dark mode does not prevent eye damage.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)

}

@Composable
private fun MintProfileSafety(
    primaryTextColor: Color, secondaryTextColor: Color,
    hiddenStoriesCount: Int, localReportsCount: Int, privateReactionsCount: Int,
    onRestoreHiddenStories: () -> Unit,
    showProfileAdvanced: Boolean, onToggleAdvanced: () -> Unit
) {
                        Text("Safety and community", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text("Only moderated studio-authored offline reels are available. Public comments, strangers, friend requests, direct messages, uploads and sharing are disabled.", color = secondaryTextColor)
                        Text("Hidden stories: ${hiddenStoriesCount} • Local reports: ${localReportsCount} • Private reactions: ${privateReactionsCount}", color = primaryTextColor)
                        OutlinedButton(onClick = {
                            onRestoreHiddenStories()
                        }) { Text("Restore locally hidden studio stories") }
                        Text("Local reports have not been submitted to moderators; no public social account has been created.", color = secondaryTextColor, style = MaterialTheme.typography.bodySmall)
                        TextButton(onClick = { onToggleAdvanced() }) { Text(if (showProfileAdvanced) "Close learner details" else "Edit learner details") }

}

@Composable
private fun MintProfileLearnerEditor(
    primaryTextColor: Color, secondaryTextColor: Color,
    profileName: String, onNameChange: (String) -> Unit,
    learnerAgeBand: String, onAgeBandChange: (String) -> Unit,
    learningGoals: List<String>, learningGoal: String, onLearningGoalChange: (String) -> Unit,
    chosenDifficulty: String, onDifficultyChange: (String) -> Unit,
    learningPaths: List<Pair<String, String>>, chosenPath: String, onPathChange: (String) -> Unit,
    dailyGoal: Int, onDailyGoalChange: (Int) -> Unit,
    profileFeedback: String, onSaveProfile: () -> Unit, onRetakePlacement: () -> Unit
) {
                        Text("Learner profile", style = MaterialTheme.typography.headlineMedium, color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Text("Update who is learning and why. Your XP, achievements and lesson history will be retained.", color = secondaryTextColor)
                        OutlinedTextField(value = profileName, onValueChange = { onNameChange(it.take(40)) }, label = { Text("Name or nickname") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        Text("Age group", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            MintExperiencePolicy.ageBands.forEach { band ->
                                FilterChip(selected = learnerAgeBand == band, onClick = {
                                    onAgeBandChange(band)
                                }, label = { Text(band) })
                            }
                        }
                        Text("Learning purpose", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        learningGoals.forEach { goal ->
                            FilterChip(selected = learningGoal == goal, onClick = { onLearningGoalChange(goal) }, label = { Text(goal) })
                        }
                        Text("Challenge difficulty", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Easy", "Medium", "Hard").forEach { d ->
                                FilterChip(selected = chosenDifficulty == d, onClick = { onDifficultyChange(d) }, label = { Text(d) })
                            }
                        }
                        Text("Learning pathway", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        learningPaths.forEach { (pathId, pathTitle) ->
                            FilterChip(selected = chosenPath == pathId, onClick = { onPathChange(pathId) }, label = { Text(pathTitle) })
                        }
                        Text("Daily goal", color = primaryTextColor, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(5, 10, 15).forEach { minutes ->
                                FilterChip(selected = dailyGoal == minutes, onClick = { onDailyGoalChange(minutes) }, label = { Text("$minutes min") })
                            }
                        }
                        Button(onClick = onSaveProfile, modifier = Modifier.fillMaxWidth()) { Text("Save profile changes") }
                        if (profileFeedback.isNotBlank()) Text(profileFeedback, color = Color(0xFF178D8D))
                        OutlinedButton(onClick = onRetakePlacement, modifier = Modifier.fillMaxWidth()) { Text("Retake placement assessment") }

}
