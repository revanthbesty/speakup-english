package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MintPetLearningEngineTest {
    @Test fun adventuresHaveDistinctIdsAndAllSkills() {
        assertEquals(5, MintPetLearningEngine.adventures.map { it.id }.distinct().size)
        assertEquals(MintPetSkill.values().toSet(),
            MintPetLearningEngine.adventures.map { it.skill }.toSet())
    }

    @Test fun aNewLearnerReceivesAWelcomeAndOfflineAdventure() {
        val result = MintPetLearningEngine.suggest(MintPetLearningSnapshot(0, 0, 0))
        assertEquals(MintPetMood.WELCOME, result.mood)
        assertEquals(MintPetSkill.VOCABULARY, result.adventure.skill)
    }

    @Test fun recentStruggleReturnsEncouragementNotPunishment() {
        val result = MintPetLearningEngine.suggest(MintPetLearningSnapshot(4, 3, 1))
        assertEquals(MintPetMood.ENCOURAGE, result.mood)
        assertTrue(result.message.contains("retry"))
    }

    @Test fun skillPreferenceControlsTheMissionWithoutChangingRewards() {
        val result = MintPetLearningEngine.suggest(
            MintPetLearningSnapshot(9, 0, 5, MintPetSkill.SPEAKING))
        assertEquals(MintPetSkill.SPEAKING, result.adventure.skill)
        assertEquals(MintPetMood.CELEBRATE, result.mood)
    }

    @Test fun worldGrowthUsesExistingCompletionThresholds() {
        assertEquals(0, MintPetLearningEngine.gardenStage(0))
        assertEquals(1, MintPetLearningEngine.gardenStage(1))
        assertEquals(2, MintPetLearningEngine.gardenStage(3))
        assertEquals(3, MintPetLearningEngine.gardenStage(7))
        assertEquals(4, MintPetLearningEngine.gardenStage(14))
    }
}
