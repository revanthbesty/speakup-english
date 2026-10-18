package com.speakup.app

/** Capability-only speech policy; the OS engine may require downloads or network. */
internal enum class MintSpeechCapability { UNAVAILABLE, UNVERIFIED_SYSTEM, VERIFIED_OFFLINE }
internal data class MintSpeechRequest(val consent:Boolean,val capability:MintSpeechCapability,val networkAllowed:Boolean)
internal object MintSpeechPrivacy {
 fun permitOfflineSpeech(request:MintSpeechRequest):Boolean =
  request.consent && request.capability==MintSpeechCapability.VERIFIED_OFFLINE && !request.networkAllowed
 fun fallback(request:MintSpeechRequest):String=if(!request.consent)
  "Use the text transcript; microphone permission is optional."
 else "Use transcript or typed practice until offline speech capability is verified."
}
