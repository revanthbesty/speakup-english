package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintDataProtectionTest {
    @Test fun preservesLegacyLearningDataInSelectiveExport() {
        val raw: Map<String, Any> = mapOf(
            "learning_path" to "speaking",
            "total_xp" to 650,
            "completed_lessons" to setOf("lesson1", "lesson2"),
            "mint_game_awards" to setOf("mint_game_reply-greet_v1"),
            "revision_due_1" to 1000L,
            "mint_theme_mode" to "dark",
            "profile_name" to "Learner"
        )
        val export = MintDataProtection.safeSnapshot(raw)
        assertEquals(7, export.size)
        assertEquals(650, export["total_xp"])
        assertEquals(listOf("lesson1", "lesson2"), export["completed_lessons"])
        assertTrue(MintDataProtection.audit(raw).isEmpty())
    }

    @Test fun excludesCredentialsAndUnexpectedPrivacySensitiveContent() {
        val raw = mapOf(
            "total_xp" to 10,
            "access_token" to "private-secret",
            "user_password" to "password",
            "speech_transcript" to "sensitive speech",
            "revision_due_bad-index" to 111L,
            "untrusted_raw_audio" to byteArrayOf(1,2,3)
        )
        val export = MintDataProtection.safeSnapshot(raw)
        assertEquals(setOf("total_xp"), export.keys)
        val json = MintDataProtection.encodeJson(export)
        assertFalse(json.contains("private-secret"))
        assertFalse(json.contains("sensitive speech"))
        assertTrue(json.contains("\"schemaVersion\":1"))
    }

    @Test fun jsonEscapesControlCharactersAndQuotationMarks() {
        val raw = mapOf<String, Any>("profile_name" to "A \"quote\" and\nnewline\\slash")
        val json = MintDataProtection.encodeJson(MintDataProtection.safeSnapshot(raw))
        assertTrue(json.contains("\\\"quote\\\""))
        assertTrue(json.contains("\\n"))
        assertTrue(json.contains("\\\\slash"))
    }

    @Test fun exportListsAreLimitedAndSortedDeterministically() {
        val raw = mapOf<String, Any>("completed_lessons" to (0..1200).map { "lesson-$it" }.toSet())
        val snapshot = MintDataProtection.safeSnapshot(raw)
        val list = snapshot["completed_lessons"] as List<*>
        assertEquals(1000, list.size)
        assertEquals(list.sortedBy { it.toString() }, list)
    }

    @Test fun invalidOldValuesProduceWarningsWithoutModification() {
        val raw: Map<String, Any> = mapOf(
            "total_xp" to -2,
            "learning_path" to 8,
            "completed_lessons" to "broken"
        )
        assertEquals(3, MintDataProtection.audit(raw).size)
        assertEquals(-2, raw["total_xp"])
    }

    @Test fun deletionScopeNeverIncludesEarnedProgress() {
        val protected = setOf("total_xp","completed_lessons","speaking_attempts",
            "mint_game_awards","revision_correct","profile_name","learner_age")
        assertTrue(MintDataProtection.privateInteractionKeys.intersect(protected).isEmpty())
        assertEquals(4, MintDataProtection.privateInteractionKeys.size)
    }

    @Test fun originalPreferencesNameAndApkIdentityStayUnchanged() {
        assertEquals("speakup_learning",MintIdentity.legacyPreferencesFile)
        assertEquals("com.speakup.app",MintIdentity.legacyApplicationId)
    }
}
