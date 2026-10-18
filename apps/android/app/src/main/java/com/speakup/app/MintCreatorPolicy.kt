package com.speakup.app

/** Local authoring prototype; no public publish or external asset upload. */
internal enum class MintCreatorStatus { DRAFT, RIGHTS_CHECKED, LANGUAGE_REVIEWED, SAFETY_REVIEWED, READY_FOR_SCHEDULING, RETIRED }
internal data class MintCreatorDraft(val id: String, val title: String, val script: String, val rightsNote: String, val status: MintCreatorStatus)
internal object MintCreatorPolicy {
    fun valid(d: MintCreatorDraft) = d.id.isNotBlank() && d.title.isNotBlank() && d.script.isNotBlank() &&
        d.title.length <= 100 && d.script.length <= 2000
    fun next(d: MintCreatorDraft, rightsConfirmed: Boolean, languageReviewed: Boolean, safetyReviewed: Boolean): MintCreatorDraft? {
        if (!valid(d)) return null
        val to = when(d.status) {
            MintCreatorStatus.DRAFT -> if (rightsConfirmed && d.rightsNote.isNotBlank()) MintCreatorStatus.RIGHTS_CHECKED else null
            MintCreatorStatus.RIGHTS_CHECKED -> if (languageReviewed) MintCreatorStatus.LANGUAGE_REVIEWED else null
            MintCreatorStatus.LANGUAGE_REVIEWED -> if (safetyReviewed) MintCreatorStatus.SAFETY_REVIEWED else null
            MintCreatorStatus.SAFETY_REVIEWED -> MintCreatorStatus.READY_FOR_SCHEDULING
            else -> null
        }
        return to?.let { d.copy(status = it) }
    }
    fun publicPublishingAllowed(d: MintCreatorDraft): Boolean = false
}
