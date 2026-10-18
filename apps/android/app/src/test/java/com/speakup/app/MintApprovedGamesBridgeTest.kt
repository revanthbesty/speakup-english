package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintApprovedGamesBridgeTest {
    private fun candidate(id: String = "WB-A1-0001", category: String = "word_builder") =
        MintDraftRecord(id, category, "A1", "Choose an answer", "A sentence", "Because")

    @Test fun allDraftsRemainUnplayableAndCannotAwardRewards() {
        assertTrue(MintApprovedGamesBridge.approvedReleaseIds().isEmpty())
        assertTrue(MintApprovedGamesBridge.playableRecords(listOf(candidate())).isEmpty())
        assertFalse(MintApprovedGamesBridge.mayAwardProgress("WB-A1-0001"))
    }

    @Test fun onlyExplicitTrustedApprovalAllowsStaging() {
        val first = candidate()
        val second = candidate("REA-B1-0232", "reading")
        val selected = MintApprovedGamesBridge.playableRecords(
            listOf(first, second), setOf("REA-B1-0232"))
        assertEquals(listOf(second), selected)
        assertFalse(MintApprovedGamesBridge.mayAwardProgress(second.id))
    }

    @Test fun malformedOrDuplicateIdsFailClosed() {
        assertTrue(MintApprovedGamesBridge.playableRecords(
            listOf(candidate("", "reading")), setOf("")).isEmpty())
        try {
            MintApprovedGamesBridge.playableRecords(
                listOf(candidate(), candidate()), setOf("WB-A1-0001"))
            fail("Expected duplicate rejection")
        } catch (_: IllegalArgumentException) { }
    }

    @Test fun unknownGameCategoriesAreNotPlayable() {
        assertTrue(MintApprovedGamesBridge.playableRecords(
            listOf(candidate("ZZ-A1-0001", "unknown")), setOf("ZZ-A1-0001")).isEmpty())
    }
}
