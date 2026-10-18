package com.speakup.app

/** Conservative UX for unsupported or low-memory local AI devices. */
internal data class MintAiDeviceEvidence(
 val measuredRamMb:Int?,val modelMemoryMb:Int?,val thermalSafe:Boolean?,
 val captionsAvailable:Boolean,val transcriptFallback:Boolean
)
internal object MintAiLitePolicy {
 fun localInferenceAllowed(e:MintAiDeviceEvidence):Boolean =
  e.measuredRamMb!=null && e.modelMemoryMb!=null &&
  e.measuredRamMb>=e.modelMemoryMb*2 &&
  e.thermalSafe==true && e.captionsAvailable && e.transcriptFallback
 fun mode(e:MintAiDeviceEvidence):MintAiMode =
  if(localInferenceAllowed(e))MintAiMode.LOCAL_OPTIONAL else MintAiMode.LITE
}
