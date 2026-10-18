package com.speakup.app
import org.junit.Assert.*
import org.junit.Test
class MintNegotiationPilotTest {
 @Test fun itemsAreValidAndUnique(){assertEquals(4,MintNegotiationPilot.items.size);assertTrue(MintNegotiationPilot.items.all(MintNegotiationPilot::valid));assertEquals(4,MintNegotiationPilot.items.map{it.id}.distinct().size)}
 @Test fun bothCategoriesAreCovered(){assertEquals(setOf("Negotiation","Problem-Solving"),MintNegotiationPilot.items.map{it.category}.toSet())}
}
