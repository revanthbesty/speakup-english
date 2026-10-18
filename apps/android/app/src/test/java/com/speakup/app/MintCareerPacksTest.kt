package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintCareerPacksTest {
    @Test fun allOriginalQuestionsValidAndDistinct() {
        assertEquals(6,MintCareerPacks.questions.size)
        assertTrue(MintCareerPacks.questions.all(MintCareerPacks::valid))
        assertEquals(6,MintCareerPacks.questions.map { it.id }.distinct().size)
        assertEquals(setOf("Interview","Travel","Unofficial Exam Practice"),MintCareerPacks.questions.map { it.track }.toSet())
    }
    @Test fun correctChoiceUsesStableKey() {
        val q=MintCareerPacks.questions.first()
        assertTrue(MintCareerPacks.result(q,0))
        assertFalse(MintCareerPacks.result(q,1))
        assertEquals(MintCareerPacks.key(q),MintCareerPacks.key(q))
    }
}
