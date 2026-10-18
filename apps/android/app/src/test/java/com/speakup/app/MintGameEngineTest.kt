package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGameEngineTest {
    @Test fun allDefinitionsHaveDistinctIdsAndValidAnswers() {
        val catalog = MintGameCatalog.definitions
        assertEquals(24, catalog.size)
        assertEquals(catalog.size, catalog.map { it.id }.toSet().size)
        assertTrue(catalog.all { it.isValid() })
        assertEquals(4, catalog.map { it.mechanic }.toSet().size)
        assertEquals(3, catalog.map { it.difficulty }.toSet().size)
    }

    @Test fun everyMechanicHasTwoPlayableQuestsPerDifficulty() {
        MintMechanic.entries.forEach { mechanic ->
            MintDifficulty.entries.forEach { level ->
                val quests = MintGameCatalog.listFor(level).filter { it.mechanic == mechanic }
                assertTrue("Every mechanic must have at least two authored quests", quests.size >= 2)
                assertTrue(quests.all { it.evaluate(it.answer) })
            }
        }
    }

    @Test fun acceptsWhitespaceAndCaseButNotWrongChoices() {
        val choose = MintGameCatalog.byId("reply-greet")!!
        assertTrue(choose.evaluate(" nice to meet you too! "))
        assertFalse(choose.evaluate("I am a sandwich."))
        val order = MintGameCatalog.byId("order-hello")!!
        assertTrue(order.evaluate("  Hello,   my   name is Sam. "))
        assertFalse(order.evaluate("Sam. is my name Hello,"))
    }

    @Test fun doesNotAwardDuplicateXpAfterRepeatedMastery() {
        val game = MintGameCatalog.byId("blank-am")!!
        val first = MintGameEngine.reward(game, game.answer, emptySet())
        assertTrue(first.correct)
        assertEquals(15, first.xpAward)
        val duplicate = MintGameEngine.reward(game, game.answer, setOf(first.rewardKey!!))
        assertTrue(duplicate.correct)
        assertEquals(0, duplicate.xpAward)
        assertNull(duplicate.rewardKey)
        assertEquals(0, MintGameEngine.reward(game, "wrong", emptySet()).xpAward)
    }

    @Test fun ageFilteringDoesNotExposeAdvancedOrTeenContentToYoungChildren() {
        assertTrue(MintGameCatalog.listFor(MintDifficulty.EASY, 4).isNotEmpty())
        assertTrue(MintGameCatalog.listFor(MintDifficulty.MEDIUM, 4).isEmpty())
        assertTrue(MintGameCatalog.listFor(MintDifficulty.HARD, 8).isEmpty())
        assertTrue(MintGameCatalog.listFor(MintDifficulty.HARD, 13).size >= 8)
    }

    @Test fun allActivitiesHaveSpeakingFollowUpAndUsefulHints() {
        assertTrue(MintGameCatalog.definitions.all {
            it.speakingPrompt.length > 8 && it.hint.length > 8 && it.explanation.length > 8
        })
    }
}
