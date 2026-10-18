package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintExperiencePolicyTest {
    @Test fun adultAndChildVisualBandsDoNotAlterStoredLegacyCategory() {
        assertEquals("Kids", MintExperiencePolicy.legacyAge("4–7"))
        assertEquals("Kids", MintExperiencePolicy.legacyAge("8–12"))
        assertEquals("Teen", MintExperiencePolicy.legacyAge("13–17"))
        assertEquals("Adult", MintExperiencePolicy.legacyAge("50+"))
        assertTrue(MintExperiencePolicy.childSafetyEnabled("13–17"))
        assertFalse(MintExperiencePolicy.childSafetyEnabled("50+"))
    }
    @Test fun allFiveJourneysHaveStableLegacyIdentifiers() {
        assertEquals(5, MintExperiencePolicy.journeys.size)
        assertEquals("Speaking Accelerator", MintExperiencePolicy.selectedJourney("speaking"))
        assertEquals("Exam Champion", MintExperiencePolicy.selectedJourney("exams"))
    }
    @Test fun nightScheduleAndOverridesArePredictable() {
        assertTrue(MintExperiencePolicy.nightModeEnabled("night", false, 22))
        assertTrue(MintExperiencePolicy.nightModeEnabled("night", false, 2))
        assertFalse(MintExperiencePolicy.nightModeEnabled("night", true, 12))
        assertTrue(MintExperiencePolicy.nightModeEnabled("dark", false, 12))
        assertFalse(MintExperiencePolicy.nightModeEnabled("light", true, 22))
        assertEquals("system", MintExperiencePolicy.normalizedMode("invalid"))
    }
    @Test fun reducedMotionNeverOverridesAccessibilityOff() {
        assertTrue(MintExperiencePolicy.reducedMotion(false, true))
        assertTrue(MintExperiencePolicy.reducedMotion(true, false))
        assertFalse(MintExperiencePolicy.reducedMotion(false, false))
    }
}
