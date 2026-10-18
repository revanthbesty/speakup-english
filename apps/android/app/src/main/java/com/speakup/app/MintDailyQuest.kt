package com.speakup.app

/**
 * Deterministic and finite daily suggestion, with no server clock, reward pressure
 * or streak penalty. It prefers unmastered age-appropriate quests.
 */
internal object MintDailyQuest {
    fun select(
        difficulty: MintDifficulty,
        learnerAge: Int,
        epochDay: Long,
        awardedIds: Set<String>
    ): MintGameDefinition? {
        val eligible = MintGameCatalog.listFor(difficulty, learnerAge)
            .sortedBy { it.id }
        if (eligible.isEmpty()) return null
        val start = ((epochDay % eligible.size + eligible.size) % eligible.size).toInt()
        val rotated = eligible.drop(start) + eligible.take(start)
        return rotated.firstOrNull { "mint_game_${it.id}_v${it.version}" !in awardedIds }
            ?: rotated.first()
    }
}
