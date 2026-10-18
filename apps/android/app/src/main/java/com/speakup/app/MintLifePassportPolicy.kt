package com.speakup.app
internal data class MintPassportDraft(val id:String,val promptVersion:Int,val consent:Boolean,val localUri:String?,val transcript:String?,val confidence:Int?,val deleted:Boolean)
internal object MintLifePassportPolicy {
 fun retain(d:MintPassportDraft)=d.id.isNotBlank()&&d.promptVersion>0&&d.consent&&!d.deleted&&d.confidence?.let{it in 1..5}!=false&&(d.localUri==null||d.localUri.startsWith("file:"))
 fun redact(d:MintPassportDraft)=d.copy(localUri=null,transcript=null,confidence=null,deleted=true)
}
