package com.speakup.app

/**
 * Deliberately strict bridge from imported content to playable Games.
 * Editorial review must happen outside the shipped JSON; the `review_status`
 * in a draft pack is never interpreted as evidence of independent approval.
 */
internal data class MintApprovedGameQuestion(
    val id: String,
    val category: String,
    val level: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

internal object MintApprovedGamesBridge {
    private val playableCategories = setOf("word_builder", "reading", "riddles",
        "vocabulary", "situations", "listening", "dialogues", "stories")
    private val levels = setOf("A1", "A2", "B1", "B2", "C1")

    /**
     * Trusted editorial allowlist is supplied by a separately approved release
     * manifest, never taken from the source activity's mutable review label.
     * For the current Sprint 26 release this list is empty.
     */
    fun approvedReleaseIds(): Set<String> = emptySet()

    fun playableRecords(
        candidates: List<MintDraftRecord>,
        independentlyApprovedIds: Set<String> = approvedReleaseIds()
    ): List<MintDraftRecord> {
        if (independentlyApprovedIds.isEmpty()) return emptyList()
        val duplicates = candidates.groupingBy { it.id }.eachCount().any { it.value > 1 }
        require(!duplicates) { "Duplicate candidate IDs must not enter Games" }
        return candidates.filter {
            it.id in independentlyApprovedIds && it.id.isNotBlank() &&
                it.category in playableCategories && it.level in levels &&
                it.question.isNotBlank() && it.explanation.isNotBlank()
        }
    }

    fun mayAwardProgress(itemId: String): Boolean = itemId in approvedReleaseIds()
}
