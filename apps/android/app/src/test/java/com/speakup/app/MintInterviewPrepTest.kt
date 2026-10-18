package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintInterviewPrepTest {
 @Test fun interviewItemsHaveValidDistinctKeys() {
  assertEquals(4,MintInterviewPrep.items.size)
  assertEquals(4,MintInterviewPrep.items.map { it.id }.distinct().size)
  assertTrue(MintInterviewPrep.items.all(MintInterviewPrep::valid))
 }
 @Test fun everyAnswerHasAnExplanationAndFollowUp() {
  MintInterviewPrep.items.forEach { item ->
   assertEquals(3,item.choices.size)
   assertTrue(item.best in item.choices.indices)
   assertTrue(item.followUp.isNotBlank())
  }
 }
}
