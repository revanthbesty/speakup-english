package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintOfflineGuideTest {
    @Test fun guideDistinguishesOfflineTextFromProviderDependentSpeech() {
        assertTrue(MintOfflineGuide.accurate())
        assertTrue(MintOfflineGuide.features.first { it.title == "Real-World Quests" }.offline)
        assertTrue(MintOfflineGuide.needsConnection(MintOfflineGuide.features.first { it.title == "Speech recognition" }))
        assertFalse(MintOfflineGuide.features.first { it.title == "Online rooms and sync" }.offline)
    }
}
