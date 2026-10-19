package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MintGamesRecommendationsTest {
    @Test fun modesRemainUniqueAndCoverCoreLearningActivities() {
        assertEquals(MintGamesRecommendations.modes.size,
            MintGamesRecommendations.modes.distinct().size)
        listOf("Grammar", "Riddles", "Vocabulary", "Listening", "Reading",
            "Situations", "Word Builder", "Dialogues").forEach {
            assertTrue("Missing game mode $it", it in MintGamesRecommendations.modes)
        }
    }

    @Test fun recommendationsAreExistingGameModes() {
        listOf("Start speaking", "School & exams", "Work & interviews",
            "Reading & writing", "Travel", "Unknown goal").forEach { goal ->
            val recommended = MintGamesRecommendations.forGoal(goal)
            assertEquals(3, recommended.size)
            assertEquals(3, recommended.distinct().size)
            assertTrue("$goal recommends unavailable game",
                recommended.all { it in MintGamesRecommendations.modes })
        }
    }

    @Test fun fallbackAndTravelGoalsStayDeterministic() {
        assertEquals(listOf("Vocabulary", "Situations", "Word Builder"),
            MintGamesRecommendations.forGoal("Unknown goal"))
        assertEquals(listOf("Dialogues", "Situations", "Listening"),
            MintGamesRecommendations.forGoal("Travel"))
    }
}
