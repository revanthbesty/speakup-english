package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintAiReleaseReviewTest {
 @Test fun emptyEvidenceCannotShip(){assertFalse(MintAiReleaseReview.ready(emptyList()))}
 @Test fun duplicateAndEmptyReferencesNeverApprove(){
  val a=MintAiReleaseProof(MintAiReleaseRequirement.OWNER_APPROVAL,true,"")
  assertTrue(MintAiReleaseRequirement.OWNER_APPROVAL in MintAiReleaseReview.missing(listOf(a)))
 }
 @Test fun explicitlyEvidenceBackedFullApprovalIsRequired(){
  val all=MintAiReleaseRequirement.entries.map{MintAiReleaseProof(it,true,"verified-${it.name}")}
  assertTrue(MintAiReleaseReview.ready(all))
  assertFalse(MintAiReleaseReview.ready(all.dropLast(1)))
 }
}
