package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintRewardUniverseTest {
    @Test fun migrationReadsRealLedgerEntriesWithoutAwardingNewRewards() {
        val garden = MintRewardUniverse.fromLegacy(
            setOf("0"), setOf("0", "1"), emptySet(), setOf("2"),
            setOf("3"), setOf("4"), emptySet(), emptySet(), setOf("quest:one"))
        assertEquals(3, garden.blooms)
        assertEquals(1, garden.words)
        assertEquals(1, garden.stories)
        assertEquals(1, garden.sounds)
        assertEquals(0, garden.voices)
        assertEquals(1, garden.quests)
        assertEquals(7, garden.total)
    }
    @Test fun replaysDoNotBecomeNewCollectibles() {
        assertFalse(MintRewardUniverse.isNewReward(0))
        assertTrue(MintRewardUniverse.isNewReward(10))
        assertEquals(MintCollectible.WORD, MintRewardUniverse.displayName("Vocabulary"))
    }
}
