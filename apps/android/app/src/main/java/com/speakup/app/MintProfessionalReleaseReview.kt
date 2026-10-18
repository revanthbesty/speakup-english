package com.speakup.app

/** Full Professional English production readiness needs independent evidence. */
internal enum class MintProfessionalReleaseRequirement {
 CONTENT_APPROVAL, COPYRIGHT_AND_PROVENANCE, ACCOUNT_ROLE_REVIEW,
 OFFLINE_CORE_AND_NO_PAID_AI, PRESERVED_LEARNER_PROGRESS,
 MIGRATIONS_AND_ROLLBACK, ACCESSIBILITY_ON_DEVICE,
 PRIVACY_AND_CHILD_SAFETY, CORE_NAVIGATION_AND_REWARDS,
 INSTRUMENTATION_API29_35, APK_SAME_SHA, EDITORIAL_CMS_BACKEND,
 OWNER_APPROVAL
}
internal data class MintProfessionalReleaseProof(
 val requirement:MintProfessionalReleaseRequirement,val accepted:Boolean,val reference:String
)
internal object MintProfessionalReleaseReview {
 fun missing(proofs:List<MintProfessionalReleaseProof>):Set<MintProfessionalReleaseRequirement> =
  MintProfessionalReleaseRequirement.entries.filterNot { gate ->
   proofs.count { it.requirement==gate && it.accepted && it.reference.isNotBlank() }==1
  }.toSet()
 fun mayRelease(proofs:List<MintProfessionalReleaseProof>):Boolean=missing(proofs).isEmpty()
}
