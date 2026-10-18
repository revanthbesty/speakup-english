package com.speakup.app

import org.json.JSONObject

/** Parses authoring JSON without granting draft questions access to scored sessions. */
internal object MintGrammarJsonLoader {
    fun parseDraftBank(json: String): List<MintGrammarDraft> {
        val root = JSONObject(json)
        require(root.getInt("schema_version") == 1) { "Unsupported grammar schema" }
        val questions = root.getJSONArray("questions")
        require(questions.length() <= 10000) { "Question bank exceeds safety limit" }
        val output = ArrayList<MintGrammarDraft>(questions.length())
        for (index in 0 until questions.length()) {
            val q = questions.getJSONObject(index)
            val answerOptions = q.getJSONArray("options")
            val feedback = q.getJSONArray("explanations")
            require(answerOptions.length() == 3 && feedback.length() == 3) {
                "Question at index $index requires three options and three explanations"
            }
            val status = when (q.getString("status").lowercase()) {
                "draft" -> MintGrammarStatus.DRAFT
                "validated" -> MintGrammarStatus.VALIDATED
                "approved" -> MintGrammarStatus.APPROVED
                "published" -> MintGrammarStatus.PUBLISHED
                else -> MintGrammarStatus.REJECTED
            }
            val key = q.getString("correct_key")
            require(key.length == 1 && key[0] in 'A'..'C') { "Invalid correct answer" }
            val rights = q.optString("rights_status")
            output += MintGrammarDraft(
                id = q.getString("id"),
                version = q.getInt("version"),
                topicId = q.getString("topic_id"),
                prompt = q.getString("prompt"),
                options = (0..2).map { answerOptions.getString(it) },
                correctKey = key[0],
                feedback = (0..2).map { feedback.getString(it) },
                grammarRule = q.getString("grammar_rule"),
                exampleSentence = q.getString("example_sentence"),
                status = status,
                reviewerId = q.optString("reviewer_id").takeIf { it.isNotBlank() && it != "null" },
                approvedAt = q.optString("approved_at").takeIf { it.isNotBlank() && it != "null" },
                rightsReviewed = rights == "original_text_reviewed" || rights == "licensed_reviewed"
            )
        }
        require(output.map { it.id }.distinct().size == output.size) { "Duplicate IDs" }
        return output
    }

    fun playableQuestions(json: String): List<MintGrammarDraft> =
        MintGrammarGate.publishableBatch(parseDraftBank(json))
}
