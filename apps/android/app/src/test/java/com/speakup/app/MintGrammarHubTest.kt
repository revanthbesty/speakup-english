package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarHubTest {
    @Test fun pilotHasExactlyThreePacksAndStableBookmarks() {
        assertEquals(3,MintGrammarHub.packCount(18))
        assertEquals("grammar-pilot-v1-q1",MintGrammarHub.bookmarkId(0))
        assertEquals("grammar-pilot-v1-q18",MintGrammarHub.bookmarkId(17))
        assertTrue(MintGrammarHub.validBookmark(MintGrammarHub.bookmarkId(17),18))
        assertFalse(MintGrammarHub.validBookmark("grammar-pilot-v1-q19",18))
    }
    @Test fun savedCountExcludesUnrelatedOrStaleIds() {
        assertEquals(1,MintGrammarHub.savedCount(setOf("grammar-pilot-v1-q1","random"),18))
        assertEquals(0,MintGrammarHub.packCount(0))
    }
}
