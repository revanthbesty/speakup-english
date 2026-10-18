package com.speakup.app

/** Offline roleplay turns with a fixed original script. No external AI, accounts or chat. */
internal data class MintWorkplaceTurn(val speaker: String, val prompt: String, val options: List<String>, val answer: Int, val explanation: String)
internal data class MintWorkplaceRoleplay(val id: String, val title: String, val turns: List<MintWorkplaceTurn>)
internal object MintWorkplaceRoleplays {
    val samples = listOf(
        MintWorkplaceRoleplay("meeting-update","Clear project update",listOf(
            MintWorkplaceTurn("Manager","Could you update us on the report?",listOf("The draft is ready; I will send it by 3 PM.","No.","You do it."),0,"Give the current status and a clear next step."),
            MintWorkplaceTurn("Manager","What if you need more time?",listOf("You cannot ask that.","I'll let you know early and explain the revised timeline.","I will say nothing."),1,"Flag a delay early and explain what will change."),
            MintWorkplaceTurn("Colleague","Can I help with the final check?",listOf("Stop asking.","Your help is not needed.","Yes, could you review the figures? Thank you."),2,"Accept help with a specific, polite request.")
        )),
        MintWorkplaceRoleplay("customer-help","Resolve a customer query",listOf(
            MintWorkplaceTurn("Customer","My delivery has not arrived.",listOf("That's your problem.","I'm sorry for the delay. May I check the tracking status?","Wait forever."),1,"Acknowledge the concern and propose an action."),
            MintWorkplaceTurn("Customer","When can I expect an update?",listOf("I'll check and update you by 4 PM.","Never.","I don't need to say."),0,"Give a realistic, specific follow-up commitment."),
            MintWorkplaceTurn("Customer","Thank you for helping.",listOf("Go away.","I did nothing.","You're welcome. Please contact us if you need further help."),2,"Close courteously and offer appropriate follow-up.")
        ))
    )
    fun valid(s: MintWorkplaceRoleplay) = s.id.isNotBlank() && s.title.isNotBlank() && s.turns.size == 3 &&
        s.turns.all { it.speaker.isNotBlank() && it.prompt.isNotBlank() && it.options.size == 3 &&
            it.options.distinct().size == 3 && it.options.none(String::isBlank) &&
            it.answer in 0..2 && it.explanation.isNotBlank() }
    fun advance(s: MintWorkplaceRoleplay, index: Int, choice: Int): Int =
        if (index in s.turns.indices && s.turns[index].answer == choice) index + 1 else index
    fun finished(s: MintWorkplaceRoleplay, index: Int) = valid(s) && index == s.turns.size
}
