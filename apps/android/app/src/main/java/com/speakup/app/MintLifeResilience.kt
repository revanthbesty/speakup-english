package com.speakup.app
internal data class MintLifeDeviceState(val offline:Boolean,val lowData:Boolean,val reducedMotion:Boolean,val modelApproved:Boolean,val thermalSafe:Boolean)
internal enum class MintLifeExperienceMode { LITE, OFFLINE_PACK, LOCAL_ENHANCED }
internal object MintLifeResilience {
 fun choose(x:MintLifeDeviceState):MintLifeExperienceMode =
  if(!x.modelApproved||!x.thermalSafe||x.lowData)MintLifeExperienceMode.LITE
  else if(x.offline)MintLifeExperienceMode.OFFLINE_PACK else MintLifeExperienceMode.LOCAL_ENHANCED
 fun animationsAllowed(x:MintLifeDeviceState)=!x.reducedMotion
}
