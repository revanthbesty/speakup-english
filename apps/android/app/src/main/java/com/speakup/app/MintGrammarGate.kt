package com.speakup.app

/**
 * Sprint 26-compatible Grammar Arena staging contract.
 * Does not replace built-in questions or publish unreviewed content.
 */
internal enum class MintGrammarStatus { DRAFT, VALIDATED, APPROVED, PUBLISHED, REJECTED }

internal data class MintGrammarDraft(
    val id: String,
    val version: Int,
    val topicId: String,
    val prompt: String,
    val options: List<String>,
    val correctKey: Char,
    val feedback: List<String>,
    val grammarRule: String,
    val exampleSentence: String,
    val status: MintGrammarStatus,
    val reviewerId: String?,
    val approvedAt: String?,
    val rightsReviewed: Boolean
)

internal data class MintGrammarAnswerFeedback(
    val isCorrect: Boolean,
    val selectedExplanation: String,
    val correctExplanation: String,
    val correctKey: Char,
    val grammarRule: String,
    val exampleSentence: String
)

internal object MintGrammarGate {
    fun eligible(question: MintGrammarDraft): Boolean =
        question.status == MintGrammarStatus.APPROVED &&
            question.id.isNotBlank() && question.version > 0 &&
            question.topicId.isNotBlank() && question.prompt.isNotBlank() &&
            question.options.size == 3 && question.options.all(String::isNotBlank) &&
            question.options.distinct().size == 3 &&
            question.feedback.size == 3 && question.feedback.all(String::isNotBlank) &&
            question.correctKey in 'A'..'C' &&
            question.grammarRule.isNotBlank() && question.exampleSentence.isNotBlank() &&
            !question.reviewerId.isNullOrBlank() &&
            !question.approvedAt.isNullOrBlank() &&
            question.rightsReviewed

    fun feedback(question: MintGrammarDraft, selected: Char): MintGrammarAnswerFeedback? {
        if (!eligible(question) || selected !in 'A'..'C') return null
        val chosen = selected - 'A'
        val correct = question.correctKey - 'A'
        return MintGrammarAnswerFeedback(
            isCorrect = chosen == correct,
            selectedExplanation = question.feedback[chosen],
            correctExplanation = question.feedback[correct],
            correctKey = question.correctKey,
            grammarRule = question.grammarRule,
            exampleSentence = question.exampleSentence
        )
    }

    fun publishableBatch(questions: List<MintGrammarDraft>): List<MintGrammarDraft> {
        if (questions.isEmpty() || questions.any { !eligible(it) }) return emptyList()
        if (questions.map { it.id }.distinct().size != questions.size) return emptyList()
        return questions
    }
}
