package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintWorkplaceRoleplaysTest {
    @Test fun roleplaysHaveValidKeysAndThreeTurns() {
        assertEquals(2,MintWorkplaceRoleplays.samples.size)
        assertTrue(MintWorkplaceRoleplays.samples.all(MintWorkplaceRoleplays::valid))
        assertEquals(2,MintWorkplaceRoleplays.samples.map { it.id }.distinct().size)
    }
    @Test fun onlyCorrectAnswersAdvanceAndCompletionRequiresAllTurns() {
        val story=MintWorkplaceRoleplays.samples.first()
        assertEquals(0,MintWorkplaceRoleplays.advance(story,0,1))
        var index=0
        for(turn in story.turns) index=MintWorkplaceRoleplays.advance(story,index,turn.answer)
        assertTrue(MintWorkplaceRoleplays.finished(story,index))
        assertFalse(MintWorkplaceRoleplays.finished(story,1))
    }
}
