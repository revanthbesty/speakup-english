package com.speakup.app

/** Unofficial offline workplace communication pilot; no simulated hiring decisions. */
internal data class MintNegotiationItem(val id:String,val category:String,val context:String,val choices:List<String>,val best:Int,val explanation:String)
internal object MintNegotiationPilot {
 val items=listOf(
  MintNegotiationItem("deadline","Negotiation","Your teammate asks for an earlier deadline than you can meet.",listOf("I can't do it; stop asking.","I can deliver the outline on Tuesday and the full report on Thursday. Would that work?","You must wait."),1,"Offer a realistic alternative and invite agreement."),
  MintNegotiationItem("scope","Negotiation","The client requests additional work without moving the deadline.",listOf("Let's review the priorities and agree on what fits within the timeline.","I'll do everything somehow.","Your request is unreasonable."),0,"Clarify scope and agree on trade-offs rather than promising what you cannot deliver."),
  MintNegotiationItem("mistake","Problem-Solving","You spot an error in a report shortly before a meeting.",listOf("Hide it.","Blame the last editor.","I'll correct the number, alert the team and explain the update."),2,"Take responsibility, correct the mistake and communicate clearly."),
  MintNegotiationItem("blocker","Problem-Solving","A task is blocked because a requirement is unclear.",listOf("Guess and proceed.","Could we confirm the requirement before I continue?","Stop working without telling anyone."),1,"Ask for clarification before committing effort to an uncertain requirement.")
 )
 fun valid(item:MintNegotiationItem)=item.id.isNotBlank()&&item.category in setOf("Negotiation","Problem-Solving")&&item.context.isNotBlank()&&item.choices.size==3&&item.choices.distinct().size==3&&item.choices.none(String::isBlank)&&item.best in 0..2&&item.explanation.isNotBlank()
}
