package com.speakup.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Standalone Games entry view. Activity state and navigation stay with the caller. */
@Composable
internal fun MintGamesHubEntry(
    showFilters: Boolean,
    darkAppearance: Boolean,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    softSurfaceColor: Color,
    onFeatured: () -> Unit,
    onGrammar: () -> Unit,
    onStories: () -> Unit,
    onChooseMode: (String) -> Unit
) {
    Surface(shape = RoundedCornerShape(26.dp), modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer) {
        Row(Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Games Hub", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("Learn English through quick challenges.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Text("🎮", style = MaterialTheme.typography.displayMedium)
        }
    }
    if (!showFilters) {
        Surface(color = softSurfaceColor, shape = RoundedCornerShape(22.dp),
            modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🌟  Today's featured game", color = primaryTextColor,
                    fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Mint Quests", color = primaryTextColor,
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("A quick challenge from your existing game collection.", color = secondaryTextColor)
                Button(onClick = onFeatured, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text("Play featured game →")
                }
            }
        }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf(Triple("📖", "Grammar", "Quick practice"),
            Triple("📚", "Stories", "Watch and read")).forEachIndexed { index, item ->
            Surface(onClick = if (index == 0) onGrammar else onStories,
                shape = RoundedCornerShape(22.dp),
                color = if (darkAppearance) Color(0xFF28354D)
                    else if (index == 0) Color(0xFFF0E7FF) else Color(0xFFE3F5FF),
                modifier = Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text(item.first, style = MaterialTheme.typography.headlineMedium)
                    Text(item.second, fontWeight = FontWeight.Bold, color = primaryTextColor)
                    Text(item.third, style = MaterialTheme.typography.bodySmall, color = secondaryTextColor)
                }
            }
        }
    }
    Text("Pick your next adventure", style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold, color = primaryTextColor)
    listOf(
        listOf(Triple("🧩", "Word Builder", "Build vocabulary"), Triple("💡", "Riddles", "Think and solve")),
        listOf(Triple("🎧", "Listening", "Listen and learn"), Triple("📖", "Reading", "Discover stories"))
    ).forEach { pair ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            pair.forEach { (symbol, mode, description) ->
                Surface(onClick = { onChooseMode(mode) }, shape = RoundedCornerShape(22.dp),
                    color = if (darkAppearance) Color(0xFF26364C) else Color(0xFFE9F7F3),
                    modifier = Modifier.weight(1f).heightIn(min = 110.dp)) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(symbol, style = MaterialTheme.typography.headlineMedium)
                        Text(mode, fontWeight = FontWeight.Bold, color = primaryTextColor)
                        Text(description, style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor)
                    }
                }
            }
        }
    }
}
