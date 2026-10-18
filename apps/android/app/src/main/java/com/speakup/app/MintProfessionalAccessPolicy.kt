package com.speakup.app

/** Professional English preview policies. Core learning stays free and offline. */
internal enum class MintProfessionalFeature { CORE_PRACTICE, REVIEW, OPTIONAL_MEDIA }
internal enum class MintProfessionalAccess { AVAILABLE_OFFLINE, REQUIRES_OPTIONAL_DOWNLOAD }
internal object MintProfessionalAccessPolicy {
 fun access(feature:MintProfessionalFeature, hasEntitlement:Boolean, connected:Boolean):MintProfessionalAccess =
  if (feature==MintProfessionalFeature.OPTIONAL_MEDIA) MintProfessionalAccess.REQUIRES_OPTIONAL_DOWNLOAD
  else MintProfessionalAccess.AVAILABLE_OFFLINE
 fun canUseCoreOffline(networkAvailable:Boolean, hasEntitlement:Boolean):Boolean =
  access(MintProfessionalFeature.CORE_PRACTICE,hasEntitlement,networkAvailable)==MintProfessionalAccess.AVAILABLE_OFFLINE
}
