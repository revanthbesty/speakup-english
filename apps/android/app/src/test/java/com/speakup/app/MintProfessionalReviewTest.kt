package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintProfessionalReviewTest {
 @Test fun countsIgnoreUnknownAndDuplicateIds(){
  val id=MintProfessionalPilot.items.first().id
  assertEquals(1,MintProfessionalReview.completed(setOf(id,id,"invalid")))
 }
 @Test fun onlyReviewedCorrectAnswersCanBePersistedOnce(){
  val id=MintInterviewPrep.items.first().id
  assertEquals(emptySet<String>(),MintProfessionalReview.recordVerified(emptySet(),id,false))
  assertEquals(emptySet<String>(),MintProfessionalReview.recordVerified(emptySet(),"unpublished",true))
  val saved=MintProfessionalReview.recordVerified(emptySet(),id,true)
  assertEquals(saved,MintProfessionalReview.recordVerified(saved,id,true))
  assertEquals(1,MintProfessionalReview.completed(saved))
 }
 @Test fun allProfessionalModulesUseDistinctIds(){
  val ids=MintProfessionalPilot.items.map{it.id}+MintInterviewPrep.items.map{it.id}+
   MintMeetingPractice.items.map{it.id}+MintBusinessWriting.items.map{it.id}+
   MintNegotiationPilot.items.map{it.id}+MintWorkplaceRoleplays.samples.map{it.id}
  assertEquals(ids.size,ids.toSet().size)
  assertEquals(ids.toSet(),MintProfessionalReview.validIds)
 }
 @Test fun missingReviewIsDeterministicAndNeverCreatesAwards(){
  val item=MintProfessionalReview.next(emptySet(),"business")
  assertNotNull(item)
  assertTrue(item in MintProfessionalReview.validIds)
  assertEquals(item,MintProfessionalReview.next(emptySet(),"business"))
 }
}
