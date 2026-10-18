package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintAiFeatureRouterTest {
 @Test fun offNeverInvokesProvider(){
  var calls=0
  val provider=object:MintLocalAiProvider{override fun available()=true;override fun reply(input:String):String?{calls++;return "local"}}
  assertTrue(MintAiFeatureRouter.reply(MintAiMode.OFF,"test",provider) is MintAiResponse.Fallback)
  assertEquals(0,calls)
 }
 @Test fun providerFailureUsesDeterministicFallback(){
  val provider=object:MintLocalAiProvider{override fun available()=true;override fun reply(input:String):String?=error("fail")}
  assertTrue(MintAiFeatureRouter.reply(MintAiMode.LOCAL_OPTIONAL,"test",provider) is MintAiResponse.Fallback)
 }
}
