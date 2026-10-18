package com.speakup.app

/** UI and safety defaults; proficiency always remains independent of age. */
internal object MintExperiencePolicy {
    val ageBands = listOf("4–7", "8–12", "13–17", "18–49", "50+")
    val appearanceModes = listOf("system", "light", "dark", "night")
    val journeys = mapOf(
        "zero" to "English Explorer",
        "basic" to "Confidence Builder",
        "speaking" to "Speaking Accelerator",
        "advanced" to "English Pro",
        "exams" to "Exam Champion"
    )

    fun legacyAge(band: String): String = when (band) {
        "4–7", "8–12" -> "Kids"
        "13–17" -> "Teen"
        else -> "Adult"
    }
    fun initialAgeBand(legacy: String): String = when (legacy) {
        "Kids" -> "8–12"
        "Teen" -> "13–17"
        else -> "18–49"
    }
    fun childSafetyEnabled(band: String): Boolean = band in listOf("4–7", "8–12", "13–17")
    fun selectedJourney(path: String): String = journeys[path] ?: journeys.getValue("zero")
    fun nightModeEnabled(mode: String, systemDark: Boolean, hour: Int): Boolean = when(mode) {
        "dark" -> true
        "light" -> false
        "night" -> hour >= 20 || hour < 6
        else -> systemDark
    }
    fun normalizedMode(mode: String): String = mode.takeIf { it in appearanceModes } ?: "system"
    fun reducedMotion(preference: Boolean, systemAnimationsOff: Boolean) =
        preference || systemAnimationsOff
}
