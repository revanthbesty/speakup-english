package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintConversationEngineTest {
    @Test fun catalogHasSixScenesThreeTurnsWithUniqueIds() {
        val scenes = MintConversationEngine.all()
        assertEquals(6, scenes.size)
        assertEquals(6, scenes.map { it.id }.toSet().size)
        assertTrue(scenes.all { it.turns.size == 3 })
        assertEquals(18, scenes.flatMap { it.turns }.map { it.id }.toSet().size)
        assertTrue(scenes.flatMap { it.turns }.all {
            it.hint.isNotBlank() && it.prompt.isNotBlank() && it.suggestedResponse.isNotBlank()
        })
        assertEquals(3, scenes.flatMap { it.turns }.map { it.mode }.toSet().size)
    }

    @Test fun childrenCannotAccessAdultInterviewAndPresentation() {
        val young = MintConversationEngine.scenarios(MintConversationLevel.BEGINNER, 4)
        assertEquals(listOf("meet"), young.map { it.id })
        assertTrue(MintConversationEngine.scenarios(MintConversationLevel.INTERMEDIATE, 12).isEmpty())
        assertTrue(MintConversationEngine.scenarios(MintConversationLevel.ADVANCED, 12).isEmpty())
        assertTrue(MintConversationEngine.scenarios(MintConversationLevel.INTERMEDIATE, 18).any { it.id == "interview" })
    }

    @Test fun difficultySelectsRealScenarioLevelNotAge() {
        assertEquals(MintConversationLevel.BEGINNER, MintConversationEngine.recommendedLevel("Easy"))
        assertEquals(MintConversationLevel.INTERMEDIATE, MintConversationEngine.recommendedLevel("Medium"))
        assertEquals(MintConversationLevel.ADVANCED, MintConversationEngine.recommendedLevel("Hard"))
    }

    @Test fun quietModeTextNeverCountsAsSpeaking() {
        val turn = MintConversationEngine.all().first().turns.first()
        val result = MintConversationEngine.evaluate(turn, "Hello my name is Priya", MintResponseSource.TEXT)
        assertEquals(MintFeedbackStatus.PRACTISED, result.status)
        assertFalse(result.speakingCounted)
        assertFalse(result.confidenceKnown)
    }

    @Test fun speechIsCountedOnlyWhenNonemptyAndSubstantive() {
        val turn = MintConversationEngine.all().first().turns.first()
        assertFalse(MintConversationEngine.evaluate(turn, "", MintResponseSource.SPEECH).speakingCounted)
        assertFalse(MintConversationEngine.evaluate(turn, "hi", MintResponseSource.SPEECH).speakingCounted)
        val result = MintConversationEngine.evaluate(turn, "Hello my name is Priya", MintResponseSource.SPEECH)
        assertTrue(result.speakingCounted)
        assertFalse(result.confidenceKnown)
    }

    @Test fun accentVariationDoesNotReceiveFakeAccuracyOrHarshRejection() {
        val turn = MintConversationEngine.all().first().turns.first()
        val result = MintConversationEngine.evaluate(turn, "Lovely to meet everybody today", MintResponseSource.SPEECH)
        assertEquals(MintFeedbackStatus.PRACTISED, result.status)
        assertTrue(result.message.contains("may also work"))
    }

    @Test fun supportsAllThreeSpeakingInteractionPatterns() {
        val modes = MintConversationEngine.all().flatMap { it.turns }.map { it.mode }.toSet()
        assertTrue(modes.containsAll(MintVoiceMode.entries))
    }
}
