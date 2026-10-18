package com.speakup.app

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Local-data controls; no claim of account deletion or cloud deletion without a
 * connected authenticated account backend.
 */
@Composable
internal fun MintPrivacyScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember(context) {
        context.getSharedPreferences(MintIdentity.legacyPreferencesFile, Context.MODE_PRIVATE)
    }
    var feedback by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showExportDisclosure by remember { mutableStateOf(false) }
    var recordsVersion by remember { mutableIntStateOf(0) }
    val checkedData = remember(recordsVersion) { MintDataProtection.safeSnapshot(prefs.all) }
    val warnings = remember(recordsVersion) { MintDataProtection.audit(prefs.all) }

    val createExport = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri == null) {
            feedback = "Export cancelled; your learning records remain unchanged."
        } else {
            try {
                val snapshot = MintDataProtection.safeSnapshot(prefs.all)
                context.contentResolver.openOutputStream(uri, "w")?.use { stream ->
                    stream.write(MintDataProtection.encodeJson(snapshot).toByteArray(Charsets.UTF_8))
                } ?: error("Unable to open destination")
                feedback = "Export saved to the location you selected. Keep this personal data file private."
            } catch (_: Exception) {
                feedback = "Export could not be saved. No learning records were changed."
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Privacy and Data", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Your local learning history belongs to you. No cloud account or remote deletion is connected in this version.")
        Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainer) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Local learning records", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("${checkedData.size} supported settings and progress entries are available for export.")
                Text("Includes XP, lesson progress, quest and speaking participation, nickname, level choices and private feed settings. No raw microphone audio is kept by this app's practice flow.")
                if (warnings.isNotEmpty()) {
                    warnings.forEach { warning ->
                        Text("Data review needed: $warning", color = MaterialTheme.colorScheme.error)
                    }
                }
                Button(onClick = { showExportDisclosure = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Export my local learning data")
                }
            }
        }
        Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceContainer) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Private feed controls", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Clear private reactions, locally hidden stories, local report drafts and story completion marks. Lessons, speaking progress, badges and XP are not changed.")
                OutlinedButton(onClick = { showDeleteConfirmation = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Clear private feed interactions")
                }
            }
        }
        Text("Account deletion and cloud data export require authenticated server integration and identity verification. This screen does not pretend to delete an online account.",
            style = MaterialTheme.typography.bodySmall)
        if (feedback.isNotBlank()) {
            Text(feedback, color = MaterialTheme.colorScheme.primary)
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Return to Profile") }
    }

    if (showExportDisclosure) AlertDialog(
        onDismissRequest = { showExportDisclosure = false },
        title = { Text("Export personal learning data?") },
        text = { Text("The JSON file can include your nickname and learning activity. Choose a private destination you trust. This makes a copy; it does not sync or delete your data.") },
        confirmButton = {
            TextButton(onClick = {
                showExportDisclosure = false
                createExport.launch("english-mint-learning-export.json")
            }) { Text("Choose destination") }
        },
        dismissButton = { TextButton(onClick = { showExportDisclosure = false }) { Text("Cancel") } }
    )
    if (showDeleteConfirmation) AlertDialog(
        onDismissRequest = { showDeleteConfirmation = false },
        title = { Text("Clear private feed interactions?") },
        text = { Text("Only reactions, hidden-story choices, local report drafts and story challenge marks will be cleared. This action cannot be undone. Your XP, badges, lessons and speaking progress will remain.") },
        confirmButton = {
            TextButton(onClick = {
                val editor = prefs.edit()
                MintDataProtection.privateInteractionKeys.forEach(editor::remove)
                if (editor.commit()) {
                    recordsVersion += 1
                    feedback = "Private feed interactions cleared. Other learning progress was preserved."
                } else {
                    feedback = "Unable to clear interactions. Please try again."
                }
                showDeleteConfirmation = false
            }) { Text("Clear selected data") }
        },
        dismissButton = { TextButton(onClick = { showDeleteConfirmation = false }) { Text("Keep my data") } }
    )
}
