package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarChallengesTest {
    @Test fun requiresSixDistinctVerifiedGrammarQuestions() {
        assertFalse(MintGrammarChallenges.unlocked(18,setOf("0","1","2","3","4")))
        assertTrue(MintGrammarChallenges.unlocked(18,setOf("0","1","2","3","4","5")))
        assertFalse(MintGrammarChallenges.unlocked(18,setOf("0","1","2","3","4","999")))
    }
    @Test fun achievementIsOnceOnlyAndHandlesEmptyPilot() {
        val done=(0..5).map(Int::toString).toSet()
        assertTrue(MintGrammarChallenges.newlyEligible(18,done,emptySet()))
        assertFalse(MintGrammarChallenges.newlyEligible(18,done,setOf(MintGrammarChallenges.achievementKey)))
        assertFalse(MintGrammarChallenges.unlocked(0,done))
    }
}
