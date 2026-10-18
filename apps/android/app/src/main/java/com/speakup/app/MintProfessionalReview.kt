package com.speakup.app

/** Local, deterministic Professional English review without inventing awards. */
internal object MintProfessionalReview {
 val validIds = (MintProfessionalPilot.items.map { it.id } + MintInterviewPrep.items.map { it.id } +
  MintMeetingPractice.items.map { it.id } + MintBusinessWriting.items.map { it.id } +
  MintNegotiationPilot.items.map { it.id }).toSet()
 fun sanitize(saved:Set<String>)=saved.intersect(validIds)
 fun next(saved:Set<String>, preferred:String?):String? {
  val ordered=validIds.sorted().let { ids ->
   if(preferred == null) ids else ids.sortedBy { if(it.startsWith(preferred)) 0 else 1 }
  }
  return ordered.firstOrNull { it !in sanitize(saved) } ?: ordered.firstOrNull()
 }
 fun completed(saved:Set<String>)=sanitize(saved).size
}
