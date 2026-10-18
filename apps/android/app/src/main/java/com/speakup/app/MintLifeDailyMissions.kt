package com.speakup.app
internal data class MintLifeMission(val id:String,val module:String,val minutes:Int,val offline:Boolean,val ageEligible:Boolean,val published:Boolean)
internal object MintLifeDailyMissions {
 fun suggested(items:List<MintLifeMission>):List<MintLifeMission> {
  val eligible=items.filter{it.id.isNotBlank()&&it.published&&it.ageEligible&&it.offline&&it.minutes in 1..5}.distinctBy{it.id}
  return eligible.sortedWith(compareBy<MintLifeMission>{it.minutes}.thenBy{it.id}).take(2)
 }
 fun duration(items:List<MintLifeMission>)=items.sumOf{it.minutes}
 fun swap(plan:List<MintLifeMission>,index:Int,next:MintLifeMission):List<MintLifeMission> =
  if(index !in plan.indices || !next.published || !next.offline || !next.ageEligible || next.minutes !in 1..5) plan
  else plan.toMutableList().also{it[index]=next}
}
