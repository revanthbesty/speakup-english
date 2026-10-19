package com.speakup.app

/** Deterministic, offline-first pet teaching and adventure selection.
 * No XP is granted by this engine; achievements remain owned by the learning engine.
 */
internal enum class MintPetSkill { VOCABULARY, GRAMMAR, READING, LISTENING, SPEAKING }
internal enum class MintPetMood { WELCOME, ENCOURAGE, CELEBRATE, EXPLORE, FOCUS }

internal data class MintPetLearningSnapshot(
    val completedLessons: Int,
    val recentIncorrectAttempts: Int,
    val recentCorrectAttempts: Int,
    val preferredSkill: MintPetSkill? = null
)

internal data class MintPetAdventure(
    val id: String,
    val title: String,
    val description: String,
    val skill: MintPetSkill,
    val goal: String
)

internal data class MintPetSuggestion(
    val mood: MintPetMood,
    val message: String,
    val adventure: MintPetAdventure
)

internal object MintPetLearningEngine {
    val adventures = listOf(
        MintPetAdventure("word_garden", "Plant a word garden",
            "Discover words and let your garden grow.", MintPetSkill.VOCABULARY, "Practise vocabulary"),
        MintPetAdventure("grammar_bridge", "Repair the grammar bridge",
            "Find the right words to rebuild the path.", MintPetSkill.GRAMMAR, "Practise grammar"),
        MintPetAdventure("story_lantern", "Light the story lantern",
            "Read a short passage and find its meaning.", MintPetSkill.READING, "Practise reading"),
        MintPetAdventure("echo_cave", "Explore the echo cave",
            "Listen carefully and discover the missing clue.", MintPetSkill.LISTENING, "Practise listening"),
        MintPetAdventure("voice_gateway", "Open the voice gateway",
            "Build confidence with a short speaking activity.", MintPetSkill.SPEAKING, "Practise speaking")
    )

    fun suggest(snapshot: MintPetLearningSnapshot): MintPetSuggestion {
        val skill = snapshot.preferredSkill
            ?: if (snapshot.recentIncorrectAttempts > snapshot.recentCorrectAttempts)
                MintPetSkill.GRAMMAR else MintPetSkill.VOCABULARY
        val adventure = adventures.first { it.skill == skill }
        val mood = when {
            snapshot.recentIncorrectAttempts > snapshot.recentCorrectAttempts ->
                MintPetMood.ENCOURAGE
            snapshot.completedLessons >= 7 && snapshot.recentCorrectAttempts > 0 ->
                MintPetMood.CELEBRATE
            snapshot.completedLessons == 0 -> MintPetMood.WELCOME
            else -> MintPetMood.EXPLORE
        }
        val message = when (mood) {
            MintPetMood.ENCOURAGE -> "Want a hint? Every retry helps you learn."
            MintPetMood.CELEBRATE -> "Look how much your learning world has grown!"
            MintPetMood.WELCOME -> "Your first adventure is ready whenever you are."
            MintPetMood.EXPLORE -> "There is a new path to discover."
            MintPetMood.FOCUS -> "Take your time. I am here when you need help."
        }
        return MintPetSuggestion(mood, message, adventure)
    }

    fun gardenStage(completedLessons: Int): Int = MintverseGrowth.stage(completedLessons)
}
