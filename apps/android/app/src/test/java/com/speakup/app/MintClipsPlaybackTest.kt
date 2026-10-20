package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintClipsPlaybackTest {
    @Test fun finiteSwipeNeverWrapsOrSkipsInvalidContent() {
        assertEquals(0, MintClipsPlayback.swipeTarget(0, 0, -400f))
        assertEquals(1, MintClipsPlayback.swipeTarget(0, 3, -80f))
        assertEquals(0, MintClipsPlayback.swipeTarget(0, 3, 80f))
        assertEquals(2, MintClipsPlayback.swipeTarget(2, 3, -100f))
        assertEquals(1, MintClipsPlayback.swipeTarget(1, 3, 20f))
        assertEquals(0, MintClipsPlayback.safeIndex(-5, 3))
    }

    @Test fun restoredAndResetIndicesRemainBoundedForChangingCatalogs() {
        assertEquals(0, MintClipsPlayback.safeIndex(200, 0))
        assertEquals(0, MintClipsPlayback.safeIndex(-2, 5))
        assertEquals(4, MintClipsPlayback.safeIndex(200, 5))
        assertEquals(4, MintClipsPlayback.next(4, 5))
        assertEquals(0, MintClipsPlayback.previous(0, 5))
    }

    @Test fun progressAndFrameNavigationAreBounded() {
        assertEquals(0f, MintClipsPlayback.progress(0, 0), 0.0001f)
        assertEquals(0.5f, MintClipsPlayback.progress(1, 4), 0.0001f)
        assertEquals(1f, MintClipsPlayback.progress(20, 4), 0.0001f)
        assertEquals(2, MintClipsPlayback.advanceFrame(2, 3, true))
        assertEquals(1, MintClipsPlayback.advanceFrame(1, 3, false))
    }

    @Test fun noAutoplayWhenMotionIsReducedOrDialogIsOpen() {
        assertTrue(MintClipsPlayback.mayAutoAdvance(true, false, false))
        assertFalse(MintClipsPlayback.mayAutoAdvance(true, true, false))
        assertFalse(MintClipsPlayback.mayAutoAdvance(true, false, true))
        assertFalse(MintClipsPlayback.mayAutoAdvance(false, false, false))
    }
}
