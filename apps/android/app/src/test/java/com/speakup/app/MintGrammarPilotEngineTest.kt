package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarPilotEngineTest {
    @Test fun onlyThreeDistinctAnswersAndOneKey() {
        val q = MintGrammarPilotItem("legacy:0", listOf("go", "goes", "going"), "goes", "She goes.")
        assertTrue(MintGrammarPilotEngine.isValid(q))
        assertFalse(MintGrammarPilotEngine.isValid(q.copy(choices = listOf("go", "go"))))
        assertFalse(MintGrammarPilotEngine.isValid(q.copy(choices = listOf("go", "go", "going"))))
        assertFalse(MintGrammarPilotEngine.isValid(q.copy(correct = "unknown")))
    }
    @Test fun noPhantomSetsOrDuplicateQuestionIndices() {
        assertEquals(3, MintGrammarPilotEngine.setCount(18))
        assertEquals((0..5).toList(), MintGrammarPilotEngine.indicesForSet(18,0))
        assertEquals((6..11).toList(), MintGrammarPilotEngine.indicesForSet(18,1))
        assertEquals((12..17).toList(), MintGrammarPilotEngine.indicesForSet(18,2))
        assertTrue(MintGrammarPilotEngine.indicesForSet(18,3).isEmpty())
        assertEquals(18,(0..2).flatMap { MintGrammarPilotEngine.indicesForSet(18,it) }.distinct().size)
        assertEquals(listOf(6,7),MintGrammarPilotEngine.indicesForSet(8,1))
    }
    @Test fun resultsRequireExactlyOneFirstTryOutcomePerQuestion() {
        assertTrue(MintGrammarPilotEngine.validResult(4,2,6))
        assertFalse(MintGrammarPilotEngine.validResult(6,1,6))
        assertFalse(MintGrammarPilotEngine.validResult(0,0,6))
    }
}
