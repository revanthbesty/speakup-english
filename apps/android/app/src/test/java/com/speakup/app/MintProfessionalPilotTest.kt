package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintProfessionalPilotTest {
    @Test fun originalPilotHasValidUniqueItems() {
        assertEquals(4,MintProfessionalPilot.items.size)
        assertTrue(MintProfessionalPilot.items.all(MintProfessionalPilot::valid))
        assertEquals(4,MintProfessionalPilot.items.map { it.id }.distinct().size)
    }
    @Test fun eachItemHasOneIdentifiableCorrectChoice() {
        MintProfessionalPilot.items.forEach { item ->
            assertTrue(item.correctIndex in item.answers.indices)
            assertEquals(3,item.answers.size)
        }
    }
}
