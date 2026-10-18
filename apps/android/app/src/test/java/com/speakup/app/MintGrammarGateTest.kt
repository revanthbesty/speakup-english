package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarGateTest {
    private fun item(status: MintGrammarStatus = MintGrammarStatus.DRAFT) = MintGrammarDraft(
        id = "EM-GR-TEST-01", version = 1, topicId = "GRAMMAR-09",
        prompt = "They ___ their work.", options = listOf("finishes", "have finished", "finishing"),
        correctKey = 'B', feedback = listOf("Incorrect subject agreement.", "Present perfect is correct.", "Missing auxiliary verb."),
        grammarRule = "Present perfect uses have plus a past participle.",
        exampleSentence = "We have visited the museum.", status = status,
        reviewerId = "reviewer", approvedAt = "2026-10-18", rightsReviewed = true
    )

    @Test fun draftsCannotBeLoadedOrScored() {
        assertFalse(MintGrammarGate.eligible(item()))
        assertNull(MintGrammarGate.feedback(item(), 'A'))
        assertTrue(MintGrammarGate.publishableBatch(listOf(item())).isEmpty())
    }

    @Test fun approvedQuestionsShowFeedbackForCorrectAndIncorrectChoice() {
        val approved = item(MintGrammarStatus.APPROVED)
        val wrong = MintGrammarGate.feedback(approved, 'A')!!
        assertFalse(wrong.isCorrect)
        assertEquals("Incorrect subject agreement.", wrong.selectedExplanation)
        assertEquals("Present perfect is correct.", wrong.correctExplanation)
        val right = MintGrammarGate.feedback(approved, 'B')!!
        assertTrue(right.isCorrect)
        assertEquals(right.selectedExplanation, right.correctExplanation)
    }

    @Test fun incompleteApprovalEvidenceBlocksExport() {
        assertFalse(MintGrammarGate.eligible(item(MintGrammarStatus.APPROVED).copy(reviewerId = null)))
        assertFalse(MintGrammarGate.eligible(item(MintGrammarStatus.APPROVED).copy(rightsReviewed = false)))
    }

    @Test fun duplicateIdsAndEmptyBatchesAreRejected() {
        val approved = item(MintGrammarStatus.APPROVED)
        assertTrue(MintGrammarGate.publishableBatch(emptyList()).isEmpty())
        assertTrue(MintGrammarGate.publishableBatch(listOf(approved, approved)).isEmpty())
    }
}
