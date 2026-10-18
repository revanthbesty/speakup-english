package com.speakup.app

/** Manual-evidence release decision. A green workflow alone never enables AI. */
internal enum class MintAiReleaseRequirement {
 NO_PAID_PROVIDER, ZERO_INFERENCE_EGRESS, LICENSE_APPROVAL, DEVICE_BENCHMARKS,
 OFFLINE_SPEECH_QA, CHILD_SAFETY, ACCESSIBILITY, MIGRATION_REGRESSION,
 APK_SAME_SHA, OWNER_APPROVAL
}
internal data class MintAiReleaseProof(val requirement:MintAiReleaseRequirement,val accepted:Boolean,val reference:String)
internal object MintAiReleaseReview {
 fun missing(proofs:List<MintAiReleaseProof>):Set<MintAiReleaseRequirement> =
  MintAiReleaseRequirement.entries.filterNot { gate ->
   proofs.count { it.requirement==gate && it.accepted && it.reference.isNotBlank() }==1
  }.toSet()
 fun ready(proofs:List<MintAiReleaseProof>):Boolean=missing(proofs).isEmpty()
}
