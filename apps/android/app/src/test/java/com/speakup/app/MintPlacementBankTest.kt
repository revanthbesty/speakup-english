package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MintPlacementBankTest {
    @Test fun eachLevelReceivesThreeDistinctValidQuestions() {
        listOf("Easy", "Medium", "Hard", "Exam").forEach { level ->
            val questions = MintPlacementBank.session(level, 0)
            assertEquals(3, questions.size)
            assertEquals(3, questions.map { it.id }.toSet().size)
            assertTrue(questions.all { it.level == level && it.correct in it.choices })
        }
    }

    @Test fun beginnerAndAdvancedQuestionsAreDifferent() {
        val beginner = MintPlacementBank.session("Easy", 0).map { it.prompt }.toSet()
        val advanced = MintPlacementBank.session("Hard", 0).map { it.prompt }.toSet()
        assertTrue(beginner.intersect(advanced).isEmpty())
    }

    @Test fun retakesRotateQuestionSets() {
        listOf("Easy", "Medium", "Hard", "Exam").forEach { level ->
            val first = MintPlacementBank.session(level, 0).map { it.id }
            val second = MintPlacementBank.session(level, 1).map { it.id }
            assertNotEquals(first, second)
            assertTrue(first.toSet().intersect(second.toSet()).isEmpty())
        }
    }

    @Test fun journeyAndPreferenceChooseSuitableStartingChallenge() {
        assertEquals("Easy", MintPlacementBank.startingLevel("zero", "Easy", "Start speaking"))
        assertEquals("Medium", MintPlacementBank.startingLevel("basic", "Medium", "Everyday conversation"))
        assertEquals("Medium", MintPlacementBank.startingLevel("speaking", "Easy", "Start speaking"))
        assertEquals("Hard", MintPlacementBank.startingLevel("advanced", "Easy", "Work & interviews"))
        assertEquals("Exam", MintPlacementBank.startingLevel("exams", "Medium", "School & exams"))
    }

    @Test fun recommendationsReflectAssessmentLevel() {
        assertTrue(MintPlacementBank.recommendation("Hard", 3).contains("Advanced"))
        assertTrue(MintPlacementBank.recommendation("Exam", 3).contains("Exam"))
        assertTrue(MintPlacementBank.recommendation("Easy", -1).contains("skipped"))
    }
}
