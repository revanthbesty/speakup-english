package com.speakup.app

/** Original, offline multimodal-style pilot. Listening mode uses optional device TTS, never paid audio. */
internal enum class MintGrammarFormat { DIALOGUE, LISTENING, VISUAL_CUE }
internal data class MintGrammarFormatItem(
    val id: String, val format: MintGrammarFormat, val cue: String, val prompt: String,
    val options: List<String>, val correct: Int, val explanation: String
)
internal object MintGrammarFormats {
    val samples = listOf(
        MintGrammarFormatItem("dialogue-01",MintGrammarFormat.DIALOGUE,
            "Alex: Have you finished your homework?\nSam: Yes, I ___ it this morning.",
            "Complete Sam's reply.",listOf("finished","finish","finishing"),0,
            "Finished is past simple: this morning refers to a completed action."),
        MintGrammarFormatItem("listening-01",MintGrammarFormat.LISTENING,
            "She has already sent the email.",
            "Which sentence did the speaker say?",listOf("She has already sent the email.","She already sending the email.","She have sent the email."),0,
            "She takes has, and the present perfect uses has + past participle sent."),
        MintGrammarFormatItem("visual-01",MintGrammarFormat.VISUAL_CUE,
            "🚌 → 🏫  |  Every school day",
            "Choose the correct sentence to describe the routine.",listOf("He goes to school by bus.","He going to school by bus.","He go to school by bus."),0,
            "For a daily routine, use present simple: he goes.")
    )
    fun valid(item: MintGrammarFormatItem): Boolean =
        item.id.isNotBlank() && item.cue.isNotBlank() && item.prompt.isNotBlank() &&
            item.options.size == 3 && item.options.distinct().size == 3 &&
            item.correct in 0..2 && item.explanation.isNotBlank()
    fun checked(item: MintGrammarFormatItem, selected: Int) = selected == item.correct
}
