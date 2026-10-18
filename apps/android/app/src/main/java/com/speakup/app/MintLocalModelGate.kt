package com.speakup.app

/** An unapproved or unverified local model can never be enabled. */
internal data class MintLocalModelEvidence(
 val modelId:String,val licenseReference:String,val sha256:String,val bytes:Long,
 val approved:Boolean,val verifiedDevice:Boolean
)
internal object MintLocalModelGate {
 fun ready(m:MintLocalModelEvidence?):Boolean=m!=null && m.modelId.isNotBlank() &&
  m.licenseReference.isNotBlank() && m.sha256.length==64 &&
  m.sha256.all { it in '0'..'9' || it in 'a'..'f' } &&
  m.bytes>0 && m.approved && m.verifiedDevice
}
