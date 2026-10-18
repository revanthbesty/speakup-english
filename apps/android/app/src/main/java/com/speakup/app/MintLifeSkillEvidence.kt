package com.speakup.app
internal enum class MintLifeSkill { VOCABULARY, GRAMMAR, LISTENING, SPEAKING, PRONUNCIATION }
internal data class MintLifeEvidence(val id:String,val skill:MintLifeSkill,val source:String,val version:Int,val timestampMs:Long,val observed:Int?,val confidence:Int?)
internal object MintLifeSkillProfile {
 fun valid(x:MintLifeEvidence)=x.id.isNotBlank()&&x.source.isNotBlank()&&x.version>0&&x.timestampMs>0&&x.observed?.let{it in 0..100}!=false&&x.confidence?.let{it in 0..100}!=false
 fun accepted(items:List<MintLifeEvidence>)=items.filter(::valid).distinctBy{it.id}
 fun counts(items:List<MintLifeEvidence>)=accepted(items).groupingBy{it.skill}.eachCount()
}
