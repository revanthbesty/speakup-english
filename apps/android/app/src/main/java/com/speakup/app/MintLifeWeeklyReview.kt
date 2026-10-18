package com.speakup.app
internal data class MintLifeWeekSample(val skill:MintLifeSkill,val observed:Int?,val missed:Int,val timestampMs:Long)
internal data class MintLifeWeekSummary(val assessments:Int,val missedTotal:Int,val observedMean:Int?)
internal object MintLifeWeeklyReview {
 fun summarize(items:List<MintLifeWeekSample>):MintLifeWeekSummary {
  val valid=items.filter{it.timestampMs>0&&it.missed>=0&&it.observed?.let{v->v in 0..100}!=false}
  val scores=valid.mapNotNull{it.observed}
  return MintLifeWeekSummary(valid.size,valid.sumOf{it.missed},if(scores.isEmpty())null else scores.sum()/scores.size)
 }
}
