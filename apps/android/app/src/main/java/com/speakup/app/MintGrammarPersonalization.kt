package com.speakup.app

/** Offline, transparent practice recommendations. No probabilistic mastery claims or paid AI. */
internal object MintGrammarPersonalization {
    fun suggestedIndices(total: Int, missed: Set<String>, bookmarks: Set<String>, mastered: Set<String>): List<Int> {
        if (total <= 0) return emptyList()
        val indices = (0 until total).toList()
        return indices.sortedWith(compareBy<Int> {
            val key = MintGrammarRevision.missedKey(it)
            when {
                key in missed -> 0
                key in bookmarks -> 1
                it.toString() !in mastered -> 2
                else -> 3
            }
        }.thenBy { it })
    }
    fun reason(index: Int, missed: Set<String>, bookmarks: Set<String>, mastered: Set<String>): String {
        val key = MintGrammarRevision.missedKey(index)
        return when {
            key in missed -> "You missed this question earlier. Try again."
            key in bookmarks -> "You saved this question for review."
            index.toString() !in mastered -> "You haven't mastered this question yet."
            else -> "Refresh a previously mastered question."
        }
    }
}
