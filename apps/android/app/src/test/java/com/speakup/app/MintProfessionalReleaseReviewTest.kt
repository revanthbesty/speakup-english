package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintProfessionalReleaseReviewTest {
 @Test fun successfulBuildCannotAuthorizeRelease(){
  val build=MintProfessionalReleaseProof(MintProfessionalReleaseRequirement.APK_SAME_SHA,true,"github-run")
  assertFalse(MintProfessionalReleaseReview.mayRelease(listOf(build)))
  assertTrue(MintProfessionalReleaseRequirement.OWNER_APPROVAL in MintProfessionalReleaseReview.missing(listOf(build)))
 }
 @Test fun blankReferencesAndDuplicateProofsCannotApprove(){
  val evidence=MintProfessionalReleaseRequirement.entries.map{MintProfessionalReleaseProof(it,true,"accepted-${it.name}")}
  assertTrue(MintProfessionalReleaseReview.mayRelease(evidence))
  assertFalse(MintProfessionalReleaseReview.mayRelease(evidence+evidence.first()))
  assertFalse(MintProfessionalReleaseReview.mayRelease(evidence.dropLast(1)))
 }
}
