package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintBetaReadinessTest {
    @Test fun missingApprovalBlocksReleaseEvenIfCiAndApkPass() {
        val evidence=listOf(
            MintBetaEvidence(MintBetaGate.CI,true,"run-ci"),
            MintBetaEvidence(MintBetaGate.ANDROID_APK,true,"run-apk")
        )
        assertFalse(MintBetaReadiness.mayRelease(evidence))
        assertTrue(MintBetaGate.OWNER_APPROVAL in MintBetaReadiness.missing(evidence))
        assertTrue(MintBetaGate.CHILD_SAFETY in MintBetaReadiness.missing(evidence))
    }
    @Test fun missingOrBlankEvidenceFailsClosed() {
        assertFalse(MintBetaReadiness.mayRelease(emptyList()))
        assertFalse(MintBetaReadiness.mayRelease(MintBetaGate.entries.map { MintBetaEvidence(it,true,"") }))
    }
    @Test fun verifiedEvidenceForEveryGateIsNecessary() {
        val evidence=MintBetaGate.entries.map { MintBetaEvidence(it,true,"documented-${it.name}") }
        assertTrue(MintBetaReadiness.mayRelease(evidence))
        assertTrue(MintBetaReadiness.missing(evidence).isEmpty())
    }
}
