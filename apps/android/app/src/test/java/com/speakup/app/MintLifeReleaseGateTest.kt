package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeReleaseGateTest {
 @Test fun defaultIsNoGo(){assertFalse(MintLifeReleaseGate.approved(emptyList()));assertTrue(MintLifeReleaseRequirement.OWNER_APPROVAL in MintLifeReleaseGate.missing(emptyList()))}
 @Test fun oneMissingGateFails(){val all=MintLifeReleaseRequirement.entries.map{MintLifeReleaseProof(it,true,"evidence")};assertTrue(MintLifeReleaseGate.approved(all));assertFalse(MintLifeReleaseGate.approved(all.dropLast(1)))}
}
