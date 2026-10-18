package com.speakup.app

/** Local, deterministic Professional English review without inventing awards. */
internal object MintProfessionalReview {
 val validIds = (MintProfessionalPilot.items.map { it.id } + MintInterviewPrep.items.map { it.id } +
  MintMeetingPractice.items.map { it.id } + MintBusinessWriting.items.map { it.id } +
  MintNegotiationPilot.items.map { it.id } + MintWorkplaceRoleplays.samples.map { it.id }).toSet()
 const val storageKey = "mint_professional_verified_practice_v1"
 fun recordVerified(existing:Set<String>, id:String, checkedCorrect:Boolean):Set<String> =
  if(checkedCorrect && id in validIds) existing + id else existing
 fun sanitize(saved:Set<String>)=saved.intersect(validIds)
 fun next(saved:Set<String>, preferred:String?):String? {
  val ordered=validIds.sorted().let { ids ->
   if(preferred == null) ids else ids.sortedBy { if(it.startsWith(preferred)) 0 else 1 }
  }
  return ordered.firstOrNull { it !in sanitize(saved) } ?: ordered.firstOrNull()
 }
 fun completed(saved:Set<String>)=sanitize(saved).size
 fun label(id:String?):String = when {
  id==null -> "Replay any completed practice"
  MintProfessionalPilot.items.any { it.id==id } -> MintProfessionalPilot.items.first{it.id==id}.situation
  MintInterviewPrep.items.any { it.id==id } -> MintInterviewPrep.items.first{it.id==id}.prompt
  MintMeetingPractice.items.any { it.id==id } -> MintMeetingPractice.items.first{it.id==id}.situation
  MintBusinessWriting.items.any { it.id==id } -> MintBusinessWriting.items.first{it.id==id}.context
  MintNegotiationPilot.items.any { it.id==id } -> MintNegotiationPilot.items.first{it.id==id}.context
  MintWorkplaceRoleplays.samples.any { it.id==id } -> MintWorkplaceRoleplays.samples.first{it.id==id}.title
  else -> "Explore a practice module"
 }
}
