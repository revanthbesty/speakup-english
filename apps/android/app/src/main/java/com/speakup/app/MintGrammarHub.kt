package com.speakup.app

/** Sprint 31: stable bookmark keys for the existing 18-question offline pilot. */
internal object MintGrammarHub {
    const val packSize = 6
    fun packCount(total: Int) = if (total <= 0) 0 else (total + packSize - 1) / packSize
    fun bookmarkId(questionIndex: Int): String = "grammar-pilot-v1-q${questionIndex + 1}"
    fun validBookmark(id: String, total: Int): Boolean =
        (0 until total).any { bookmarkId(it) == id }
    fun savedCount(saved: Set<String>, total: Int): Int =
        saved.count { validBookmark(it, total) }
    fun packTitle(pack: Int): String = "Practice Pack ${pack + 1}"
}
