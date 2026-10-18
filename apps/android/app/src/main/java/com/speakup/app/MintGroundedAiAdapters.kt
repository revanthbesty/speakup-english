package com.speakup.app

/** Suggestions must not modify the reviewed source, answer, grades or rewards. */
internal data class MintGroundedFeedback(val itemId:String,val fixedAnswer:String,val rule:String,val suggestion:String)
internal object MintGroundedAiAdapters {
 fun grammar(itemId:String,answer:String,explanation:String):MintGroundedFeedback =
  MintGroundedFeedback(itemId,answer,explanation,
   if(explanation.isBlank())"Review the question and try again." else explanation)
 fun previewPrompt(domain:String):String=when(domain) {
  "Story" -> "Retell the story using your own words."
  "Professional" -> "Try a clear, respectful workplace response."
  else -> "Practise one short English sentence."
 }
}
