package com.speakup.app

/** Offline, deterministic companion sound signatures; pure functions for JVM tests. */
internal object MintPetSoundProfile {
    fun pitchRatio(id: String): Double = when (id) {
        "mint_fox" -> 1.0
        "buddy_panda" -> 0.75
        "spark_cat" -> 1.20
        "wise_owl" -> 0.90
        "joy_rabbit" -> 1.12
        "tiny_dragon" -> 1.34
        else -> 1.0
    }
}
