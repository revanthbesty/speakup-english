package com.speakup.app

/** Main-thread session. Tokens prevent cancelled utterances from updating a new replay. */
internal class ListeningSession(private val publish: (ListeningSession) -> Unit) {
    var ready = false
        private set
    var speaking = false
        private set
    var message = "Preparing English voice…"
        private set
    private var closed = false
    private var sequence = 0
    private var active: String? = null

    fun initialized(available: Boolean) {
        if (closed) return
        ready = available
        message = if (available) "Ready to listen" else "English audio unavailable. Install an English voice in device text-to-speech settings, then reopen Listening. You can still use the transcript."
        publish(this)
    }

    fun play(): String? {
        if (closed || !ready) return null
        val token = (++sequence).toString()
        active = token
        speaking = true
        message = "Playing…"
        publish(this)
        return token
    }

    fun finished(token: String?, failed: Boolean = false) {
        if (closed || token == null || token != active) return
        active = null
        speaking = false
        message = if (failed) "Audio could not play. Try again or use the transcript." else "Finished. Replay whenever you like."
        publish(this)
    }

    fun stop() {
        if (closed) return
        active = null
        speaking = false
        if (ready) message = "Stopped. Tap Play to listen again."
        publish(this)
    }

    fun close() {
        active = null
        speaking = false
        ready = false
        closed = true
    }
}
