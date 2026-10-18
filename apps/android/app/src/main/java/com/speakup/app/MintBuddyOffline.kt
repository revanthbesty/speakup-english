package com.speakup.app

/** Child-conscious scripted Mint Buddy, usable with airplane mode and no model. */
internal enum class MintBuddyLevel { EASY, MEDIUM, HARD }
internal object MintBuddyOffline {
 fun response(level:MintBuddyLevel, message:String):String {
  val text=message.trim().take(240)
  if(text.isBlank())return "Hello! Tell me one thing you like."
  return when(level) {
   MintBuddyLevel.EASY -> "Good practice! Can you say one more simple sentence?"
   MintBuddyLevel.MEDIUM -> "Thanks for sharing. What happened next? Try using a complete sentence."
   MintBuddyLevel.HARD -> "Can you explain your reason and give a specific example?"
  }
 }
}
