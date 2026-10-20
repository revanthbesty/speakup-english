package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintV1ReleaseGateTest {
    @Test fun noEvidenceMeansNoGo() {
        val evidence = MintV1ReleaseGate.evaluate()
        assertFalse(evidence.canPublish)
        assertEquals(MintV1ReleaseCheck.entries.toSet(), evidence.missing)
    }

    @Test fun greenDebugBuildAloneCannotAuthorizePublicRelease() {
        val evidence = MintV1ReleaseGate.evaluate(
            setOf(MintV1ReleaseCheck.ANDROID_UNIT_LINT_BUILD)
        )
        assertFalse(evidence.canPublish)
        assertTrue(MintV1ReleaseCheck.API_29_INSTRUMENTATION in evidence.missing)
        assertTrue(MintV1ReleaseCheck.API_35_INSTRUMENTATION in evidence.missing)
        assertTrue(MintV1ReleaseCheck.EXPLICIT_OWNER_APPROVAL in evidence.missing)
    }

    @Test fun ownerApprovalAloneCannotBypassSafetyGates() {
        assertFalse(MintV1ReleaseGate.evaluate(
            setOf(MintV1ReleaseCheck.EXPLICIT_OWNER_APPROVAL)
        ).canPublish)
    }

    @Test fun everyRequiredEvidenceItemIsNecessary() {
        val all = MintV1ReleaseCheck.entries.toSet()
        for (check in all) {
            val evidence = MintV1ReleaseGate.evaluate(all - check)
            assertFalse("Missing $check must block release", evidence.canPublish)
            assertEquals(setOf(check), evidence.missing)
        }
        assertTrue(MintV1ReleaseGate.evaluate(all).canPublish)
    }
}
