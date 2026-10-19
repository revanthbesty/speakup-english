package com.speakup.app

import org.junit.Assert.assertEquals
import org.junit.Test

class MintUiNavigationTest {
    @Test fun allFiveTabsAreReachableAndUniquelyLabeled() {
        assertEquals(listOf("Home", "Games", "Clips", "Speak", "Profile"), MintUiNavigation.tabs.map(MintUiNavigation::label))
        assertEquals(5, MintUiNavigation.tabs.distinct().size)
    }

    @Test fun preservedDestinationsResolveToTheirParentTabs() {
        assertEquals("Today", MintUiNavigation.mainTab("Learn"))
        assertEquals("Today", MintUiNavigation.mainTab("Review"))
        assertEquals("Today", MintUiNavigation.mainTab("Professional"))
        assertEquals("Today", MintUiNavigation.mainTab("AILifePilot"))
        assertEquals("Today", MintUiNavigation.mainTab("FamilyPilot"))
        assertEquals("Today", MintUiNavigation.mainTab("CreatorLab"))
        assertEquals("Profile", MintUiNavigation.mainTab("Privacy"))
        assertEquals("Profile", MintUiNavigation.mainTab("Membership"))
        assertEquals("Profile", MintUiNavigation.mainTab("Help"))
        assertEquals("Feed", MintUiNavigation.mainTab("Feed"))
        assertEquals("Feed", MintUiNavigation.mainTab("FiveWord"))
    }

    @Test fun invalidSavedRoutesSafelyReturnHome() {
        assertEquals("Today", MintUiNavigation.restoredTab(null))
        assertEquals("Today", MintUiNavigation.restoredTab("NotARealRoute"))
        assertEquals("Games", MintUiNavigation.restoredTab("Games"))
        assertEquals("Today", MintUiNavigation.restoredTab("FiveWord"))
        assertEquals("Today", MintUiNavigation.restoredTab("Professional"))
    }
}
