package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintBusinessWritingTest {
 @Test fun originalExercisesHaveValidAnswerKeys() {
  assertEquals(4,MintBusinessWriting.items.size)
  assertTrue(MintBusinessWriting.items.all(MintBusinessWriting::valid))
  assertEquals(4,MintBusinessWriting.items.map{it.id}.distinct().size)
 }
 @Test fun bothWritingTopicsAreCovered() {
  assertEquals(setOf("Email","Business Writing"),MintBusinessWriting.items.map{it.topic}.toSet())
 }
}
