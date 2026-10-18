package com.speakup.app

/** Solo-device, non-networked assignment preview. No child accounts or third-party progress access. */
internal data class MintFamilyAssignment(
    val id: String, val title: String, val route: String, val gameMode: String?,
    val assignedBy: String, val completed: Boolean = false
)
internal object MintFamilyPilot {
    val templates = listOf(
        MintFamilyAssignment("family-words", "Practise word building", "Games", "Word Builder", "Caregiver"),
        MintFamilyAssignment("teacher-reading", "Try a reading challenge", "Games", "Reading", "Teacher"),
        MintFamilyAssignment("family-speaking", "Practise a guided conversation", "Speak", null, "Caregiver")
    )
    fun valid(a: MintFamilyAssignment) = a.id.isNotBlank() && a.title.isNotBlank() &&
        a.route in setOf("Games", "Speak") && a.assignedBy in setOf("Caregiver","Teacher") &&
        (a.route != "Games" || a.gameMode in setOf("Word Builder","Reading"))
    fun markDone(a: MintFamilyAssignment, evidenceFromCompletedActivity: Boolean): MintFamilyAssignment =
        if (evidenceFromCompletedActivity) a.copy(completed = true) else a
    fun mayViewOtherLearnerProgress(authenticatedGuardian: Boolean, verifiedRelationship: Boolean): Boolean =
        false // Backend permissions, verified identity and consent do not yet exist.
}
