package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintMeetingPracticeTest {
 @Test fun fourItemsAreValidOriginalPractices() {
  assertEquals(4,MintMeetingPractice.items.size)
  assertEquals(4,MintMeetingPractice.items.map{it.id}.distinct().size)
  assertTrue(MintMeetingPractice.items.all(MintMeetingPractice::valid))
  assertEquals(setOf("Meeting","Presentation"),MintMeetingPractice.items.map{it.category}.toSet())
 }
 @Test fun eachItemHasOneAnswerAndExplanation() {
  MintMeetingPractice.items.forEach{assertTrue(it.correct in it.options.indices);assertTrue(it.explanation.isNotBlank())}
 }
}
