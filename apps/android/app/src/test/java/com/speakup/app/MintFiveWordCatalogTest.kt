package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintFiveWordCatalogTest {
    @Test fun allPublishedPacksHaveExactlyFiveUniqueWordsAndFourToEightScenes() {
        assertTrue(MintFiveWordCatalog.packs.isNotEmpty())
        assertEquals(MintFiveWordCatalog.packs.size, MintFiveWordCatalog.packs.map { it.id }.distinct().size)
        MintFiveWordCatalog.packs.forEach { assertTrue("${it.id} is invalid", it.valid()) }
    }
    @Test fun brokenFiveWordContractsAreRejected() {
        val p = MintFiveWordCatalog.packs.first()
        assertFalse(p.copy(words = p.words.take(4)).valid())
        assertFalse(p.copy(words = listOf("same", "same", "rain", "cat", "hat")).valid())
        assertFalse(p.copy(scenes = p.scenes.take(3)).valid())
        assertFalse(p.copy(correctAnswer = "Not listed").valid())
    }
    @Test fun noIneligibleContentIsExposed() {
        assertTrue(MintFiveWordCatalog.visible(3).isEmpty())
        assertTrue(MintFiveWordCatalog.visible(8).all { it.minAge <= 8 })
    }
}
