package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MintPetCatalogTest {
    @Test fun catalogProvidesSixUniqueStablePetIds() {
        assertEquals(6, MintPetCatalog.pets.size)
        assertEquals(6, MintPetCatalog.pets.map { it.id }.distinct().size)
        assertEquals(listOf("mint_fox", "buddy_panda", "spark_cat",
            "wise_owl", "joy_rabbit", "tiny_dragon"), MintPetCatalog.pets.map { it.id })
    }

    @Test fun unknownPetIsSafelyClearedAndNoPetIsAllowed() {
        assertEquals("", MintPetCatalog.normalized("not_a_pet"))
        assertEquals("", MintPetCatalog.normalized(null))
        assertNull(MintPetCatalog.find(""))
        assertEquals("Mint Fox", MintPetCatalog.find("mint_fox")?.name)
    }
}
