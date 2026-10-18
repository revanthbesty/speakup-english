package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Read-only editorial preview. Never writes progress, grades, or rewards. */
@Composable
internal fun MintDraftLibraryPreview() {
    val context = LocalContext.current
    var opened by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("word_builder") }
    var index by remember { mutableIntStateOf(0) }
    var failure by remember { mutableStateOf(false) }
    var rows by remember { mutableStateOf<List<MintDraftRecord>>(emptyList()) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Editorial content preview · Not approved", fontWeight = FontWeight.Bold)
        Text("288 offline replacement drafts, not assigned lessons. No progress or rewards.")
        if (!opened) OutlinedButton(onClick = {
            try {
                rows = MintDraftLibrary.read(context)
                failure = false
                opened = true
            } catch (_: Exception) {
                rows = emptyList()
                failure = true
            }
        }) { Text("Inspect 288 draft records") }
        if (failure) Text("Draft pack failed validation.", color = MaterialTheme.colorScheme.error)
        if (opened) {
            val subset = rows.filter { it.category == category }
            Text("Loaded 288 drafts · " + subset.size + " in this category")
            MintDraftLibrary.categories.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pair.forEach { name ->
                        OutlinedButton(
                            onClick = { category = name; index = 0 },
                            modifier = Modifier.weight(1f),
                            enabled = name != category
                        ) { Text(name.replace('_', ' ')) }
                    }
                }
            }
            if (subset.isNotEmpty()) {
                val item = subset[index.coerceIn(subset.indices)]
                Text("Draft " + (index + 1) + " of " + subset.size + " · " +
                    item.level + " · " + item.id, fontWeight = FontWeight.SemiBold)
                Text(item.content)
                Text(item.question)
                Text("Explanation (review only): " + item.explanation)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { index = (index - 1).coerceAtLeast(0) },
                        enabled = index > 0) { Text("Previous") }
                    OutlinedButton(onClick = { index = (index + 1).coerceAtMost(subset.lastIndex) },
                        enabled = index < subset.lastIndex) { Text("Next draft") }
                }
            }
            Text("Not available as teacher assignments. No completion, progress or Blooms.")
        }
    }
}
