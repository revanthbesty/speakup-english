package com.speakup.app

import androidx.test.core.app.ActivityScenario
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
    @Test fun mainActivityCanLaunchAndRecreateWithoutCrashing() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals("com.speakup.app", activity.packageName)
                assertTrue(!activity.isFinishing)
            }
            scenario.recreate()
            scenario.onActivity { activity ->
                assertTrue(!activity.isFinishing)
            }
        }
    }

    @Test fun fiveTabNavigationContractRemainsAvailable() {
        assertEquals(listOf("Today", "Games", "Feed", "Speak", "Profile"), MintUiNavigation.tabs)
        assertEquals("Home", MintUiNavigation.label("Today"))
        assertEquals("Clips", MintUiNavigation.label("Feed"))
        assertEquals("Feed", MintUiNavigation.mainTab("FiveWord"))
        assertEquals("Games", MintUiNavigation.backDestination("Feed", storyFromGames = true))
    }

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
