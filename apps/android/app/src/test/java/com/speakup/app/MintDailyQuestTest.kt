package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintDailyQuestTest {
    @Test fun dayAndLevelAlwaysSelectSameEligibleQuest() {
        val a = MintDailyQuest.select(MintDifficulty.EASY,4,20_000L,emptySet())
        val b = MintDailyQuest.select(MintDifficulty.EASY,4,20_000L,emptySet())
        assertNotNull(a)
        assertEquals(a?.id,b?.id)
        assertEquals(MintDifficulty.EASY,a?.difficulty)
        assertTrue((a?.minAge ?: 100) <= 4)
    }
    @Test fun prefersUnmasteredWithoutResettingAnyRewards() {
        val eligible = MintGameCatalog.listFor(MintDifficulty.EASY, 18)
        val mastered = eligible.dropLast(1).map { "mint_game_${it.id}_v${it.version}" }.toSet()
        val recommended = MintDailyQuest.select(MintDifficulty.EASY,18,20000L,mastered)
        assertNotNull(recommended)
        assertFalse("mint_game_${recommended!!.id}_v${recommended.version}" in mastered)
        val fullyMastered = eligible.map { "mint_game_${it.id}_v${it.version}" }.toSet()
        assertNotNull(MintDailyQuest.select(MintDifficulty.EASY,18,20000L,fullyMastered))
    }
    @Test fun ageGateDoesNotFallbackToUnsafeItem() {
        assertNull(MintDailyQuest.select(MintDifficulty.HARD,4,20000L,emptySet()))
        assertNull(MintDailyQuest.select(MintDifficulty.MEDIUM,4,20000L,emptySet()))
    }
}
