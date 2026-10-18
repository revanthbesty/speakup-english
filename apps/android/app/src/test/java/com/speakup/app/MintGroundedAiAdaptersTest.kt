package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintGroundedAiAdaptersTest {
 @Test fun grammarAnswerRemainsVerbatim(){
  val x=MintGroundedAiAdapters.grammar("id","goes","Use goes with she.")
  assertEquals("goes",x.fixedAnswer)
  assertEquals("Use goes with she.",x.rule)
 }
 @Test fun domainsHaveOfflineFallbacks(){assertTrue(MintGroundedAiAdapters.previewPrompt("Story").isNotBlank())}
}
