package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintReleaseGateTest {
    private val verified = MintReleaseEvidence(true, true, true, true, true, true, true, true)

    @Test fun allReleaseChecksMustBeVerified() {
        assertTrue(MintReleaseGate.canRequestProductionRelease(verified))
        assertFalse(MintReleaseGate.canRequestProductionRelease(verified.copy(ownerApproved = false)))
        assertFalse(MintReleaseGate.canRequestProductionRelease(verified.copy(stackReconciled = false)))
        assertFalse(MintReleaseGate.canRequestProductionRelease(verified.copy(existingDataMigrationVerified = false)))
        assertFalse(MintReleaseGate.canRequestProductionRelease(verified.copy(allRoutesDeviceVerified = false)))
        assertFalse(MintReleaseGate.canRequestProductionRelease(verified.copy(accessibilityDeviceVerified = false)))
    }

    @Test fun ciAloneDoesNotMeanReady() {
        val ciOnly = MintReleaseEvidence(true, true, true, false, false, false, false, false)
        assertFalse(MintReleaseGate.canRequestProductionRelease(ciOnly))
    }
}
