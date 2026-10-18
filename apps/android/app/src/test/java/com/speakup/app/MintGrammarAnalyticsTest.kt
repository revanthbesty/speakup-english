package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarAnalyticsTest {
    @Test fun countsOnlyValidLocalPilotQuestionIds() {
        val data=MintGrammarAnalytics.snapshot(
            4,setOf("0","2","99","invalid"),setOf("grammar-pilot-v1-q1","grammar-pilot-v1-q9"),
            setOf("grammar-pilot-v1-q3","not-a-key"))
        assertEquals(2,data.mastered)
        assertEquals(1,data.bookmarked)
        assertEquals(1,data.flaggedForRevision)
        assertEquals(2,data.notYetMastered)
        assertEquals(50,data.masteredPercent)
    }
    @Test fun emptyDataIsSafeAndDoesNotInventScores() {
        val data=MintGrammarAnalytics.snapshot(0,emptySet(),emptySet(),emptySet())
        assertEquals(0,data.total)
        assertEquals(0,data.masteredPercent)
        assertEquals(0,data.flaggedForRevision)
    }
}
