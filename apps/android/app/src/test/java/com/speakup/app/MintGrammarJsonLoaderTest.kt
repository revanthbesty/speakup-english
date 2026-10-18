package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintGrammarJsonLoaderTest {
    private val draft = """
      {"schema_version":1,"publication_status":"DRAFT_NOT_PLAYABLE","questions":[{
      "id":"EM-GR-NEW-00501","version":2,"status":"draft","topic_id":"GRAMMAR-01",
      "prompt":"Which word is an adjective?","options":["quiet","desk","quickly"],
      "correct_key":"A","explanations":["Quiet modifies nouns.","Desk is a noun.","Quickly is an adverb."],
      "grammar_rule":"Adjectives describe nouns.","example_sentence":"The quiet room was peaceful.",
      "rights_status":"original_text_unreviewed","reviewer_id":null,"approved_at":null}]}
    """.trimIndent()

    @Test fun draftBankParsesButIsNotPlayable() {
        val parsed = MintGrammarJsonLoader.parseDraftBank(draft)
        assertEquals(1, parsed.size)
        assertEquals("EM-GR-NEW-00501", parsed[0].id)
        assertTrue(MintGrammarJsonLoader.playableQuestions(draft).isEmpty())
    }

    @Test fun mismatchedExplanationCountIsRejected() {
        val invalid = draft.replace(
            "\"Quiet modifies nouns.\",\"Desk is a noun.\",\"Quickly is an adverb.\"",
            "\"Only one explanation\""
        )
        try {
            MintGrammarJsonLoader.parseDraftBank(invalid)
            fail("Expected invalid explanations to be rejected")
        } catch (_: IllegalArgumentException) { }
    }

    @Test fun duplicateIdsAreRejected() {
        val first = org.json.JSONObject(draft)
        val question = first.getJSONArray("questions").getJSONObject(0)
        first.getJSONArray("questions").put(org.json.JSONObject(question.toString()))
        try {
            MintGrammarJsonLoader.parseDraftBank(first.toString())
            fail("Expected duplicate IDs to be rejected")
        } catch (_: IllegalArgumentException) { }
    }
}
