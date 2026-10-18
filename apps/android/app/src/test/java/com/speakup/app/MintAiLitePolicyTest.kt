package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintAiLitePolicyTest {
 @Test fun unknownDeviceAlwaysUsesLite(){assertEquals(MintAiMode.LITE,MintAiLitePolicy.mode(MintAiDeviceEvidence(null,null,null,true,true)))}
 @Test fun transcriptsAndSafetyAreRequired(){
  assertEquals(MintAiMode.LITE,MintAiLitePolicy.mode(MintAiDeviceEvidence(8192,1500,true,false,true)))
  assertEquals(MintAiMode.LOCAL_OPTIONAL,MintAiLitePolicy.mode(MintAiDeviceEvidence(8192,1500,true,true,true)))
 }
}
