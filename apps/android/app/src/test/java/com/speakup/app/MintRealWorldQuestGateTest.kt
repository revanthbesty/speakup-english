package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintRealWorldQuestGateTest {
    private fun sample(id: Int = 1, difficulty: String = "EASY", status: String = "draft") =
        MintRealWorldQuestRecord(
            "RWQ-" + id.toString().padStart(4, '0'), "CAT-01", difficulty, "Ordering politely",
            "What should you say?", listOf("Please help me", "Do this now", "Give that"),
            "A", "Say the polite request aloud", status
        )

    @Test fun draftIsValidButNeverPublishable() {
        assertTrue(MintRealWorldQuestGate.valid(sample()))
        assertFalse(MintRealWorldQuestGate.publishable(sample()))
        assertTrue(MintRealWorldQuestGate.publishable(sample(status = "approved")))
    }

    @Test fun rejectsMalformedRecords() {
        assertFalse(MintRealWorldQuestGate.valid(sample().copy(correctAnswer = "D")))
        assertFalse(MintRealWorldQuestGate.valid(sample().copy(choices = listOf("Only one"))))
        assertFalse(MintRealWorldQuestGate.valid(sample().copy(status = "unknown")))
        assertFalse(MintRealWorldQuestGate.valid(sample().copy(id = "bad")))
    }

    @Test fun requiresFullUniqueDifficultyDistribution() {
        val good = (1..100).map {
            sample(it, when { it <= 40 -> "EASY"; it <= 75 -> "MEDIUM"; else -> "HARD" })
        }
        assertTrue(MintRealWorldQuestGate.readyForImport(good))
        assertFalse(MintRealWorldQuestGate.readyForImport(good.dropLast(1)))
        assertFalse(MintRealWorldQuestGate.readyForImport(good.dropLast(1) + good.first()))
    }
}
