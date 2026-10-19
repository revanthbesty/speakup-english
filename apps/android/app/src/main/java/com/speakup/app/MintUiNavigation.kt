package com.speakup.app

/**
 * UI navigation contract. Internal page IDs are deliberately unchanged to preserve
 * all existing screen destinations, deep links and learner preferences.
 */
internal object MintUiNavigation {
    val tabs = listOf("Today", "Games", "Feed", "Speak", "Profile")

    fun label(route: String): String = when (route) {
        "Today" -> "Home"
        "Feed" -> "Clips"
        else -> route
    }

    fun mainTab(route: String): String = when (route) {
        "Learn", "Review", "Professional", "AILifePilot", "FamilyPilot", "CreatorLab" -> "Today"
        "FiveWord" -> "Feed"
        "Privacy", "Membership", "Help" -> "Profile"
        else -> route.takeIf { it in tabs } ?: "Today"
    }

    /** Single source of truth for parent destinations when leaving a secondary route. */
    fun backDestination(route: String, gameActive: Boolean = false, storyFromGames: Boolean = false): String = when {
        route == "Games" && gameActive -> "Games"
        route == "FiveWord" -> "Feed"
        route == "Feed" && storyFromGames -> "Games"
        route in listOf("Privacy", "Membership", "Help") -> "Profile"
        route == "Today" -> "Today"
        else -> "Today"
    }

    /** A tab tap is an explicit navigation request, not a return to the Games-launched clip. */
    fun clipsReturnToGamesAfterTabTap(tab: String): Boolean {
        require(tab in tabs) { "Unknown bottom tab: $tab" }
        return false
    }

    fun restoredTab(saved: String?): String =
        saved?.takeIf { it in tabs } ?: "Today"
}
