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
        "Learn", "Review" -> "Today"
        "FiveWord" -> "Feed"
        "Privacy", "Membership", "Help" -> "Profile"
        else -> route.takeIf { it in tabs } ?: "Today"
    }

    fun restoredTab(saved: String?): String =
        saved?.takeIf { it in tabs } ?: "Today"
}
