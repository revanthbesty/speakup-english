package com.speakup.app

/**
 * Versioned, content-driven game definitions for English Mint.
 *
 * One engine powers many curated items. A variant represents an authored learning
 * activity, not an artificial count of random permutations.
 */
internal enum class MintMechanic { CHOOSE_REPLY, MISSING_WORD, WORD_SCRAMBLE, SENTENCE_ORDER, LISTEN_AND_CHOOSE, STORY_DECISION }
internal enum class MintDifficulty { EASY, MEDIUM, HARD }
internal enum class MintSkill { VOCABULARY, GRAMMAR, SPEAKING, READING, LISTENING }

internal data class MintGameDefinition(
    val id: String,
    val version: Int,
    val mechanic: MintMechanic,
    val difficulty: MintDifficulty,
    val skill: MintSkill,
    val title: String,
    val prompt: String,
    val tokens: List<String>,
    val answer: String,
    val hint: String,
    val explanation: String,
    val speakingPrompt: String,
    val minAge: Int = when(difficulty) { MintDifficulty.EASY -> 4; MintDifficulty.MEDIUM -> 8; MintDifficulty.HARD -> 13 },
    val locale: String = "en",
    val listeningLine: String? = null,
) {
    fun isValid(): Boolean =
        id.isNotBlank() && version > 0 && title.isNotBlank() &&
            prompt.isNotBlank() && answer.isNotBlank() && hint.isNotBlank() &&
            explanation.isNotBlank() && speakingPrompt.isNotBlank() &&
            tokens.size >= 2 && tokens.all { it.isNotBlank() } &&
            when(mechanic) {
                MintMechanic.CHOOSE_REPLY, MintMechanic.MISSING_WORD, MintMechanic.STORY_DECISION ->
                    tokens.distinct().size == tokens.size && tokens.count { it == answer } == 1
                MintMechanic.LISTEN_AND_CHOOSE ->
                    !listeningLine.isNullOrBlank() && tokens.distinct().size == tokens.size && tokens.count { it == answer } == 1
                MintMechanic.WORD_SCRAMBLE ->
                    tokens.size == 1 + 1 && tokens.joinToString("").toList().sorted() == answer.toList().sorted()
                MintMechanic.SENTENCE_ORDER ->
                    tokens.sorted() == answer.split(" ").sorted()
            }

    fun evaluate(response: String): Boolean {
        val normalize: (String) -> String = { it.trim().replace(Regex("\\s+"), " ").lowercase() }
        return normalize(response) == normalize(answer)
    }
}

internal data class MintGameResult(val correct: Boolean, val xpAward: Int, val rewardKey: String?)

/** Unique keyed rewards avoid awarding XP twice for the same authored activity. */
internal object MintGameEngine {
    fun reward(definition: MintGameDefinition, response: String, awardedIds: Set<String>): MintGameResult {
        val correct = definition.evaluate(response)
        val rewardKey = "mint_game_${definition.id}_v${definition.version}"
        return MintGameResult(
            correct = correct,
            xpAward = if (correct && rewardKey !in awardedIds) 15 else 0,
            rewardKey = if (correct && rewardKey !in awardedIds) rewardKey else null,
        )
    }
}

