package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarUiContractTest {
    @Test fun rendersAccessibleABCOptions() {
        assertEquals("A. She goes.", MintGrammarUiContract.optionLabel(0, "She goes."))
        assertEquals("B. She go.", MintGrammarUiContract.optionLabel(1, "She go."))
        assertEquals("C. She going.", MintGrammarUiContract.optionLabel(2, "She going."))
    }

    @Test fun rejectsUnvalidatedQuestionShapes() {
        assertTrue(MintGrammarUiContract.isPlayable(listOf("a", "b", "c"), "b"))
        assertFalse(MintGrammarUiContract.isPlayable(listOf("a", "b"), "a"))
        assertFalse(MintGrammarUiContract.isPlayable(listOf("a", "a", "b"), "a"))
        assertFalse(MintGrammarUiContract.isPlayable(listOf("a", "b", "c"), "d"))
        assertFalse(MintGrammarUiContract.isPlayable(listOf("a", "", "c"), "a"))
    }
}
