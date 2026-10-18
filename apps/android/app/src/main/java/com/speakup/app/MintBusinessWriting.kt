package com.speakup.app

/** Original offline business-writing exercises. No external AI, email sending or content grading. */
internal data class MintBusinessWritingItem(val id:String,val topic:String,val context:String,val options:List<String>,val best:Int,val explanation:String,val draftPrompt:String)
internal object MintBusinessWriting {
 val items=listOf(
  MintBusinessWritingItem("email-subject","Email","You are requesting a meeting about the project update. Choose a clear subject.",listOf("Request: Project Update Meeting","Important!!!","Read this now"),0,"An informative subject states the purpose without unnecessary urgency.","Write a clear subject for a meeting request."),
  MintBusinessWritingItem("polite-request","Email","You need a colleague to review a document. Choose an appropriate request.",listOf("You must check this immediately.","Could you review the draft by Thursday, please?","Do it."),1,"A polite request names the task and a realistic deadline.","Write one sentence requesting feedback."),
  MintBusinessWritingItem("meeting-summary","Business Writing","After a team meeting, how should you summarise action points?",listOf("We talked about many things.","Everything was good.","Alex will review the figures by Friday; Sam will update the slides."),2,"Assign clear owners and deadlines when summarising follow-up tasks.","Write two clear action points with owners."),
  MintBusinessWritingItem("uncertain-update","Business Writing","You are unsure when a task will finish. What should you write?",listOf("The work is underway. I'll confirm the timeline after checking the remaining steps.","It is definitely finished tomorrow.","There is no need to update you."),0,"Communicate uncertainty accurately and promise a reasonable follow-up.","Draft a truthful progress update.")
 )
 fun valid(x:MintBusinessWritingItem)=x.id.isNotBlank()&&x.topic in setOf("Email","Business Writing")&&x.context.isNotBlank()&&x.options.size==3&&x.options.distinct().size==3&&x.options.none(String::isBlank)&&x.best in 0..2&&x.explanation.isNotBlank()&&x.draftPrompt.isNotBlank()
}
