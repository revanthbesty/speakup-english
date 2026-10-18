package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintCoachPlannerTest {
    @Test fun deterministicOfflinePlanIncludesActualRoutes() {
        val p=MintCoachPlanner.build("Speaking confidence","Easy",0,0)
        assertEquals(2,p.steps.size)
        assertTrue(p.steps.all { MintCoachPlanner.allowedRoute(it.route) })
        assertEquals("Real-World Quests",p.steps[0].gameMode)
        assertEquals("Speak",p.steps[1].route)
        assertEquals(p,MintCoachPlanner.build("Speaking confidence","Easy",0,0))
    }
    @Test fun plansAdaptToExistingProgressWithoutFakeScores() {
        val p=MintCoachPlanner.build("Grammar","Medium",2,2)
        assertEquals("Learn",p.steps[0].route)
        assertEquals("Grammar",p.steps[1].gameMode)
        assertFalse(p.reason.contains("accuracy",ignoreCase=true))
    }
    @Test fun safeFeedbackDoesNotPretendToGradePronunciation() {
        assertTrue(MintCoachPlanner.feedback("").contains("skip"))
        assertTrue(MintCoachPlanner.feedback("Hello").contains("polite"))
        assertFalse(MintCoachPlanner.feedback("Could you please help me with directions?").contains("score"))
    }
}
