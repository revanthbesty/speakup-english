package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarReleaseReadinessTest {
    @Test fun passingBuildAloneCannotApproveRelease() {
        val evidence = listOf(MintGrammarReleaseEvidence(MintGrammarReleaseGate.APK_SAME_COMMIT,true,"run-123"))
        assertFalse(MintGrammarReleaseReadiness.releaseAllowed(evidence))
        assertTrue(MintGrammarReleaseGate.OWNER_APPROVAL in MintGrammarReleaseReadiness.missing(evidence))
        assertTrue(MintGrammarReleaseGate.VERIFIED_CONTENT in MintGrammarReleaseReadiness.missing(evidence))
    }
    @Test fun approvalRequiresReferencesAndAllGates() {
        val evidence=MintGrammarReleaseGate.entries.map { MintGrammarReleaseEvidence(it,true,"") }
        assertFalse(MintGrammarReleaseReadiness.releaseAllowed(evidence))
        assertTrue(MintGrammarReleaseReadiness.releaseAllowed(MintGrammarReleaseGate.entries.map {
            MintGrammarReleaseEvidence(it,true,"verified-${it.name}")
        }))
    }
    @Test fun draftQuestionCountCannotCountAsApprovedCorpus() {
        assertFalse(MintGrammarReleaseReadiness.pilotContentApproved(0,18))
        assertFalse(MintGrammarReleaseReadiness.pilotContentApproved(18,9500))
        assertTrue(MintGrammarReleaseReadiness.pilotContentApproved(18,18))
    }
}
