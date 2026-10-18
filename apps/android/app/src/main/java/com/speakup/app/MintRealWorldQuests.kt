package com.speakup.app

internal data class MintRealWorldScenario(
    val id: String, val title: String, val context: String, val characterLine: String,
    val choices: List<String>, val correctIndex: Int, val explanation: String,
    val speakingPrompt: String, val example: String
)
internal object MintRealWorldQuests {
    val scenarios = listOf(
        MintRealWorldScenario("cafe-v1", "At a café", "Order politely at a café.",
            "Barista: What would you like to order?",
            listOf("Coffee now!", "Could I have a cup of tea, please?", "I take yesterday tea."),
            1, "Could I have … please? is a clear, polite request.",
            "Ask for a drink politely.", "Could I have a cup of tea, please?"),
        MintRealWorldScenario("station-v1", "At a train station", "Ask for directions to a platform.",
            "Staff: Hello! Can I help?",
            listOf("Where is platform two, please?", "Train platform go me.", "No thanks, help now."),
            0, "Where is … please? politely asks for a location.",
            "Ask where platform two is.", "Excuse me, where is platform two, please?")
    )
    fun valid(s: MintRealWorldScenario) = s.id.isNotBlank() &&
        s.choices.size == 3 && s.choices.all { it.isNotBlank() } &&
        s.choices.map { it.trim().lowercase() }.distinct().size == 3 &&
        s.correctIndex in 0..2 && s.explanation.isNotBlank() &&
        s.speakingPrompt.isNotBlank() && s.example.isNotBlank()
    fun canFinish(correctChoice: Boolean, step: String, response: String): Boolean =
        correctChoice && when(step) {
            "typed" -> response.trim().isNotEmpty()
            "voice" -> response.trim().isNotEmpty()
            "skipped" -> true
            else -> false
        }
    fun rewardKey(s: MintRealWorldScenario) = "rwq_${s.id}"
}