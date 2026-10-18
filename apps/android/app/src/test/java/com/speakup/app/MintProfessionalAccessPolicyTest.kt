package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintProfessionalAccessPolicyTest {
 @Test fun freeCoreNeverRequiresNetworkOrPayment(){
  for(online in listOf(false,true)) for(paid in listOf(false,true)) {
   assertTrue(MintProfessionalAccessPolicy.canUseCoreOffline(online,paid))
   assertEquals(MintProfessionalAccess.AVAILABLE_OFFLINE,
    MintProfessionalAccessPolicy.access(MintProfessionalFeature.REVIEW,paid,online))
  }
 }
 @Test fun optionalMediaIsNeverMisrepresentedAsCached(){
  assertEquals(MintProfessionalAccess.REQUIRES_OPTIONAL_DOWNLOAD,
    MintProfessionalAccessPolicy.access(MintProfessionalFeature.OPTIONAL_MEDIA,false,false))
 }
}
