package com.speakup.app

/**
 * Presentation-only guard for the three-answer Grammar Arena.
 * It does not score answers or award XP.
 */
internal object MintGrammarUiContract {
    fun isPlayable(choices: List<String>, answer: String): Boolean =
        choices.size == 3 && choices.distinct().size == 3 && choices.all(String::isNotBlank) && answer in choices

    fun optionLabel(index: Int, choice: String): String {
        require(index in 0..2) { "Grammar Arena only supports three options" }
        return "${"ABC"[index]}. $choice"
    }
}
