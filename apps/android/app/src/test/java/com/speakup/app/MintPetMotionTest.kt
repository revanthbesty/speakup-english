package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MintPetMotionTest {
    @Test fun allSixPetsHaveDistinctPoses() {
        val celebration = MintPetCatalog.pets.map { MintPetMotion.celebrationRotation(it.id) }
        val encouragement = MintPetCatalog.pets.map { MintPetMotion.encouragementRotation(it.id) }
        assertEquals(6, celebration.distinct().size)
        assertEquals(6, encouragement.distinct().size)
        assertTrue(celebration.all { it in -20f..20f })
        assertTrue(encouragement.all { it in -20f..20f })
    }

    @Test fun unknownPetStaysNeutral() {
        assertEquals(0f, MintPetMotion.celebrationRotation("unknown"))
        assertEquals(0f, MintPetMotion.encouragementRotation("unknown"))
    }
}
