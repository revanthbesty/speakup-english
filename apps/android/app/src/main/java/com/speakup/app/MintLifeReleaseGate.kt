package com.speakup.app
internal enum class MintLifeReleaseRequirement { ZERO_PAID_AI, DATA_MIGRATION, OFFLINE_EGRESS, VERIFIED_CONTENT, CHILD_PRIVACY, LOCAL_RECORDING_DELETE, ACCESSIBILITY_DEVICE, API_29_35, APK_SAME_SHA, ROLLBACK, OWNER_APPROVAL }
internal data class MintLifeReleaseProof(val gate:MintLifeReleaseRequirement,val passed:Boolean,val reference:String)
internal object MintLifeReleaseGate {
 fun missing(items:List<MintLifeReleaseProof>)=MintLifeReleaseRequirement.entries.filterNot{g->items.count{it.gate==g&&it.passed&&it.reference.isNotBlank()}==1}.toSet()
 fun approved(items:List<MintLifeReleaseProof>)=missing(items).isEmpty()
}
