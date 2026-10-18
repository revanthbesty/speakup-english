package com.speakup.app

/** Privacy-first Speaking Passport V1. Notes are locally stored only after explicit consent.
 * This prototype intentionally does NOT record, upload or compare raw microphone audio. */
internal data class MintPassportNote(
    val promptId: String,
    val selfReflection: String,
    val practiceDay: Long
)
internal object MintSpeakingPassport {
    private const val delimiter = "|"
    fun safeNote(promptId: String, reflection: String, day: Long): MintPassportNote? {
        val note = reflection.trim()
        if (promptId !in setOf("week", "month", "quarter") || note.isBlank() || note.length > 240 || day < 0) return null
        if (note.contains(delimiter) || note.contains("\n")) return null
        return MintPassportNote(promptId, note, day)
    }
    fun encode(note: MintPassportNote): String =
        "${note.promptId}$delimiter${note.practiceDay}$delimiter${note.selfReflection}"
    fun decode(raw: String): MintPassportNote? {
        val pieces = raw.split(delimiter, limit = 3)
        if (pieces.size != 3) return null
        return safeNote(pieces[0], pieces[2], pieces[1].toLongOrNull() ?: return null)
    }
    fun newestNotes(rows: Set<String>): List<MintPassportNote> =
        rows.mapNotNull(::decode).sortedByDescending { it.practiceDay }
    fun prompts() = mapOf(
        "week" to "7-day reflection: How would you politely introduce yourself?",
        "month" to "30-day reflection: Explain something you learned this month.",
        "quarter" to "90-day reflection: Describe a goal you are working toward."
    )
}
