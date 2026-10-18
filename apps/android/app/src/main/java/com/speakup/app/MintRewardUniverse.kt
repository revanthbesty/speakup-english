package com.speakup.app

/** Display-only collectible identities derived from existing, once-only completion ledgers. */
internal enum class MintCollectible(val title: String, val symbol: String) {
    BLOOM("Mint Bloom", "🌸"),
    WORD("Word Gem", "💎"),
    STORY("Story Flower", "🌺"),
    VOICE("Voice Bloom", "🎤"),
    SOUND("Sound Star", "🎵"),
    QUEST("Quest Emblem", "🏅"),
    PUZZLE("Puzzle Bloom", "🧩")
}
internal data class MintGardenCounts(
    val blooms: Int, val words: Int, val stories: Int, val sounds: Int,
    val quests: Int, val puzzles: Int, val voices: Int
) {
    val total: Int get() = blooms + words + stories + sounds + quests + puzzles + voices
}
internal object MintRewardUniverse {
    /** Never infer voice mastery from recognition attempts, or awards from XP totals alone. */
    fun fromLegacy(
        lessons: Set<String>, grammar: Set<String>, riddles: Set<String>,
        vocabulary: Set<String>, reading: Set<String>, listening: Set<String>,
        scenarios: Set<String>, puzzles: Set<String>, quests: Set<String>,
        voicesVerified: Set<String> = emptySet()
    ) = MintGardenCounts(
        blooms = lessons.size + grammar.size + riddles.size + scenarios.size,
        words = vocabulary.size,
        stories = reading.size,
        sounds = listening.size,
        quests = quests.size,
        puzzles = puzzles.size,
        voices = voicesVerified.size
    )
    fun displayName(activity: String): MintCollectible = when (activity) {
        "Vocabulary" -> MintCollectible.WORD
        "Reading", "Story" -> MintCollectible.STORY
        "Listening" -> MintCollectible.SOUND
        "Speaking" -> MintCollectible.VOICE
        "Mint Quests" -> MintCollectible.QUEST
        "Word Builder" -> MintCollectible.PUZZLE
        else -> MintCollectible.BLOOM
    }
    fun isNewReward(newXp: Int) = newXp > 0
}
