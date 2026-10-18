package com.speakup.app

/**
 * Local progress snapshot. XP represents earned participation, NOT measured proficiency.
 * Legacy XP and achievement keys are never rewritten during English Mint migration.
 */
internal data class MintProgressEvidence(
    val xp: Int,
    val completedLessons: Int,
    val puzzlesMastered: Int,
    val mintQuestsMastered: Int,
    val speakingAttempts: Int,
    val guidedVoiceTurns: Int,
    val revisionCorrect: Int,
    val listeningMastered: Int,
    val readingMastered: Int,
    val independentReplies: Int
)

internal data class MintLevel(val number: Int, val title: String, val currentFloor: Int, val nextTarget: Int?) {
    fun progress(xp: Int): Float = if (nextTarget == null) 1f else
        ((xp.coerceAtLeast(currentFloor) - currentFloor).toFloat() / (nextTarget - currentFloor)).coerceIn(0f, 1f)
}
internal data class MintBadge(val id: String, val name: String, val description: String, val earned: Boolean)
internal data class MintSkillSummary(val name: String, val evidence: String, val activities: Int)
internal data class MintCertificateStatus(val eligibleForAssessment: Boolean, val message: String)

/**
 * Local coins are cosmetic-credit *estimates*, derived from unique first mastery IDs.
 * They are not purchases, financial currency or a remote synced wallet.
 */
internal object MintRewards {
    private val levelFloors = listOf(0,100,300,600,1000,1800,3000,5000,8000,12000)
    private val levelNames = listOf("First Steps","Word Explorer","Sentence Builder","Rising Speaker",
        "Confident Communicator","Conversation Champion","Fluent Explorer",
        "English Achiever","Language Master","Mint Legend")

    fun level(xp: Int): MintLevel {
        val i = levelFloors.indexOfLast { xp.coerceAtLeast(0) >= it }.coerceAtLeast(0)
        return MintLevel(i + 1, levelNames[i], levelFloors[i], levelFloors.getOrNull(i + 1))
    }

    fun badges(p: MintProgressEvidence): List<MintBadge> = listOf(
        MintBadge("first_steps", "First Steps", "Complete one lesson", p.completedLessons >= 1),
        MintBadge("lesson_explorer", "Lesson Explorer", "Finish four lessons", p.completedLessons >= 4),
        MintBadge("brave_speaker", "Brave Speaker", "Try private microphone speaking", p.speakingAttempts >= 1),
        MintBadge("memory_starter", "Memory Starter", "Answer three revision questions correctly", p.revisionCorrect >= 3),
        MintBadge("xp_collector", "XP Collector", "Collect 300 XP", p.xp >= 300),
        MintBadge("mint_arcade", "Mint Arcade", "Master four different Mint quests", p.mintQuestsMastered >= 4),
        MintBadge("conversation_starter", "Conversation Starter", "Practise three distinct guided voice turns", p.guidedVoiceTurns >= 3),
        MintBadge("listening_explorer", "Listening Explorer", "Master two listening challenges", p.listeningMastered >= 2),
        MintBadge("bookworm", "Reading Explorer", "Master two reading passages", p.readingMastered >= 2),
        MintBadge("independent_voice", "Independent Voice", "Make three practice replies without a hint", p.independentReplies >= 3)
    )

    fun uniqueMintEventKeys(keys: Set<String>): Set<String> =
        keys.filter { it.matches(Regex("mint_game_[a-z0-9-]+_v[1-9][0-9]*")) }.toSet()

    /** 5 non-purchasable cosmetic coins per genuine, distinct first-time Mint quest mastery. */
    fun cosmeticCoins(keys: Set<String>): Int = uniqueMintEventKeys(keys).size * 5

    fun skills(p: MintProgressEvidence): List<MintSkillSummary> = listOf(
        MintSkillSummary("Speaking", "${p.speakingAttempts} microphone attempts, ${p.guidedVoiceTurns} guided turns", p.speakingAttempts + p.guidedVoiceTurns),
        MintSkillSummary("Vocabulary & games", "${p.puzzlesMastered} legacy puzzles, ${p.mintQuestsMastered} Mint quests", p.puzzlesMastered + p.mintQuestsMastered),
        MintSkillSummary("Listening", "${p.listeningMastered} listening activities mastered", p.listeningMastered),
        MintSkillSummary("Reading", "${p.readingMastered} passages mastered", p.readingMastered),
        MintSkillSummary("Review", "${p.revisionCorrect} correct revision answers", p.revisionCorrect)
    )

    /**
     * Earning badges/XP alone cannot grant a certificate. Eligibility to attempt
     * an assessment is distinct from issuance/verification and is NOT a credential.
     */
    fun certificate(p: MintProgressEvidence): MintCertificateStatus =
        MintCertificateStatus(
            eligibleForAssessment = p.completedLessons >= 4 && p.guidedVoiceTurns >= 3 && p.revisionCorrect >= 3,
            message = if (p.completedLessons >= 4 && p.guidedVoiceTurns >= 3 && p.revisionCorrect >= 3)
                "You have enough practice evidence to explore a future assessed certificate. No assessment or credential has been issued."
            else "Certificates are locked until a defined skill assessment, grading rubric and secure verification are available."
        )
}
