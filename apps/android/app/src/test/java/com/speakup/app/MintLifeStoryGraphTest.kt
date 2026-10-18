package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeStoryGraphTest {
 @Test fun noDanglingChoices(){assertTrue(MintLifeStoryGraph.valid(MintLifeStoryGraph.pilot))}
 @Test fun missingTargetIsRejected(){assertFalse(MintLifeStoryGraph.valid(MintLifeStoryGraph.pilot+MintLifeStoryNode("broken","x",listOf(MintLifeStoryChoice("oops","missing")))))}
}
