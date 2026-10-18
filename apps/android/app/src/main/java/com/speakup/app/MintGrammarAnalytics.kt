package com.speakup.app

/** Transparent snapshot of locally saved pilot state, not proficiency or attempt analytics. */
internal data class MintGrammarAnalyticsSnapshot(
    val total: Int, val mastered: Int, val bookmarked: Int, val flaggedForRevision: Int,
    val notYetMastered: Int, val masteredPercent: Int
)
internal object MintGrammarAnalytics {
    fun snapshot(total: Int, mastered: Set<String>, bookmarks: Set<String>, missed: Set<String>): MintGrammarAnalyticsSnapshot {
        val size=total.coerceAtLeast(0)
        val indices=0 until size
        val done=indices.count { it.toString() in mastered }
        val saved=indices.count { MintGrammarHub.bookmarkId(it) in bookmarks }
        val flagged=indices.count { MintGrammarRevision.missedKey(it) in missed }
        return MintGrammarAnalyticsSnapshot(size,done,saved,flagged,size-done,if(size==0) 0 else 100*done/size)
    }
}
