package com.speakup.app

/** Explicit, fail-closed Grammar Arena release gate. Does not initiate publication. */
internal enum class MintGrammarReleaseGate {
    VERIFIED_CONTENT, STABLE_IDS_AND_MIGRATION, UNIT_AND_INSTRUMENTATION,
    APK_SAME_COMMIT, ACCESSIBILITY_DEVICE, OFFLINE_DEVICE, PRIVACY_AND_REWARDS,
    INTEGRATION_REVIEW, OWNER_APPROVAL
}
internal data class MintGrammarReleaseEvidence(
    val gate: MintGrammarReleaseGate, val accepted: Boolean, val reference: String
)
internal object MintGrammarReleaseReadiness {
    val required = MintGrammarReleaseGate.entries.toSet()
    fun missing(evidence: List<MintGrammarReleaseEvidence>): Set<MintGrammarReleaseGate> =
        required.filterNot { gate ->
            evidence.any { it.gate == gate && it.accepted && it.reference.isNotBlank() }
        }.toSet()
    fun releaseAllowed(evidence: List<MintGrammarReleaseEvidence>): Boolean =
        missing(evidence).isEmpty()
    fun pilotContentApproved(approvedCount: Int, loadedCount: Int): Boolean =
        approvedCount > 0 && approvedCount == loadedCount
}
