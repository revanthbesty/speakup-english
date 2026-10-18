package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintCoopStoryTest {
    @Test fun scriptValidAndTwoRolesAlternate() {
        assertTrue(MintCoopStory.valid())
        var turns=emptyList<MintStoryTurn>()
        assertEquals(turns,MintCoopStory.next(turns,1))
        for(i in 0..2) turns=MintCoopStory.next(turns,0)
        assertEquals(listOf("Explorer","Helper","Explorer"),turns.map { it.role })
        assertTrue(MintCoopStory.complete(turns))
    }
    @Test fun tamperedOrIncompleteProgressCannotMintReward() {
        assertFalse(MintCoopStory.complete(emptyList()))
        val turns=MintCoopStory.next(emptyList(),0)
        assertFalse(MintCoopStory.complete(turns))
        assertFalse(MintCoopStory.complete(turns + MintStoryTurn("Stranger","fake",1)))
        assertEquals(MintCoopStory.prompts.size,MintCoopStory.choices.size)
    }
}
