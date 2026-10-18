package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLocalModelGateTest {
 @Test fun refusesAbsentUnlicensedAndUntestedModels(){
  assertFalse(MintLocalModelGate.ready(null))
  val m=MintLocalModelEvidence("small","license","a".repeat(64),1024,false,true)
  assertFalse(MintLocalModelGate.ready(m))
  assertFalse(MintLocalModelGate.ready(m.copy(approved=true,verifiedDevice=false)))
  assertTrue(MintLocalModelGate.ready(m.copy(approved=true)))
 }
}
