package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintSpeechSafetyTest {
    @Test fun onlyRecognizedVoiceCanCountAsVoiceAttempt() {
        MintSpeechOutcome.entries.forEach { outcome ->
            assertEquals(outcome == MintSpeechOutcome.RECOGNIZED, MintSpeechSafety.countsAsRecognizedVoice(outcome))
        }
    }
    @Test fun noOutcomeClaimsPronunciationScore() {
        MintSpeechOutcome.entries.forEach { assertFalse(MintSpeechSafety.mayShowPronunciationScore(it)) }
    }
    @Test fun skippedAndUnavailableOfferTruthfulFeedback() {
        assertTrue(MintSpeechSafety.feedback(MintSpeechOutcome.SKIPPED).contains("No voice attempt"))
        assertTrue(MintSpeechSafety.feedback(MintSpeechOutcome.UNAVAILABLE).contains("text"))
    }
}
