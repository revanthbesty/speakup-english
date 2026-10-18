package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintLifeVirtualCityTest {
 @Test fun cafeHasThreeDeterministicTurns(){assertTrue(MintLifeVirtualCity.valid());assertEquals(0,MintLifeVirtualCity.advance(0,2));assertEquals(1,MintLifeVirtualCity.advance(0,0))}
}
