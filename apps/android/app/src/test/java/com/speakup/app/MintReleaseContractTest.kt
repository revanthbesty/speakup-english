package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

/**
 * Integration guardrails covering continuity and safety across the ten development stages.
 * Passing these unit tests does not prove UI accessibility, external provider integrations,
 * signed APK upgrade compatibility or production readiness.
 */
class MintReleaseContractTest {
    @Test fun existingInstallAndNewBrandRemainCompatible() {
        assertEquals("com.speakup.app", MintIdentity.legacyApplicationId)
        assertEquals("speakup_learning", MintIdentity.legacyPreferencesFile)
        assertEquals("English Mint", MintIdentity.displayName)
        assertTrue(MintIdentity.protectedPreferenceKeys.containsAll(
            setOf("total_xp", "completed_lessons", "speaking_attempts", "placement_score")
        ))
    }

    @Test fun allCuratedQuestsRemainPlayableAndFirstMasteryAwardsAreUnique() {
        val all = MintGameCatalog.allDefinitions
        assertEquals(72, all.size)
        assertEquals(all.size, all.map { it.id }.toSet().size)
        assertEquals(8, all.map { it.mechanic }.toSet().size)
        all.forEach { quest ->
            assertTrue("Invalid quest ${quest.id}", quest.isValid())
            assertTrue("Unsolvable quest ${quest.id}", quest.evaluate(quest.answer))
            val first = MintGameEngine.reward(quest, quest.answer, emptySet())
            assertEquals(15, first.xpAward)
            assertEquals(0, MintGameEngine.reward(quest, quest.answer, setOf(first.rewardKey!!)).xpAward)
        }
    }

    @Test fun ageRestrictionsStayClosedForSmallChildren() {
        assertTrue(MintGameCatalog.listFor(MintDifficulty.HARD, 4).isEmpty())
        assertTrue(MintFeedCatalog.visible(4, MintClipLevel.HARD, emptySet()).isEmpty())
        assertTrue(MintConversationEngine.scenarios(MintConversationLevel.ADVANCED,4).isEmpty())
        MintSocialAction.entries.filter { it !in setOf(
            MintSocialAction.WATCH, MintSocialAction.REACT, MintSocialAction.PRIVATE_PRACTICE
        ) }.forEach { assertFalse(MintSocialSafety.allowed("4–7", it)) }
    }

    @Test fun paidFeaturesAndUnassessedCertificatesStayDisabled() {
        assertEquals(MintTier.FREE, MintMembershipPolicy.effectiveTier(null, System.currentTimeMillis()))
        assertTrue(MintMembershipPolicy.offers().none { MintMembershipPolicy.checkoutEnabled(it,true) })
        val xpOnly = MintProgressEvidence(900000,0,0,0,0,0,0,0,0,0)
        assertFalse(MintRewards.certificate(xpOnly).eligibleForAssessment)
    }

    @Test fun localExportKeepsAchievementHistoryWithoutExposingUnknownSecrets() {
        val saved = mapOf<String, Any>(
            "total_xp" to 100, "mint_game_awards" to setOf("mint_game_reply-greet_v1"),
            "completed_lessons" to (0 until 1500).map { "lesson-$it" }.toSet(),
            "auth_token" to "never-share-this",
            "speech_audio" to "private-transcript"
        )
        val export = MintDataProtection.safeSnapshot(saved)
        assertEquals(1500, (export["completed_lessons"] as List<*>).size)
        assertEquals(100, export["total_xp"])
        assertFalse(MintDataProtection.encodeJson(export).contains("never-share-this"))
        assertFalse(MintDataProtection.encodeJson(export).contains("private-transcript"))
        assertFalse("completed_lessons" in MintDataProtection.privateInteractionKeys)
        assertFalse("total_xp" in MintDataProtection.privateInteractionKeys)
    }

    @Test fun quietTextIsNeverMisrepresentedAsRecognizedSpeech() {
        val turn = MintConversationEngine.all().first().turns.first()
        val result = MintConversationEngine.evaluate(turn, "Hello my name is Alex", MintResponseSource.TEXT)
        assertFalse(result.speakingCounted)
        assertFalse(result.confidenceKnown)
    }
}
