package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintRewardsTest {
    private val zero = MintProgressEvidence(0,0,0,0,0,0,0,0,0,0)

    @Test fun boundariesAndProgressAreSafe() {
        assertEquals(1,MintRewards.level(-10).number)
        assertEquals(1,MintRewards.level(99).number)
        assertEquals(2,MintRewards.level(100).number)
        assertEquals(10,MintRewards.level(12000).number)
        assertNull(MintRewards.level(12000).nextTarget)
        assertEquals(0f, MintRewards.level(100).progress(100))
        assertEquals(1f, MintRewards.level(12000).progress(12000))
    }

    @Test fun badgesAreDrivenByEvidenceAndDoNotClaimMasteryFromXpAlone() {
        assertTrue(MintRewards.badges(zero).none { it.earned })
        val xpOnly = zero.copy(xp=500)
        assertEquals(listOf("xp_collector"), MintRewards.badges(xpOnly).filter { it.earned }.map { it.id })
        val all = zero.copy(xp=1000,completedLessons=4,puzzlesMastered=3,
            mintQuestsMastered=4,speakingAttempts=3,guidedVoiceTurns=3,
            revisionCorrect=3,listeningMastered=2,readingMastered=2,independentReplies=3)
        assertEquals(10,MintRewards.badges(all).count { it.earned })
        assertEquals(10,MintRewards.badges(all).map { it.id }.toSet().size)
    }

    @Test fun coinsComeOnlyFromUniqueVersionedMintMasteryKeys() {
        val first = "mint_game_reply-greet_v1"
        val second = "mint_game_reply-shop_v1"
        assertEquals(0,MintRewards.cosmeticCoins(emptySet()))
        assertEquals(5,MintRewards.cosmeticCoins(setOf(first,"puzzle_0","freebie")))
        assertEquals(10,MintRewards.cosmeticCoins(setOf(first,second)))
        assertEquals(10,MintRewards.cosmeticCoins(setOf(first,second,second)))
        assertEquals(0,MintRewards.cosmeticCoins(setOf("mint_game_reply-greet_v0","mint_game_../evil_v1")))
    }

    @Test fun noCertificatesIssuedForXpOrBadgesAlone() {
        assertFalse(MintRewards.certificate(zero.copy(xp=100000)).eligibleForAssessment)
        val readiness = MintRewards.certificate(zero.copy(completedLessons=4,guidedVoiceTurns=3,revisionCorrect=3))
        assertTrue(readiness.eligibleForAssessment)
        assertTrue(readiness.message.contains("No assessment or credential has been issued"))
    }

    @Test fun perSkillProgressShowsEvidenceNotFabricatedScores() {
        val summaries = MintRewards.skills(zero.copy(speakingAttempts=2,guidedVoiceTurns=3))
        assertEquals(5,summaries.size)
        assertEquals(5,summaries.first().activities)
        assertTrue(summaries.first().evidence.contains("2 microphone attempts"))
    }
}
