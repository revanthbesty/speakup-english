package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintLifeMissionLedgerTest {
    private val day = "2026-10-19"
    private val mission = MintLifeMissionLedger.cafeMissionId

    @Test fun unverifiedOrUnsupportedMissionNeverCompletes() {
        assertEquals(emptySet<String>(), MintLifeMissionLedger.recordVerified(emptySet(), day, mission, false))
        assertEquals(emptySet<String>(), MintLifeMissionLedger.recordVerified(emptySet(), day, "unpublished", true))
        assertEquals(emptySet<String>(), MintLifeMissionLedger.recordVerified(emptySet(), "not-a-date", mission, true))
    }

    @Test fun verifiedCafeCompletionIsOncePerDayAndDoesNotGrantCurrency() {
        val first = MintLifeMissionLedger.recordVerified(emptySet(), day, mission, true)
        val second = MintLifeMissionLedger.recordVerified(first, day, mission, true)
        assertEquals(first, second)
        assertTrue(MintLifeMissionLedger.completedToday(second, day, mission))
        assertEquals(1, MintLifeMissionLedger.completedMissionCount(second, day))
        assertEquals(0, MintLifeMissionLedger.completedMissionCount(second, "2026-10-20"))
    }

    @Test fun storageFiltersInvalidOrUnknownEntries() {
        val result = MintLifeMissionLedger.recordVerified(
            setOf("junk", "$day|unapproved-item", "2026-02-30|$mission"),
            day, mission, true
        )
        assertEquals(setOf("$day|$mission"), result)
    }
}
