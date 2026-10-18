package com.speakup.app

import org.json.JSONObject

/** Android adapter for the versioned 100-quest JSON export. No network or paid APIs. */
internal object MintRealWorldQuestJson {
    fun parse(json: String): List<MintRealWorldQuestRecord> {
        val root = JSONObject(json)
        require(root.optString("schema_version") == "1.0") { "Unsupported quest schema" }
        val data = root.getJSONArray("quests")
        val result = (0 until data.length()).map { index ->
            val q = data.getJSONObject(index)
            MintRealWorldQuestRecord(
                id = q.getString("quest_id"),
                category = q.getString("category_id"),
                difficulty = q.getString("difficulty"),
                title = q.getString("quest_title"),
                question = q.getString("question_prompt"),
                choices = listOf(q.getString("option_a"), q.getString("option_b"), q.getString("option_c")),
                correctAnswer = q.getString("correct_answer"),
                speakingPrompt = q.getString("speaking_prompt"),
                status = q.getString("content_status")
            )
        }
        require(MintRealWorldQuestGate.readyForImport(result)) { "Incomplete or malformed 100-quest catalog" }
        return result
    }

    /** Drafts are retained for editorial work but never exposed to production learners. */
    fun published(json: String): List<MintRealWorldQuestRecord> =
        parse(json).filter(MintRealWorldQuestGate::publishable)
}
