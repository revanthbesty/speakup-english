package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class ListeningSessionTest {
    @Test fun cannotPlayBeforeInitializationOrWithMissingVoice() {
        val session = ListeningSession {}
        assertNull(session.play())
        session.initialized(false)
        assertFalse(session.ready)
        assertNull(session.play())
        assertTrue(session.message.contains("transcript"))
    }

    @Test fun replayIgnoresCompletionAndErrorsFromPreviousUtterance() {
        val session = ListeningSession {}
        session.initialized(true)
        val first = session.play()
        val replay = session.play()
        assertNotEquals(first, replay)
        session.finished(first)
        session.finished(first, true)
        assertTrue(session.speaking)
        session.finished(replay)
        assertFalse(session.speaking)
        assertTrue(session.ready)
    }

    @Test fun stopInvalidatesCallbacksAndAllowsReplay() {
        val session = ListeningSession {}
        session.initialized(true)
        val token = session.play()
        session.stop()
        session.finished(token, true)
        assertFalse(session.speaking)
        assertTrue(session.message.startsWith("Stopped"))
        assertNotNull(session.play())
    }

    @Test fun disposalIgnoresLateInitializationAndPlaybackCallbacks() {
        var updates = 0
        val session = ListeningSession { updates++ }
        session.initialized(true)
        val token = session.play()
        val beforeClose = updates
        session.close()
        session.initialized(true)
        session.finished(token)
        session.stop()
        assertEquals(beforeClose, updates)
        assertFalse(session.ready)
        assertFalse(session.speaking)
        assertNull(session.play())
    }

    @Test fun synthesisFailureCanBeRetried() {
        val session = ListeningSession {}
        session.initialized(true)
        session.finished(session.play(), true)
        assertFalse(session.speaking)
        assertTrue(session.message.contains("Try again"))
        assertNotNull(session.play())
    }

    @Test fun engineCancellationCannotStopANewerReplay() {
        val session = ListeningSession {}
        session.initialized(true)
        val old = session.play()
        val current = session.play()
        session.cancelled(old)
        assertTrue(session.speaking)
        session.cancelled(current)
        assertFalse(session.speaking)
        assertTrue(session.ready)
    }
}
