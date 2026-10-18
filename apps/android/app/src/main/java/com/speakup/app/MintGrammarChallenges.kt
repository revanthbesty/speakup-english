package com.speakup.app

/** Offline challenge eligibility based on existing, verified grammar mastery only. */
internal object MintGrammarChallenges {
    const val title = "Grammar Garden Challenge"
    const val target = 6
    const val achievementKey = "grammar-garden-six-v1"
    fun verifiedCount(total: Int, mastered: Set<String>): Int =
        (0 until total.coerceAtLeast(0)).count { it.toString() in mastered }
    fun unlocked(total: Int, mastered: Set<String>): Boolean =
        verifiedCount(total, mastered) >= target
    fun newlyEligible(total: Int, mastered: Set<String>, awarded: Set<String>): Boolean =
        unlocked(total, mastered) && achievementKey !in awarded
}
