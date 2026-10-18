package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintSpeakingPassportTest {
    @Test fun validReflectionsRoundTripWithoutRawVoice() {
        val n = MintSpeakingPassport.safeNote("week", "I can introduce myself politely.", 123L)!!
        assertEquals(n, MintSpeakingPassport.decode(MintSpeakingPassport.encode(n)))
        assertEquals(3, MintSpeakingPassport.prompts().size)
    }
    @Test fun invalidOrExcessiveNotesAreRejected() {
        assertNull(MintSpeakingPassport.safeNote("unknown", "hello", 1))
        assertNull(MintSpeakingPassport.safeNote("month", "", 1))
        assertNull(MintSpeakingPassport.safeNote("month", "a".repeat(241), 1))
        assertNull(MintSpeakingPassport.decode("malformed"))
        assertNull(MintSpeakingPassport.decode("week|not-date|something"))
    }
    @Test fun deletedRowsDisappearFromHistory() {
        val row = MintSpeakingPassport.encode(MintSpeakingPassport.safeNote("quarter","More confident writing.",2)!!)
        assertEquals(1, MintSpeakingPassport.newestNotes(setOf(row)).size)
        assertTrue(MintSpeakingPassport.newestNotes(emptySet()).isEmpty())
    }
}
