package com.speakup.app

/** Fail-closed, evidence-based staged beta gate. A successful APK alone never authorizes launch. */
internal enum class MintBetaGate { CI, ANDROID_APK, ACCESSIBILITY_DEVICE, OFFLINE_DEVICE, PRIVACY_CONSENT, CHILD_SAFETY, CONTENT_REVIEW, DATA_MIGRATION, ROLLBACK_PLAN, OWNER_APPROVAL }
internal data class MintBetaEvidence(val gate: MintBetaGate, val verified: Boolean, val reference: String)
internal object MintBetaReadiness {
    val required: Set<MintBetaGate> = MintBetaGate.entries.toSet()
    fun missing(evidence: List<MintBetaEvidence>): Set<MintBetaGate> = required.filterNot { gate ->
        evidence.any { it.gate == gate && it.verified && it.reference.isNotBlank() }
    }.toSet()
    fun mayRelease(evidence: List<MintBetaEvidence>) = missing(evidence).isEmpty()
    fun previewStatus(): List<MintBetaEvidence> = listOf(
        MintBetaEvidence(MintBetaGate.CI, false, ""),
        MintBetaEvidence(MintBetaGate.ANDROID_APK, false, "")
    )
}
