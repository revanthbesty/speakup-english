package com.speakup.app

/**
 * Conservative release criteria independent of CI success.
 * This code never triggers publishing or billing.
 */
internal data class MintReleaseEvidence(
    val androidBuildPassed: Boolean,
    val unitTestsPassed: Boolean,
    val lintPassed: Boolean,
    val existingDataMigrationVerified: Boolean,
    val accessibilityDeviceVerified: Boolean,
    val allRoutesDeviceVerified: Boolean,
    val ownerApproved: Boolean,
    val stackReconciled: Boolean
)
internal object MintReleaseGate {
    fun canRequestProductionRelease(evidence: MintReleaseEvidence): Boolean =
        evidence.androidBuildPassed && evidence.unitTestsPassed && evidence.lintPassed &&
        evidence.existingDataMigrationVerified && evidence.accessibilityDeviceVerified &&
        evidence.allRoutesDeviceVerified && evidence.ownerApproved && evidence.stackReconciled
}
