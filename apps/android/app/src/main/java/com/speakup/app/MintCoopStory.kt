package com.speakup.app

/** Device-only turn-taking prototype. No invitations, accounts, network or public rooms. */
internal data class MintStoryTurn(val role: String, val choice: String, val index: Int)
internal object MintCoopStory {
    val roles = listOf("Explorer", "Helper")
    val prompts = listOf(
        "Two friends find a lost book at the library. What should they do?",
        "The librarian asks where they found it. How can they respond politely?",
        "The book is returned. What do the friends say?"
    )
    val choices = listOf(
        listOf("Give it to the librarian.", "Hide it in a bag.", "Leave without helping."),
        listOf("We found it near the window.", "You must know.", "No words for you."),
        listOf("Thank you for helping us!", "Give us a reward.", "We will take another book.")
    )
    val keys = listOf(0, 0, 0)
    val explanations = listOf(
        "Returning a lost item to staff is a responsible choice.",
        "A clear, polite explanation helps staff find the owner.",
        "Thanking someone closes a conversation kindly."
    )
    fun valid(): Boolean = prompts.size == choices.size && keys.size == prompts.size &&
        explanations.size == prompts.size && choices.all { it.size == 3 && it.distinct().size == 3 }
    fun next(turns: List<MintStoryTurn>, answerIndex: Int): List<MintStoryTurn> {
        if (turns.size >= prompts.size || answerIndex !in 0..2) return turns
        if (answerIndex != keys[turns.size]) return turns
        return turns + MintStoryTurn(roles[turns.size % 2], choices[turns.size][answerIndex], turns.size)
    }
    fun complete(turns: List<MintStoryTurn>) = turns.size == prompts.size &&
        turns.indices.all { i -> turns[i].index == i && turns[i].role == roles[i % 2] &&
            turns[i].choice == choices[i][keys[i]] }
    const val rewardKey = "coop_story_library_v1"
}
