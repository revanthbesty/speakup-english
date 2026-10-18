package com.speakup.app
internal data class MintLifeStoryChoice(val caption:String,val next:String)
internal data class MintLifeStoryNode(val id:String,val caption:String,val choices:List<MintLifeStoryChoice>)
internal object MintLifeStoryGraph {
 val pilot=listOf(
  MintLifeStoryNode("start","You find a note at the library.",listOf(MintLifeStoryChoice("Ask the librarian","help"),MintLifeStoryChoice("Read the note","read"))),
  MintLifeStoryNode("help","The librarian shows you a map.",listOf(MintLifeStoryChoice("Thank them","end"))),
  MintLifeStoryNode("read","The note invites you to a reading club.",listOf(MintLifeStoryChoice("Join politely","end"))),
  MintLifeStoryNode("end","You made a new reading connection.",emptyList())
 )
 fun valid(nodes:List<MintLifeStoryNode>):Boolean {
  val ids=nodes.map{it.id}.toSet()
  return nodes.isNotEmpty()&&ids.size==nodes.size&&nodes.all{it.id.isNotBlank()&&it.caption.isNotBlank()&&it.choices.all{c->c.caption.isNotBlank()&&c.next in ids}}
 }
 fun next(nodes:List<MintLifeStoryNode>,current:String,choice:Int):String? =
  nodes.firstOrNull{it.id==current}?.choices?.getOrNull(choice)?.next
}
