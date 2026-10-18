package com.speakup.app

/**
 * In-place storage compatibility and controlled export policy.
 * No account or cloud synchronization is implied by a local preferences snapshot.
 */
internal object MintDataProtection {
    const val schemaVersion = 1

    private val fixedKeys = setOf(
        "learning_path", "learner_age", "mint_age_band", "learning_difficulty", "learning_goal",
        "daily_goal", "profile_name", "total_xp", "completed_lessons",
        "placement_done", "placement_score", "placement_level", "placement_attempt",
        "puzzles_solved", "puzzle_awards", "riddle_awards", "mint_game_awards",
        "mint_guided_voice_turns", "speaking_attempts", "revision_correct",
        "confidence_attempts", "independent_replies", "reading_mastered",
        "listening_mastered", "scenario_mastered", "dialogue_mastered",
        "vocabulary_mastered", "grammar_mastered", "skill_practice_mastered",
        "cafe_lesson_complete", "mint_story_completed", "mint_hidden_clips",
        "mint_private_reactions", "mint_local_clip_reports", "mint_theme_mode",
        "mint_eye_comfort", "mint_reduce_motion", "dark_appearance"
    )
    private val stageRegex = Regex("revision_(due|stage)_[0-9]+")

    val privateInteractionKeys = setOf(
        "mint_hidden_clips", "mint_private_reactions", "mint_local_clip_reports", "mint_story_completed"
    )

    fun exportableKey(key: String): Boolean = key in fixedKeys || stageRegex.matches(key)

    /** Filters unknown data by default; no raw recordings, passwords or tokens. */
    fun safeSnapshot(all: Map<String, *>): Map<String, Any> =
        all.filterKeys(::exportableKey).mapNotNull { (key, value) ->
            val safe = when (value) {
                is String, is Boolean, is Int, is Long -> value
                is Set<*> -> value.filterIsInstance<String>().sorted().take(1000)
                else -> null
            }
            if (safe == null) null else key to safe
        }.toMap()

    /** A conservative warning list, not automatic mutation of legacy keys. */
    fun audit(all: Map<String, *>): List<String> {
        val warnings = mutableListOf<String>()
        val xp = all["total_xp"]
        if (xp !is Int || xp < 0) warnings.add("XP is missing or invalid; do not overwrite it automatically.")
        if (all.containsKey("learning_path") && all["learning_path"] !is String) {
            warnings.add("Learning path is invalid; keep the legacy value for review.")
        }
        if (all.containsKey("completed_lessons") && all["completed_lessons"] !is Set<*>) {
            warnings.add("Completed lesson records may need manual review.")
        }
        return warnings
    }

    fun encodeJson(data: Map<String, Any>): String {
        val fields = data.toSortedMap().map { (key, value) ->
            jsonString(key) + ":" + encodeValue(value)
        }
        return "{\"format\":\"english-mint-local-export\",\"schemaVersion\":$schemaVersion," +
            "\"data\":{"+fields.joinToString(",")+"}}"
    }

    private fun encodeValue(value: Any): String = when (value) {
        is String -> jsonString(value)
        is Boolean, is Int, is Long -> value.toString()
        is List<*> -> value.filterIsInstance<String>().joinToString(",", "[", "]") { jsonString(it) }
        else -> "null"
    }

    private fun jsonString(s: String): String = buildString {
        append('"')
        s.forEach { c ->
            when (c) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (c.code < 0x20) append("\\u%04x".format(c.code)) else append(c)
            }
        }
        append('"')
    }
}
