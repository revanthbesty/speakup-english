package com.speakup.app

import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class MintPetReactionCopyTest {
    @Test fun everyPetHasDistinctPositiveAndEncouragingFeedback() {
        val correct = MintPetCatalog.pets.map { MintPetReactionCopy.message(true, it.id) }
        val retry = MintPetCatalog.pets.map { MintPetReactionCopy.message(false, it.id) }
        assertEquals(6, correct.distinct().size)
        assertEquals(6, retry.distinct().size)
        assertTrue(correct.all { it.isNotBlank() })
        assertTrue(retry.all { it.isNotBlank() })
    }

    @Test fun unknownPetDoesNotInventReactions() {
        assertEquals("", MintPetReactionCopy.message(true, "unknown"))
        assertEquals("", MintPetReactionCopy.message(false, ""))
    }
}
