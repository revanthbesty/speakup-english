package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarRevisionTest {
    @Test fun reviewQueueCombinesMistakesBookmarksAndUnmastered() {
        val result=MintGrammarRevision.reviewIndices(4,setOf("grammar-pilot-v1-q1"),setOf("grammar-pilot-v1-q2"),setOf("0","1","2"))
        assertEquals(listOf(0,1,3),result)
    }
    @Test fun progressDoesNotCountStaleOrUnrelatedEntries() {
        assertEquals(50,MintGrammarRevision.progressPercent(4,setOf("0","2","99")))
        assertEquals(0,MintGrammarRevision.progressPercent(0,emptySet()))
        assertEquals(0,MintGrammarRevision.masteredCount(2,setOf("9")))
    }
}
