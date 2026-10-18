package com.speakup.app

/** Offline authored interview answers, not a hiring predictor or automated interview assessment. */
internal data class MintInterviewItem(val id:String,val prompt:String,val choices:List<String>,val best:Int,val explanation:String,val followUp:String)
internal object MintInterviewPrep {
    val items=listOf(
        MintInterviewItem("intro","Tell me about yourself.",
            listOf("I enjoy solving problems and recently completed a team project.","You should hire me immediately.","I have nothing to share."),0,
            "A concise introduction connects a relevant strength to a concrete experience.","Give a 30-second introduction in your own words."),
        MintInterviewItem("strength","What is one of your strengths?",
            listOf("I never make mistakes.","I organize tasks carefully, for example by using weekly checklists.","I am better than everyone."),1,
            "Name a real strength and support it with a specific example.","Describe one strength and a time you used it."),
        MintInterviewItem("challenge","Tell me about a challenge you faced.",
            listOf("I always avoid challenges.","Everyone else caused the problem.","Our deadline changed, so I reordered tasks and updated the team."),2,
            "Explain the situation, your own action and a result without blaming others.","Explain a challenge, your action and what you learned."),
        MintInterviewItem("questions","Do you have any questions for us?",
            listOf("What would success look like in the first three months?","Why is this taking so long?","No, I have not prepared."),0,
            "A thoughtful role-related question shows preparation and curiosity.","Ask one professional question about the role.")
    )
    fun valid(item:MintInterviewItem):Boolean=item.id.isNotBlank()&&item.prompt.isNotBlank()&&
        item.choices.size==3&&item.choices.distinct().size==3&&item.choices.none { it.isBlank() }&&
        item.best in 0..2&&item.explanation.isNotBlank()&&item.followUp.isNotBlank()
}
