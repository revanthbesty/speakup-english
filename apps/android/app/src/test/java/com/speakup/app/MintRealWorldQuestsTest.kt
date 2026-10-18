package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintRealWorldQuestsTest {
    @Test fun twoDistinctSafeScenarios() {
        assertEquals(2, MintRealWorldQuests.scenarios.size)
        assertEquals(2, MintRealWorldQuests.scenarios.map { it.id }.distinct().size)
        assertTrue(MintRealWorldQuests.scenarios.all(MintRealWorldQuests::valid))
    }
    @Test fun skipNeverCountsAsVoiceAndAnswerIsRequired() {
        assertFalse(MintRealWorldQuests.canFinish(false, "skipped", ""))
        assertTrue(MintRealWorldQuests.canFinish(true, "skipped", ""))
        assertFalse(MintRealWorldQuests.canFinish(true, "voice", ""))
        assertTrue(MintRealWorldQuests.canFinish(true, "typed", "Thank you"))
        assertFalse(MintRealWorldQuests.canFinish(true, "typed", "  "))
        assertFalse(MintRealWorldQuests.canFinish(true, "none", ""))
    }
    @Test fun rewardKeysAreStableAcrossReplays() {
        val s=MintRealWorldQuests.scenarios.first()
        assertEquals(MintRealWorldQuests.rewardKey(s), MintRealWorldQuests.rewardKey(s))
    }
}