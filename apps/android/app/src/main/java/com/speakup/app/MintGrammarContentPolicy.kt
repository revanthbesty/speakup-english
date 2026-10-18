package com.speakup.app

/** Sprint 29 editorial contract: only approved, reviewed content may enter scored sessions. */
internal enum class MintGrammarApproval { DRAFT, REVIEW, APPROVED, RETIRED }
internal enum class MintGrammarTier { EASY, MEDIUM, HARD }
internal data class MintGrammarEntry(
    val id: String, val version: Int, val topicId: String, val subtopicId: String,
    val tier: MintGrammarTier, val prompt: String, val options: Map<String, String>,
    val correctKey: String, val explanation: String, val learningOutcome: String,
    val ageSegment: String, val format: String, val locale: String,
    val author: String, val reviewerId: String?, val rightsStatus: String,
    val approval: MintGrammarApproval, val approvedAt: String?,
    val duplicateHash: String, val altText: String? = null, val transcript: String? = null
)
internal object MintGrammarContentPolicy {
    val tiers = mapOf(MintGrammarTier.EASY to 3000, MintGrammarTier.MEDIUM to 3500, MintGrammarTier.HARD to 3500)
    const val domainCount = 25
    const val perDomain = 400
    const val intendedTotal = domainCount * perDomain
    private val keys = setOf("A", "B", "C")
    private val mediaFormats = setOf("visual", "audio", "dialogue", "story")
    fun structurallyValid(q: MintGrammarEntry): Boolean =
        q.id.matches(Regex("[A-Za-z0-9-]+")) && q.version > 0 &&
        q.topicId.isNotBlank() && q.subtopicId.isNotBlank() &&
        q.prompt.isNotBlank() && q.learningOutcome.isNotBlank() && q.ageSegment.isNotBlank() &&
        q.options.keys == keys && q.options.values.all { it.isNotBlank() } &&
        q.options.values.map { it.trim().lowercase() }.distinct().size == 3 &&
        q.correctKey in keys && q.explanation.isNotBlank() && q.locale.isNotBlank() &&
        q.author.isNotBlank() && q.rightsStatus.isNotBlank() && q.duplicateHash.isNotBlank() &&
        (q.format !in mediaFormats || when (q.format) {
            "visual" -> !q.altText.isNullOrBlank()
            "audio" -> !q.transcript.isNullOrBlank()
            else -> true
        })
    fun publishable(q: MintGrammarEntry): Boolean =
        structurallyValid(q) && q.approval == MintGrammarApproval.APPROVED &&
        !q.reviewerId.isNullOrBlank() && !q.approvedAt.isNullOrBlank() &&
        q.rightsStatus == "cleared"
    fun validateBank(bank: List<MintGrammarEntry>): Boolean =
        bank.map { it.id }.distinct().size == bank.size &&
        bank.map { it.duplicateHash }.distinct().size == bank.size &&
        bank.all(::publishable)
}
