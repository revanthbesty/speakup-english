package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MintFamilyContentGateTest {
    private fun item(
        id: String = "WB-A1-0001",
        category: String = "word_builder",
        level: String = "A1",
        approved: Boolean = false,
        sourceVerified: Boolean = false
    ) = MintReviewableActivity(id, category, level, approved, sourceVerified)

    @Test fun draftsAreNeverAssignable() {
        assertFalse(MintFamilyContentGate.isCandidateAvailableForLocalAssignment(item()))
        assertFalse(MintFamilyContentGate.isCandidateAvailableForLocalAssignment(item(approved = true)))
        assertFalse(MintFamilyContentGate.isCandidateAvailableForLocalAssignment(item(sourceVerified = true)))
    }

    @Test fun onlyVerifiedReviewedSupportedCategoriesCanBeStaged() {
        assertTrue(MintFamilyContentGate.isCandidateAvailableForLocalAssignment(item(approved = true, sourceVerified = true)))
        assertFalse(MintFamilyContentGate.isCandidateAvailableForLocalAssignment(item(id = "", approved = true, sourceVerified = true)))
        assertFalse(MintFamilyContentGate.isCandidateAvailableForLocalAssignment(item(category = "listening", approved = true, sourceVerified = true)))
        assertFalse(MintFamilyContentGate.isCandidateAvailableForLocalAssignment(item(level = "C2", approved = true, sourceVerified = true)))
    }

    @Test fun previewIsEmptyUntilApprovedOfflinePackExists() {
        assertEquals(emptyList<MintReviewableActivity>(), MintFamilyContentGate.shippedContentCandidates())
        assertEquals(emptyList<String>(), MintFamilyContentGate.candidateIdsForLocalPreview(listOf(item())))
    }

    @Test fun approvedCandidateIdsAreDeduplicated() {
        val approved = item(approved = true, sourceVerified = true)
        assertEquals(listOf("WB-A1-0001"),
            MintFamilyContentGate.candidateIdsForLocalPreview(listOf(approved, approved, item())))
    }
}
