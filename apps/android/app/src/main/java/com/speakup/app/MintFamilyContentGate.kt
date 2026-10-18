package com.speakup.app

/**
 * Offline-only boundary for future editorially approved English Mint content.
 *
 * Draft records from PR #35 are deliberately NOT bundled or surfaced to learners.
 * A remote server or self-declared review status cannot bypass this gate.
 */
internal data class MintReviewableActivity(
    val id: String,
    val category: String,
    val level: String,
    val editoriallyApproved: Boolean,
    val sourceVerified: Boolean
)

internal object MintFamilyContentGate {
    private val assignableCategories = setOf("word_builder", "reading")
    private val allowedLevels = setOf("A1", "A2", "B1", "B2", "C1")

    /**
     * A family/teacher preview must never turn an unreviewed replacement into
     * an actual learner assignment; this gate does not prove identity or consent.
     */
    fun isCandidateAvailableForLocalAssignment(item: MintReviewableActivity): Boolean =
        item.editoriallyApproved && item.sourceVerified &&
            item.id.isNotBlank() &&
            item.category in assignableCategories &&
            item.level in allowedLevels

    fun candidateIdsForLocalPreview(items: List<MintReviewableActivity>): List<String> =
        items.filter(::isCandidateAvailableForLocalAssignment).map { it.id }.distinct()

    /** Safe default until an independently approved offline pack is shipped. */
    fun shippedContentCandidates(): List<MintReviewableActivity> = emptyList()
}
