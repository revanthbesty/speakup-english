package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintEditorialPolicyTest {
    private val good = MintEditorialItem("market-story", "Market Story", "A learner visits the market.",
        8, MintEditorialStatus.SCHEDULED, MintRightsRecord("English Mint Studio", "Original work", "English Mint Studio"),
        "Editorial reviewer", "2026-10-18", 1)

    @Test fun publicationRequiresApprovalProvenanceDateAndReviewer() {
        assertTrue(MintEditorialPolicy.mayPublish(good, "2026-10-18", listOf(good)))
        assertFalse(MintEditorialPolicy.mayPublish(good.copy(rights = null), "2026-10-18", emptyList()))
        assertFalse(MintEditorialPolicy.mayPublish(good.copy(reviewer = null), "2026-10-18", emptyList()))
        assertFalse(MintEditorialPolicy.mayPublish(good.copy(status = MintEditorialStatus.DRAFT), "2026-10-18", emptyList()))
        assertFalse(MintEditorialPolicy.mayPublish(good, "2026-10-17", emptyList()))
    }
    @Test fun rejectsDuplicatedIdsAndContent() {
        assertFalse(MintEditorialPolicy.mayPublish(good, "2026-10-18", listOf(good,good.copy(title="Copy"))))
        assertFalse(MintEditorialPolicy.mayPublish(good, "2026-10-18", listOf(good,good.copy(id="copy"))))
    }
    @Test fun transitionGraphIsExplicit() {
        assertTrue(MintEditorialPolicy.canTransition(MintEditorialStatus.DRAFT, MintEditorialStatus.REVIEWED))
        assertTrue(MintEditorialPolicy.canTransition(MintEditorialStatus.PUBLISHED, MintEditorialStatus.ARCHIVED))
        assertFalse(MintEditorialPolicy.canTransition(MintEditorialStatus.DRAFT, MintEditorialStatus.PUBLISHED))
        assertFalse(MintEditorialPolicy.canTransition(MintEditorialStatus.ARCHIVED, MintEditorialStatus.PUBLISHED))
    }
}
