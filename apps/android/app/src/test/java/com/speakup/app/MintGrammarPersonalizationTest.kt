package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarPersonalizationTest {
    @Test fun prioritizesMistakesThenSavedThenUnmasteredThenMastered() {
        val ordered=MintGrammarPersonalization.suggestedIndices(
            5,setOf("grammar-pilot-v1-q3"),setOf("grammar-pilot-v1-q2"),setOf("0","1","2","4"))
        assertEquals(listOf(2,1,3,0,4),ordered)
    }
    @Test fun handlesEmptyBankAndExplainsRecommendation() {
        assertTrue(MintGrammarPersonalization.suggestedIndices(0,emptySet(),emptySet(),emptySet()).isEmpty())
        assertTrue(MintGrammarPersonalization.reason(0,setOf("grammar-pilot-v1-q1"),emptySet(),emptySet()).contains("missed"))
    }
}
