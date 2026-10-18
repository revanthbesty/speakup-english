package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarContentPolicyTest {
    private val q = MintGrammarEntry(
        id="EM-GR-001", version=1, topicId="articles", subtopicId="indefinite",
        tier=MintGrammarTier.EASY, prompt="He bought ___ umbrella.",
        options=mapOf("A" to "a", "B" to "an", "C" to "the"), correctKey="B",
        explanation="Use an before a vowel sound.", learningOutcome="Choose articles",
        ageSegment="8+", format="text", locale="en", author="Studio",
        reviewerId="independent-reviewer", rightsStatus="cleared",
        approval=MintGrammarApproval.APPROVED, approvedAt="2026-10-18",
        duplicateHash="verified-content-001")
    @Test fun correctAllocationAndExactlyThreeUniqueOptions() {
        assertEquals(10000, MintGrammarContentPolicy.intendedTotal)
        assertEquals(10000, MintGrammarContentPolicy.tiers.values.sum())
        assertTrue(MintGrammarContentPolicy.publishable(q))
        assertFalse(MintGrammarContentPolicy.structurallyValid(q.copy(options=mapOf("A" to "a", "B" to "an"))))
        assertFalse(MintGrammarContentPolicy.structurallyValid(q.copy(options=mapOf("A" to "a", "B" to "a", "C" to "the"))))
        assertFalse(MintGrammarContentPolicy.structurallyValid(q.copy(correctKey="D")))
    }
    @Test fun reviewedRightsAndEditorialApprovalAreMandatory() {
        assertFalse(MintGrammarContentPolicy.publishable(q.copy(approval=MintGrammarApproval.REVIEW)))
        assertFalse(MintGrammarContentPolicy.publishable(q.copy(reviewerId=null)))
        assertFalse(MintGrammarContentPolicy.publishable(q.copy(rightsStatus="unknown")))
        assertFalse(MintGrammarContentPolicy.validateBank(listOf(q,q.copy(id="EM-GR-002"))))
    }
    @Test fun accessibilityRequiredForVisualAndAudioContent() {
        assertFalse(MintGrammarContentPolicy.structurallyValid(q.copy(format="visual")))
        assertTrue(MintGrammarContentPolicy.structurallyValid(q.copy(format="visual",altText="Person carrying an umbrella")))
        assertFalse(MintGrammarContentPolicy.structurallyValid(q.copy(format="audio")))
        assertTrue(MintGrammarContentPolicy.structurallyValid(q.copy(format="audio",transcript="He bought an umbrella.")))
    }
}
