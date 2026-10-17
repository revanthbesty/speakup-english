package com.speakup.app

/**
 * English Mint ships as an in-place upgrade of SpeakUp English.
 * These identifiers are intentionally stable to preserve existing installs and local learning history.
 */
internal object MintIdentity {
    const val displayName = "English Mint"
    const val tagline = "Fresh Games. Fluent Voices."
    const val legacyPreferencesFile = "speakup_learning"
    const val legacyApplicationId = "com.speakup.app"

    val protectedPreferenceKeys = setOf(
        "learning_path", "learner_age", "daily_goal", "total_xp",
        "completed_lessons", "placement_done", "placement_score",
        "speaking_attempts", "puzzles_solved", "puzzle_awards",
        "revision_correct", "confidence_attempts", "independent_replies"
    )
}
