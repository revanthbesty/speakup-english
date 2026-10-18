package com.speakup.app

/** Pilot grammar review uses stable bookmark IDs and existing legacy mastery IDs. */
internal object MintGrammarRevision {
    fun missedKey(index: Int) = "grammar-pilot-v1-q${index + 1}"
    fun reviewIndices(total: Int, missed: Set<String>, bookmarked: Set<String>, mastered: Set<String>): List<Int> =
        (0 until total).filter { index ->
            val key = missedKey(index)
            key in missed || key in bookmarked || index.toString() !in mastered
        }
    fun masteredCount(total: Int, mastered: Set<String>) =
        (0 until total).count { it.toString() in mastered }
    fun progressPercent(total: Int, mastered: Set<String>): Int =
        if (total <= 0) 0 else masteredCount(total,mastered)*100/total
}
