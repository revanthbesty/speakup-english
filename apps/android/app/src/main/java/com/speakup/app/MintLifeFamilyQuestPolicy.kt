package com.speakup.app
internal data class MintFamilyQuestPermission(val childId:String,val adultVerified:Boolean,val invitationApproved:Boolean,val expiresAtMs:Long,val blocked:Boolean,val revoked:Boolean)
internal object MintLifeFamilyQuestPolicy {
 fun canJoin(p:MintFamilyQuestPermission?,nowMs:Long):Boolean =
  p!=null&&p.childId.isNotBlank()&&p.adultVerified&&p.invitationApproved&&p.expiresAtMs>nowMs&&!p.blocked&&!p.revoked
 fun publicVoiceRoomAllowed()=false
}
