package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintProfessionalPublishingTest {
 @Test fun unreviewedContentNeverPublishes(){
  val draft=MintProfessionalEditorialRecord("business-status",1,"original","","","",false)
  assertFalse(MintProfessionalPublishing.approved(draft))
  assertFalse(MintProfessionalPublishing.publishable(listOf(draft),setOf(draft.itemId)))
 }
 @Test fun allItemApprovalsMustBeTraceableAndUnique(){
  val a=MintProfessionalEditorialRecord("q1",1,"original","rights-1","editor","approval-1",true)
  assertTrue(MintProfessionalPublishing.publishable(listOf(a),setOf("q1")))
  assertFalse(MintProfessionalPublishing.publishable(listOf(a,a),setOf("q1")))
  assertFalse(MintProfessionalPublishing.publishable(listOf(a),setOf("q1","q2")))
 }
}
