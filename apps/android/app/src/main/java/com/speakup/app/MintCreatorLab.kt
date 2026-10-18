package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Visible local-only editorial demonstration. Nothing is uploaded or published. */
@Composable
internal fun MintCreatorLab(onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var script by remember { mutableStateOf("") }
    var rightsNote by remember { mutableStateOf("") }
    var savedDraft by remember { mutableStateOf<MintCreatorDraft?>(null) }
    var confirmRights by remember { mutableStateOf(false) }
    var confirmLanguage by remember { mutableStateOf(false) }
    var confirmSafety by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("✍️ Creator Content Lab", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Private editorial prototype. No content can be posted publicly, scheduled, uploaded or sent to other learners.")
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Content title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = script, onValueChange = { script = it }, label = { Text("Original scenario or dialogue") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        OutlinedTextField(value = rightsNote, onValueChange = { rightsNote = it }, label = { Text("Rights / original author note") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            savedDraft = MintCreatorDraft("local-preview",title.trim(),script.trim(),rightsNote.trim(),MintCreatorStatus.DRAFT)
        }, enabled = MintCreatorPolicy.valid(MintCreatorDraft("local-preview",title.trim(),script.trim(),rightsNote.trim(),MintCreatorStatus.DRAFT)), modifier = Modifier.fillMaxWidth()) { Text("Preview draft privately") }
        savedDraft?.let { draft ->
            Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(draft.title, fontWeight = FontWeight.Bold)
                    Text(draft.script)
                    Text("Editorial state: ${draft.status.name.replace('_',' ')}")
                    when (draft.status) {
                        MintCreatorStatus.DRAFT -> {
                            Row { Checkbox(checked = confirmRights, onCheckedChange = { confirmRights = it }); Text("I confirm rights and original authorship") }
                        }
                        MintCreatorStatus.RIGHTS_CHECKED -> {
                            Row { Checkbox(checked = confirmLanguage, onCheckedChange = { confirmLanguage = it }); Text("Language reviewer has checked this draft (local demonstration)") }
                        }
                        MintCreatorStatus.LANGUAGE_REVIEWED -> {
                            Row { Checkbox(checked = confirmSafety, onCheckedChange = { confirmSafety = it }); Text("Safety reviewer has checked this draft (local demonstration)") }
                        }
                        else -> {}
                    }
                    val candidate = MintCreatorPolicy.next(draft,confirmRights,confirmLanguage,confirmSafety)
                    if (candidate != null) Button(onClick = { savedDraft = candidate }, modifier = Modifier.fillMaxWidth()) { Text("Advance editorial preview") }
                    Text("Public publishing is disabled. Demonstration checkboxes are not verified staff approvals.")
                }
            }
        }
        TextButton(onClick = onBack) { Text("Back to Home") }
    }
}
