package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintProfessionalReviewTest {
 @Test fun countsIgnoreUnknownAndDuplicateIds(){
  val id=MintProfessionalPilot.items.first().id
  assertEquals(1,MintProfessionalReview.completed(setOf(id,id,"invalid")))
 }
 @Test fun missingReviewIsDeterministicAndNeverCreatesAwards(){
  val item=MintProfessionalReview.next(emptySet(),"business")
  assertNotNull(item)
  assertTrue(item in MintProfessionalReview.validIds)
  assertEquals(item,MintProfessionalReview.next(emptySet(),"business"))
 }
}
