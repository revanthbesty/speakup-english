package com.speakup.app

/** A short, non-certified routing check. The learner's selected level controls the questions. */
internal data class MintPlacementQuestion(
    val id: String,
    val prompt: String,
    val choices: List<String>,
    val correct: String,
    val level: String,
    val explanation: String
)

internal object MintPlacementBank {
    private val questions = listOf(
        MintPlacementQuestion("e1", "Which word means the opposite of hot?", listOf("Cold", "Heavy", "Fast"), "Cold", "Easy", "Hot and cold are opposites."),
        MintPlacementQuestion("e2", "Choose the correct sentence.", listOf("She likes apples.", "She like apples.", "She liking apples."), "She likes apples.", "Easy", "Use likes with she."),
        MintPlacementQuestion("e3", "Which reply fits: How are you?", listOf("I'm fine, thank you.", "At the park.", "On Monday."), "I'm fine, thank you.", "Easy", "Answer how you feel."),
        MintPlacementQuestion("e4", "Choose the correct question.", listOf("Where is the bus stop?", "Where the bus stop?", "Where does bus stop is?"), "Where is the bus stop?", "Easy", "Use is before the place in this question."),
        MintPlacementQuestion("e5", "Which sentence describes yesterday?", listOf("I played football.", "I play football tomorrow.", "I am playing tomorrow."), "I played football.", "Easy", "Played is a past-tense verb."),
        MintPlacementQuestion("e6", "Choose the polite request.", listOf("Could I have some water, please?", "Water give.", "You water now."), "Could I have some water, please?", "Easy", "Could I and please make this polite."),

        MintPlacementQuestion("m1", "Complete: If it rains, we ___ at home.", listOf("will stay", "stayed yesterday", "staying"), "will stay", "Medium", "Use will for a likely future result."),
        MintPlacementQuestion("m2", "Choose the correct present perfect sentence.", listOf("I've lived here for three years.", "I live here since three years.", "I am living here from three years."), "I've lived here for three years.", "Medium", "Present perfect with for describes duration."),
        MintPlacementQuestion("m3", "Which response politely disagrees?", listOf("I see your point, but I think differently.", "You are wrong, stop.", "No way ever."), "I see your point, but I think differently.", "Medium", "Acknowledge the idea before disagreeing."),
        MintPlacementQuestion("m4", "Complete: She is interested ___ learning English.", listOf("in", "on", "at"), "in", "Medium", "Interested is followed by in."),
        MintPlacementQuestion("m5", "Choose the correct reported speech.", listOf("He said that he was tired.", "He said that he is tired yesterday.", "He said he tired was."), "He said that he was tired.", "Medium", "Reported speech often shifts the tense back."),
        MintPlacementQuestion("m6", "Which sentence describes a hypothetical situation?", listOf("If I had more time, I would travel.", "If I have time yesterday, I travel.", "I had travel if time."), "If I had more time, I would travel.", "Medium", "Use if + past and would for hypotheticals."),

        MintPlacementQuestion("h1", "Which sentence uses inversion correctly?", listOf("Rarely have I seen such a result.", "Rarely I have seen such a result.", "Rarely seen I have such a result."), "Rarely have I seen such a result.", "Hard", "Negative or limiting adverbs can trigger inversion."),
        MintPlacementQuestion("h2", "Choose the most precise sentence.", listOf("The findings suggest a correlation, not causation.", "The findings prove everything caused everything.", "The findings are stuff about things."), "The findings suggest a correlation, not causation.", "Hard", "Distinguish association from a causal claim."),
        MintPlacementQuestion("h3", "Complete: Had I known sooner, I ___ you.", listOf("would have informed", "will inform", "would inform yesterday"), "would have informed", "Hard", "This is an inverted third conditional."),
        MintPlacementQuestion("h4", "What does 'mitigate' most nearly mean?", listOf("Reduce the severity of", "Make inevitable", "Disregard completely"), "Reduce the severity of", "Hard", "Mitigate means make less serious."),
        MintPlacementQuestion("h5", "Choose the most diplomatic recommendation.", listOf("It may be worth reconsidering the proposed timeline.", "That timeline is stupid.", "Fix it immediately, no discussion."), "It may be worth reconsidering the proposed timeline.", "Hard", "Hedging can communicate constructive criticism."),
        MintPlacementQuestion("h6", "Which transition signals contrast?", listOf("Nevertheless", "Consequently", "Furthermore"), "Nevertheless", "Hard", "Nevertheless introduces a contrasting point."),

        MintPlacementQuestion("x1", "In an academic argument, which claim is best supported?", listOf("The data indicate a possible association.", "Everyone knows the result is true.", "This proves the claim in every case."), "The data indicate a possible association.", "Exam", "Qualified claims avoid overstating evidence."),
        MintPlacementQuestion("x2", "Select the sentence with the clearest thesis.", listOf("Public transit investment can reduce congestion and improve access.", "Transport is a thing people use.", "Many things are interesting about cities."), "Public transit investment can reduce congestion and improve access.", "Exam", "A thesis states a specific, arguable claim."),
        MintPlacementQuestion("x3", "Which paraphrase best preserves 'The number of visitors rose sharply'?", listOf("Visitor numbers increased substantially.", "Visitors became less important.", "The visitors left quickly."), "Visitor numbers increased substantially.", "Exam", "A valid paraphrase keeps the original meaning."),
        MintPlacementQuestion("x4", "Choose the most appropriate formal conclusion.", listOf("Overall, the evidence supports targeted improvements.", "So yeah that's all.", "The answer is obvious to everybody."), "Overall, the evidence supports targeted improvements.", "Exam", "Conclusions should synthesize the evidence clearly."),
        MintPlacementQuestion("x5", "Which sentence expresses a cautious prediction?", listOf("The policy may lead to improved outcomes.", "The policy must fix every problem.", "The policy already worked everywhere."), "The policy may lead to improved outcomes.", "Exam", "May signals a prediction without overstating certainty."),
        MintPlacementQuestion("x6", "Which linking phrase introduces an example?", listOf("For instance", "In contrast", "As a result"), "For instance", "Exam", "For instance introduces a supporting example.")
    )

    private val validLevels = setOf("Easy", "Medium", "Hard", "Exam")
    fun startingLevel(path: String, selectedDifficulty: String, learningGoal: String): String = when {
        selectedDifficulty !in setOf("Easy", "Medium", "Hard") -> "Easy"
        path == "exams" || learningGoal == "School & exams" -> "Exam"
        selectedDifficulty == "Hard" || path == "advanced" -> "Hard"
        selectedDifficulty == "Medium" || path == "speaking" -> "Medium"
        else -> "Easy"
    }

    fun session(level: String, attempt: Int): List<MintPlacementQuestion> {
        val safeLevel = if (level in validLevels) level else "Easy"
        val pool = questions.filter { it.level == safeLevel }
        // Rotate the three-question selection on retakes without randomness or server calls.
        val offset = (attempt.coerceAtLeast(0) * 3) % pool.size
        return (0 until 3).map { pool[(offset + it) % pool.size] }
    }

    fun recommendation(level: String, score: Int): String {
        if (score < 0) return "Assessment skipped. Pick a challenge whenever you are ready."
        return when (level) {
            "Exam" -> if (score >= 2) "Exam practice: academic arguments and clear speaking." else "Build exam vocabulary and structured responses."
            "Hard" -> if (score >= 2) "Advanced speaking, debates and precise vocabulary." else "Strengthen complex grammar before advanced speaking."
            "Medium" -> if (score >= 2) "Intermediate conversations and fluent responses." else "Practise sentence construction and daily dialogue."
            else -> if (score >= 2) "Everyday conversations and short speaking missions." else "Start with basic words and simple phrases."
        }
    }
}
