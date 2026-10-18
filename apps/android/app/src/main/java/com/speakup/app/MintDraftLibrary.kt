package com.speakup.app

import android.content.Context
import org.json.JSONObject

/** Offline reader for the 288 unapproved replacement records staged with Sprint 26.
 *  This is editorial preview only: never marks completion, grants rewards, or assigns lessons.
 */
internal data class MintDraftRecord(
    val id: String, val category: String, val level: String,
    val question: String, val content: String, val explanation: String
)

internal object MintDraftLibrary {
    const val ASSET = "english_mint/draft_replacements_288.json"
    const val EXPECTED_COUNT = 288
    val categories = listOf("word_builder", "riddles", "vocabulary", "situations",
        "listening", "reading", "dialogues", "stories")

    fun read(context: Context): List<MintDraftRecord> {
        val root = JSONObject(context.assets.open(ASSET).bufferedReader().use { it.readText() })
        require(root.getString("status") == "needs_human_review") { "Unexpected approval state" }
        val items = root.getJSONArray("activities")
        require(items.length() == EXPECTED_COUNT) { "Unexpected content count" }
        val seen = mutableSetOf<String>()
        val records = (0 until items.length()).map { n ->
            val record = items.getJSONObject(n)
            val id = record.getString("activity_id")
            val cat = record.getString("category")
            val level = record.getString("cefr_level")
            require(seen.add(id)) { "Repeated activity ID" }
            require(cat in categories && level in listOf("A1", "A2", "B1", "B2", "C1"))
            require(record.getString("review_status") == "needs_human_review")
            MintDraftRecord(id, cat, level, record.getString("question"),
                record.getString("content"), record.getString("explanation"))
        }
        require(records.groupingBy { it.category }.eachCount().values.all { it == 36 })
        return records
    }
}
