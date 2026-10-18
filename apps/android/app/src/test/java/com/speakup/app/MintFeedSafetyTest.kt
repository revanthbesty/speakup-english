package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintFeedSafetyTest {
    @Test fun allCuratedStoriesAreUniqueCompleteAndStudioAuthored() {
        assertEquals(12, MintFeedCatalog.clips.size)
        assertTrue(MintFeedCatalog.validCatalog())
        assertTrue(MintFeedCatalog.clips.all { it.frames.size == 3 })
        assertTrue(MintFeedCatalog.clips.all { it.creator == "English Mint Studio" })
    }

    @Test fun ageAndLevelFilterIsStrictIncludingForAdultsWhoPickEasy() {
        val tiny = MintFeedCatalog.visible(4, MintClipLevel.EASY, emptySet())
        assertEquals(3, tiny.size)
        assertTrue(tiny.all { it.minAge <= 4 })
        assertTrue(MintFeedCatalog.visible(4, MintClipLevel.HARD, emptySet()).isEmpty())
        assertEquals(4, MintFeedCatalog.visible(18, MintClipLevel.HARD, emptySet()).size)
        assertEquals(4, MintFeedCatalog.visible(18, MintClipLevel.MEDIUM, emptySet()).size)
    }

    @Test fun blockingHidesOnlyTargetClipAndCanBeUndoneWithoutRemoteEffects() {
        val original = MintFeedCatalog.visible(18, MintClipLevel.EASY, emptySet())
        val hidden = MintFeedCatalog.visible(18, MintClipLevel.EASY, setOf(original.first().id))
        assertEquals(original.size - 1, hidden.size)
        assertFalse(hidden.any { it.id == original.first().id })
    }

    @Test fun publicInteractionsNeverActivateEvenForAdults() {
        listOf("4–7","8–12","13–17","18–49","50+").forEach { band ->
            assertTrue(MintSocialSafety.allowed(band, MintSocialAction.WATCH))
            assertTrue(MintSocialSafety.allowed(band, MintSocialAction.REACT))
            assertTrue(MintSocialSafety.allowed(band, MintSocialAction.PRIVATE_PRACTICE))
            listOf(MintSocialAction.COMMENT,MintSocialAction.POST,MintSocialAction.MESSAGE,
                MintSocialAction.FOLLOW,MintSocialAction.SHARE,MintSocialAction.FRIEND_CHALLENGE).forEach {
                assertFalse(MintSocialSafety.allowed(band,it))
            }
        }
    }

    @Test fun reportsMustBeKnownLocalOnlyAndAreNotModerationSubmissionClaims() {
        val report = MintContentReport("greet-neighbor", MintReportReason.INAPPROPRIATE, 1760000000000L)
        val known = MintFeedCatalog.clips.map { it.id }.toSet()
        assertTrue(MintSocialSafety.validReport(report, known))
        assertEquals("greet-neighbor:inappropriate", MintSocialSafety.reportKey(report))
        assertFalse(MintSocialSafety.validReport(report.copy(clipId="unknown"),known))
        assertFalse(MintSocialSafety.validReport(report.copy(sentToModerators=true),known))
        assertFalse(MintSocialSafety.validReport(report.copy(createdAtMillis=0L),known))
    }
}
