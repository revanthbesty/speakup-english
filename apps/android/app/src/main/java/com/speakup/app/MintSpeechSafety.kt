package com.speakup.app

/** Pure state rules. A skipped or cancelled voice prompt must never be counted as speech. */
internal enum class MintSpeechOutcome { NOT_STARTED, SKIPPED, CANCELLED, UNAVAILABLE, TEXT_SUBMITTED, RECOGNIZED }
internal object MintSpeechSafety {
    fun countsAsRecognizedVoice(outcome: MintSpeechOutcome): Boolean = outcome == MintSpeechOutcome.RECOGNIZED
    fun mayShowPronunciationScore(outcome: MintSpeechOutcome): Boolean = false // No pronunciation scoring service.
    fun feedback(outcome: MintSpeechOutcome): String = when (outcome) {
        MintSpeechOutcome.NOT_STARTED -> "Speaking is optional. You can type instead."
        MintSpeechOutcome.SKIPPED -> "Speaking skipped. No voice attempt recorded."
        MintSpeechOutcome.CANCELLED -> "Speech cancelled. Try again or type your answer."
        MintSpeechOutcome.UNAVAILABLE -> "Speech recognition unavailable. Use quiet text practice."
        MintSpeechOutcome.TEXT_SUBMITTED -> "Text response submitted. This is not a pronunciation score."
        MintSpeechOutcome.RECOGNIZED -> "Words recognized. Review the transcript before checking."
    }
}
