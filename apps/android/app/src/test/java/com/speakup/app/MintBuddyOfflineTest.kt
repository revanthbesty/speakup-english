package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintBuddyOfflineTest {
 @Test fun scriptsAreDeterministicAndAvailableAtAllLevels(){
  MintBuddyLevel.entries.forEach{level->
   val a=MintBuddyOffline.response(level,"Hello")
   assertEquals(a,MintBuddyOffline.response(level,"Hello"))
   assertTrue(a.isNotBlank())
  }
 }
 @Test fun blankMessagePromptsWithoutPaidInference(){assertTrue(MintBuddyOffline.response(MintBuddyLevel.EASY," ").contains("Hello"))}
}
