package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MintIdentityTest {
    @Test fun keepsUpgradeIdentityStable() {
        assertEquals("com.speakup.app", MintIdentity.legacyApplicationId)
        assertEquals("speakup_learning", MintIdentity.legacyPreferencesFile)
    }

    @Test fun preservesEssentialLegacyProgressKeys() {
        assertTrue(MintIdentity.protectedPreferenceKeys.containsAll(
            setOf("total_xp", "completed_lessons", "puzzle_awards", "placement_score", "speaking_attempts")
        ))
    }

    @Test fun exposesApprovedBrandCopy() {
        assertEquals("English Mint", MintIdentity.displayName)
        assertEquals("Fresh Games. Fluent Voices.", MintIdentity.tagline)
    }
}
