package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarFormatsTest {
    @Test fun oneOriginalPilotPerFormatWithDefensibleKey() {
        assertEquals(MintGrammarFormat.entries.toSet(), MintGrammarFormats.samples.map { it.format }.toSet())
        assertTrue(MintGrammarFormats.samples.all(MintGrammarFormats::valid))
        assertEquals(3,MintGrammarFormats.samples.map { it.id }.distinct().size)
        for (item in MintGrammarFormats.samples) {
            assertTrue(MintGrammarFormats.checked(item,item.correct))
            assertFalse(MintGrammarFormats.checked(item,(item.correct+1)%3))
        }
    }
}
