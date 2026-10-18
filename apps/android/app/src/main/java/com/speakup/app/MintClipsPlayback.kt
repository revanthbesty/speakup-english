package com.speakup.app

/** Pure, offline playback rules shared by the Clips UI and regression tests. */
internal object MintClipsPlayback {
    fun safeIndex(index: Int, count: Int): Int =
        if (count <= 0) 0 else index.coerceIn(0, count - 1)

    fun next(index: Int, count: Int): Int = safeIndex(index + 1, count)
    fun previous(index: Int, count: Int): Int = safeIndex(index - 1, count)

    fun swipeTarget(index: Int, count: Int, verticalDragPx: Float, thresholdPx: Float = 70f): Int =
        when {
            count <= 0 -> 0
            verticalDragPx <= -thresholdPx -> next(index, count)
            verticalDragPx >= thresholdPx -> previous(index, count)
            else -> safeIndex(index, count)
        }

    fun advanceFrame(index: Int, count: Int, allowed: Boolean): Int =
        if (allowed) next(index, count) else safeIndex(index, count)

    fun progress(index: Int, count: Int): Float =
        if (count <= 0) 0f else (safeIndex(index, count) + 1f) / count

    fun mayAutoAdvance(playing: Boolean, reducedMotion: Boolean, overlayOpen: Boolean): Boolean =
        playing && !reducedMotion && !overlayOpen
}
