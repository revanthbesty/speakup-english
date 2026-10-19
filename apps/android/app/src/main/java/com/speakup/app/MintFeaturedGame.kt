package com.speakup.app

/** Featured action must select a real game, never the Mint Quests hub sentinel. */
internal object MintFeaturedGame {
    const val mode = "Word Builder"
    const val hubMode = "Mint Quests"
    fun isPlayable(mode: String): Boolean = mode != hubMode && mode in MintGamesRecommendations.modes
}
