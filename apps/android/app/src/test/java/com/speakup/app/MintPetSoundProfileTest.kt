package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Test

class MintPetSoundProfileTest {
    @Test fun companionsHaveDistinctOfflineSoundIdentities() {
        val ratios = MintPetCatalog.pets.map { MintPetSoundProfile.pitchRatio(it.id) }
        assertEquals(6, ratios.distinct().size)
    }

    @Test fun unknownCompanionUsesNeutralPitch() {
        assertEquals(1.0, MintPetSoundProfile.pitchRatio("unknown"), 0.0)
        assertEquals(1.0, MintPetSoundProfile.pitchRatio(""), 0.0)
    }
}
