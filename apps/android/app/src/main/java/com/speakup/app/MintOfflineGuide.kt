package com.speakup.app

/** Honest offline and accessibility capabilities; no claims of cached AI or synced private data. */
internal data class MintOfflineFeature(val title: String, val offline: Boolean, val fallback: String)
internal object MintOfflineGuide {
    val features = listOf(
        MintOfflineFeature("Real-World Quests", true, "Text choices and optional typed reply work offline; voice recognition may require a speech provider."),
        MintOfflineFeature("Co-op Stories", true, "Private pass-the-device story works without an account."),
        MintOfflineFeature("Career & Exam pilot", true, "Original reading and choices work offline; this is unofficial practice."),
        MintOfflineFeature("Mint Coach", true, "Short rule-based suggestions work on device when you opt in."),
        MintOfflineFeature("Speech recognition", false, "Use quiet text or skip; availability depends on device services."),
        MintOfflineFeature("Online rooms and sync", false, "Not enabled in this build; no account or collaboration service.")
    )
    fun accurate(): Boolean = features.map { it.title }.distinct().size == features.size &&
        features.all { it.title.isNotBlank() && it.fallback.isNotBlank() } &&
        features.none { it.title == "Online rooms and sync" && it.offline }
    fun needsConnection(feature: MintOfflineFeature) = !feature.offline
}
