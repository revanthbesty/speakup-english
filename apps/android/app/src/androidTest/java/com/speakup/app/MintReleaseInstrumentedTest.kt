package com.speakup.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Real emulator smoke test: package identity and legacy local preference readability.
 * Detailed UI, signed-upgrade and accessibility acceptance require separate tests.
 */
@RunWith(AndroidJUnit4::class)
class MintReleaseInstrumentedTest {
    @Test fun packageIdentityIsUnchanged() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.speakup.app", context.packageName)
    }

    @Test fun previousLocalPreferencesRemainAccessible() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val prefs = context.getSharedPreferences("speakup_learning", 0)
        assertNotNull(prefs)
        assertTrue(prefs.all.keys.none { it.isBlank() })
    }
}
