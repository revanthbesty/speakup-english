package com.speakup.app

/** Deterministic local recommendations; no network tracking or inferred private attributes. */
internal object MintClipRecommendations {
    fun ordered(clips: List<MintClip>, completedIds: Set<String>, reactedIds: Set<String>): List<MintClip> =
        clips.sortedWith(compareBy<MintClip> { it.id in completedIds }
            .thenByDescending { it.id in reactedIds }
            .thenBy { it.theme.name }
            .thenBy { it.id })

    fun dailySelection(clips: List<MintClip>, completedIds: Set<String>, reactedIds: Set<String>, limit: Int = 5): List<MintClip> {
        val ranked = ordered(clips, completedIds, reactedIds)
        if (limit <= 0) return emptyList()
        val chosen = mutableListOf<MintClip>()
        val remaining = ranked.toMutableList()
        while (chosen.size < limit && remaining.isNotEmpty()) {
            val seenThemes = chosen.map { it.theme }.toSet()
            val firstNewTheme = remaining.indexOfFirst { it.theme !in seenThemes }
            val pickedAt = if (firstNewTheme >= 0) firstNewTheme else 0
            chosen.add(remaining.removeAt(pickedAt))
        }
        return chosen
    }
}

/** Offline vocabulary bookmark identifiers, not a paid AI or dictionary. */
internal object MintSavedVocabulary {
    fun normalized(word: String): String? {
        val clean = word.trim().lowercase()
        return clean.takeIf { it.length in 2..40 && it.matches(Regex("[a-z]+")) }
    }
    fun toggle(saved: Set<String>, word: String): Set<String> {
        val clean = normalized(word) ?: return saved
        return if (clean in saved) saved - clean else saved + clean
    }
}
