package com.speakup.app

/** Offline-only pilot sessions; no claims of independently approved 10,000 items. */
internal data class MintGrammarPilotItem(
    val id: String,
    val choices: List<String>,
    val correct: String,
    val explanation: String
)
internal object MintGrammarPilotEngine {
    const val setSize = 6
    fun isValid(item: MintGrammarPilotItem): Boolean =
        item.id.isNotBlank() && item.choices.size == 3 &&
        item.choices.all { it.isNotBlank() } &&
        item.choices.map { it.trim().lowercase() }.distinct().size == 3 &&
        item.correct in item.choices && item.explanation.isNotBlank()
    fun setCount(questionCount: Int): Int = if (questionCount <= 0) 0 else (questionCount + setSize - 1) / setSize
    fun indicesForSet(questionCount: Int, setIndex: Int): List<Int> {
        if (questionCount <= 0 || setIndex !in 0 until setCount(questionCount)) return emptyList()
        val start = setIndex * setSize
        return (start until minOf(start + setSize, questionCount)).toList()
    }
    fun validResult(correctFirstTry: Int, requiredRetry: Int, size: Int): Boolean =
        size > 0 && correctFirstTry >= 0 && requiredRetry >= 0 &&
        correctFirstTry + requiredRetry == size
}
