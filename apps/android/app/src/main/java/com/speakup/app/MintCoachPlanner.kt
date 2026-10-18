package com.speakup.app

/** A transparent, local, opt-in practice planner. No network LLM or invented proficiency scores. */
internal data class MintCoachPlan(
    val heading: String,
    val reason: String,
    val steps: List<MintCoachStep>
)
internal data class MintCoachStep(val label: String, val route: String, val gameMode: String? = null)
internal object MintCoachPlanner {
    fun build(goal: String, difficulty: String, completedLessons: Int, questCompletions: Int): MintCoachPlan {
        val level = if (difficulty in setOf("Easy", "Medium", "Hard")) difficulty else "Easy"
        val newQuest = questCompletions < 2
        val practice = if (goal.contains("speak", ignoreCase = true) || goal.contains("confiden", ignoreCase = true))
            MintCoachStep("Practise one guided reply", "Speak")
        else MintCoachStep("Answer a short grammar question", "Games", "Grammar")
        return MintCoachPlan(
            heading = "Your two-minute Mint Coach plan",
            reason = "Suggested for your $level level and selected goal. You can change or turn off these suggestions.",
            steps = listOf(
                if (newQuest) MintCoachStep("Try a real-world situation", "Games", "Real-World Quests")
                else MintCoachStep("Continue a learning lesson", "Learn"),
                practice
            )
        )
    }
    fun feedback(answer: String): String = when {
        answer.isBlank() -> "You can type a reply, speak, or skip this optional coach prompt."
        answer.trim().split(Regex("\\s+")).size < 4 -> "Good start. Try adding what you need and a polite phrase such as 'please'."
        else -> "Nice practice. Read your reply aloud if you wish, then try a more specific or polite alternative."
    }
    fun allowedRoute(route: String): Boolean = route in setOf("Speak", "Games", "Learn")
}
