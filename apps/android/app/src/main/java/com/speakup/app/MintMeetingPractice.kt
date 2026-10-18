package com.speakup.app

/** Original offline workplace meeting/presentation pilot; no recording or official scores. */
internal data class MintMeetingItem(val id:String,val category:String,val situation:String,val options:List<String>,val correct:Int,val explanation:String,val speakingPrompt:String)
internal object MintMeetingPractice {
 val items=listOf(
  MintMeetingItem("agenda","Meeting","You are opening a team meeting. What is a clear opening?",listOf("Let's review today's agenda and agree on next steps.","No one needs to know the agenda.","I will not take questions."),0,"State the purpose and agenda so participants know what to expect.","Introduce a meeting agenda in your own words."),
  MintMeetingItem("clarify","Meeting","You are unsure about a colleague's proposal. What should you say?",listOf("That makes no sense.","Could you clarify how the timeline would work?","I refuse to discuss it."),1,"Ask a specific, courteous clarification question.","Ask someone to explain a project detail."),
  MintMeetingItem("present-opening","Presentation","How can you introduce a short project presentation?",listOf("Don't interrupt.","You can guess my topic.","Today I'll explain the project goal, progress and next steps."),2,"A useful introduction previews the main points.","Introduce a 30-second presentation."),
  MintMeetingItem("present-evidence","Presentation","You are explaining results with limited data. Which statement is most responsible?",listOf("These early results suggest improvement, but we need more data.","The results prove success forever.","No questions are permitted."),0,"Describe evidence proportionately and acknowledge uncertainty.","Explain one result and its limitation.")
 )
 fun valid(x:MintMeetingItem)=x.id.isNotBlank()&&x.category in setOf("Meeting","Presentation")&&x.situation.isNotBlank()&&x.options.size==3&&x.options.distinct().size==3&&x.options.none(String::isBlank)&&x.correct in 0..2&&x.explanation.isNotBlank()&&x.speakingPrompt.isNotBlank()
}
