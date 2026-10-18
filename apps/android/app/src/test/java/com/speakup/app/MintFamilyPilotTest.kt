package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintFamilyPilotTest {
    @Test fun templatesRemainOnExistingScreens() {
        assertEquals(3, MintFamilyPilot.templates.size)
        assertTrue(MintFamilyPilot.templates.all(MintFamilyPilot::valid))
    }
    @Test fun completionRequiresActualLearningEvidence() {
        val assignment=MintFamilyPilot.templates.first()
        assertFalse(MintFamilyPilot.markDone(assignment,false).completed)
        assertTrue(MintFamilyPilot.markDone(assignment,true).completed)
    }
    @Test fun noRemoteLearnerAccessWithoutBackendPolicy() {
        assertFalse(MintFamilyPilot.mayViewOtherLearnerProgress(false,false))
        assertFalse(MintFamilyPilot.mayViewOtherLearnerProgress(true,true))
    }
}
