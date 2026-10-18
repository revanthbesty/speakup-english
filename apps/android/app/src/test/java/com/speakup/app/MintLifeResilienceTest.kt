package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeResilienceTest {
 @Test fun lowDataAndThermalRiskForceLite(){assertEquals(MintLifeExperienceMode.LITE,MintLifeResilience.choose(MintLifeDeviceState(true,true,false,true,true)));assertEquals(MintLifeExperienceMode.LITE,MintLifeResilience.choose(MintLifeDeviceState(true,false,false,true,false)))}
 @Test fun reducedMotionDisablesAnimation(){assertFalse(MintLifeResilience.animationsAllowed(MintLifeDeviceState(true,false,true,false,true)))}
}
