package com.speakup.app

/** Fail-closed local-only AI dispatch. No HTTP, SDK or cloud providers are allowed. */
internal enum class MintAiMode { OFF, LITE, LOCAL_OPTIONAL }
internal sealed class MintAiResponse {
 data class Local(val text:String):MintAiResponse()
 data class Fallback(val text:String):MintAiResponse()
}
internal interface MintLocalAiProvider {
 fun available():Boolean
 fun reply(input:String):String?
}
internal object MintAiFeatureRouter {
 fun reply(mode:MintAiMode,input:String,local:MintLocalAiProvider?=null):MintAiResponse {
  val sanitized=input.trim().take(400)
  if(mode==MintAiMode.LOCAL_OPTIONAL && local?.available()==true) {
   val result=runCatching { local.reply(sanitized) }.getOrNull()?.trim()?.take(500)
   if(!result.isNullOrBlank())return MintAiResponse.Local(result)
  }
  return MintAiResponse.Fallback(if(sanitized.isEmpty())"Choose a topic to practise English." else "Try saying that again using a short, clear sentence.")
 }
}
