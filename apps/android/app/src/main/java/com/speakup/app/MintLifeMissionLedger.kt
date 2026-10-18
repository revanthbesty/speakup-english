package com.speakup.app

/**
 * Local, privacy-minimal daily mission ledger. Completion is recorded by a verified
 * authored activity transition, never merely by opening a screen or tapping a reward.
 */
internal object MintLifeMissionLedger {
    const val storageKey = "mint_life_daily_completion_v1"
    const val cafeMissionId = "life-cafe-conversation-v1"
    val supportedMissionIds: Set<String> = setOf(cafeMissionId)

    fun key(date: String, missionId: String): String = "$date|$missionId"

    fun completedToday(records: Set<String>, date: String, missionId: String): Boolean =
        missionId in supportedMissionIds && key(date, missionId) in records

    fun recordVerified(
        existing: Set<String>, date: String, missionId: String, verified: Boolean
    ): Set<String> {
        if (!verified || missionId !in supportedMissionIds || !isValidDate(date)) return existing
        // Keep only the most recent 60 valid evidence keys to bound on-device storage.
        return (existing.filter { entry ->
            val parts = entry.split('|')
            parts.size == 2 && isValidDate(parts[0]) && parts[1] in supportedMissionIds
        }.toSet() + key(date, missionId)).sortedDescending().take(60).toSet()
    }

    fun completedMissionCount(records: Set<String>, date: String): Int =
        supportedMissionIds.count { completedToday(records, date, it) }

    private fun isValidDate(date: String): Boolean =
        runCatching { java.time.LocalDate.parse(date).toString() == date }.getOrDefault(false)
}
