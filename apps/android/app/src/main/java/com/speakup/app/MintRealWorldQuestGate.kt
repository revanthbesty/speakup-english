package com.speakup.app

/** Sprint 26 bridge for the 100 Real-World Quests content package.
 *  Stages validation only: draft records must never appear in the learner catalog.
 *  No network, parent/teacher access, learner PII, or XP changes.
 */
internal data class MintRealWorldQuestRecord(
    val id: String,
    val category: String,
    val difficulty: String,
    val title: String,
    val question: String,
    val choices: List<String>,
    val correctAnswer: String,
    val speakingPrompt: String,
    val status: String
)

internal object MintRealWorldQuestGate {
    private val levels = setOf("EASY", "MEDIUM", "HARD")
    private val answers = setOf("A", "B", "C")
    fun valid(q: MintRealWorldQuestRecord): Boolean =
        q.id.matches(Regex("RWQ-[0-9]{4}")) &&
        q.category.isNotBlank() && q.title.isNotBlank() && q.question.isNotBlank() &&
        q.difficulty in levels && q.choices.size == 3 &&
        q.choices.all { it.isNotBlank() } && q.correctAnswer in answers &&
        q.speakingPrompt.isNotBlank() && q.status.lowercase() in setOf("draft", "approved")

    fun publishable(q: MintRealWorldQuestRecord): Boolean =
        valid(q) && q.status.equals("approved", ignoreCase = true)

    fun readyForImport(quests: List<MintRealWorldQuestRecord>): Boolean =
        quests.size == 100 && quests.map { it.id }.toSet().size == 100 &&
        quests.count { it.difficulty == "EASY" } == 40 &&
        quests.count { it.difficulty == "MEDIUM" } == 35 &&
        quests.count { it.difficulty == "HARD" } == 25 &&
        quests.all(::valid)

    fun explanationIndex(answer: String): Int? =
        answers.indexOf(answer).takeIf { it >= 0 }
}
