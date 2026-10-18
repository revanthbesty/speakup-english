package com.speakup.app

/** A local, testable editorial gate. This model does not connect to a CMS or publish media. */
internal enum class MintEditorialStatus { DRAFT, REVIEWED, APPROVED, SCHEDULED, PUBLISHED, ARCHIVED }
internal data class MintRightsRecord(val owner: String, val license: String, val attribution: String)
internal data class MintEditorialItem(
    val id: String,
    val title: String,
    val text: String,
    val minAge: Int,
    val status: MintEditorialStatus,
    val rights: MintRightsRecord?,
    val reviewer: String?,
    val scheduledDate: String?,
    val version: Int
)
internal object MintEditorialPolicy {
    private val idPattern = Regex("[a-z0-9-]+")
    private val datePattern = Regex("\\d{4}-\\d{2}-\\d{2}")
    fun hasProvenance(item: MintEditorialItem): Boolean =
        item.rights?.let { it.owner.isNotBlank() && it.license.isNotBlank() && it.attribution.isNotBlank() } == true
    fun structurallyValid(item: MintEditorialItem): Boolean =
        item.id.matches(idPattern) && item.title.isNotBlank() && item.text.isNotBlank() &&
        item.minAge >= 4 && item.version > 0
    fun canTransition(from: MintEditorialStatus, to: MintEditorialStatus): Boolean = when (from) {
        MintEditorialStatus.DRAFT -> to == MintEditorialStatus.REVIEWED
        MintEditorialStatus.REVIEWED -> to == MintEditorialStatus.APPROVED || to == MintEditorialStatus.DRAFT
        MintEditorialStatus.APPROVED -> to == MintEditorialStatus.SCHEDULED || to == MintEditorialStatus.DRAFT
        MintEditorialStatus.SCHEDULED -> to == MintEditorialStatus.PUBLISHED || to == MintEditorialStatus.DRAFT
        MintEditorialStatus.PUBLISHED -> to == MintEditorialStatus.ARCHIVED
        MintEditorialStatus.ARCHIVED -> false
    }
    fun mayPublish(item: MintEditorialItem, onDate: String, catalog: List<MintEditorialItem>): Boolean =
        structurallyValid(item) && hasProvenance(item) &&
        item.status == MintEditorialStatus.SCHEDULED &&
        !item.reviewer.isNullOrBlank() && item.scheduledDate?.matches(datePattern) == true &&
        onDate.matches(datePattern) && item.scheduledDate <= onDate &&
        catalog.none { it.id == item.id && it !== item } &&
        catalog.none { it.id != item.id && it.text.trim().lowercase() == item.text.trim().lowercase() }
}
