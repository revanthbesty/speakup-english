package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintClipRecommendationsTest {
    @Test fun finiteRecommendationsFavorUncompletedAndDeduplicate() {
        val catalog = MintFeedCatalog.clips.filter { it.level == MintClipLevel.EASY }
        val first = catalog.first()
        val ranked = MintClipRecommendations.ordered(catalog, setOf(first.id), emptySet())
        assertEquals(first.id, ranked.last().id)
        val selected = MintClipRecommendations.dailySelection(catalog, setOf(first.id), emptySet(), 5)
        assertTrue(selected.size <= 5)
        assertEquals(selected.size, selected.map { it.id }.toSet().size)
        assertTrue(selected.all { it in catalog })
        assertTrue(MintClipRecommendations.dailySelection(catalog, emptySet(), emptySet(), 0).isEmpty())
    }
    @Test fun vocabularyTogglesAreStableAndOffline() {
        val added = MintSavedVocabulary.toggle(emptySet(), "  Basket ")
        assertEquals(setOf("basket"), added)
        assertTrue(MintSavedVocabulary.toggle(added, "basket").isEmpty())
        assertEquals(added, MintSavedVocabulary.toggle(added, "!!!"))
    }
}
