package com.speakup.app

import org.junit.Assert.*
import org.junit.Test

class MintMembershipPolicyTest {
    private val now = 1_800_000_000_000L
    private fun verified(tier: MintTier, state: MintEntitlementState = MintEntitlementState.ACTIVE) =
        MintMembershipGrant(tier, state, true, now+86_400_000L, true)

    @Test fun freeCoreLearningAlwaysRemainsAccessible() {
        val protectedCore = listOf(MintMembershipFeature.CORE_LESSONS,
            MintMembershipFeature.CORE_GAMES, MintMembershipFeature.CORE_SPEAKING,
            MintMembershipFeature.BASIC_PROGRESS)
        protectedCore.forEach { assertTrue(MintMembershipPolicy.allowed(it, null, now)) }
        assertFalse(MintMembershipPolicy.allowed(MintMembershipFeature.EXPANDED_GAME_PACKS, null, now))
    }
    @Test fun forgedLocalPremiumClaimsFailClosed() {
        val forged = MintMembershipGrant(MintTier.PRO,MintEntitlementState.ACTIVE,false,now+999_999_999L)
        assertEquals(MintTier.FREE,MintMembershipPolicy.effectiveTier(forged,now))
        assertFalse(MintMembershipPolicy.allowed(MintMembershipFeature.ADVANCED_SPEAKING_ANALYSIS,forged,now))
        assertEquals(MintTier.FREE,MintMembershipPolicy.effectiveTier(forged.copy(verifiedByServer=true),now))
        assertEquals(MintTier.FREE,MintMembershipPolicy.effectiveTier(verified(MintTier.PRO).copy(expiresAtEpochMillis=now),now))
    }
    @Test fun verifiedPlusDoesNotUnlockProFeatures() {
        val plus=verified(MintTier.PLUS)
        assertTrue(MintMembershipPolicy.allowed(MintMembershipFeature.EXTRA_AI_CONVERSATION,plus,now))
        assertFalse(MintMembershipPolicy.allowed(MintMembershipFeature.EXAM_PREPARATION,plus,now))
        assertTrue(MintMembershipPolicy.allowed(MintMembershipFeature.EXAM_PREPARATION,verified(MintTier.PRO),now))
    }
    @Test fun graceIsBoundedAndCancelledOrFamilyNotAutomaticallyEnabled() {
        assertEquals(MintTier.PLUS,MintMembershipPolicy.effectiveTier(verified(MintTier.PLUS,MintEntitlementState.GRACE),now))
        assertEquals(MintTier.FREE,MintMembershipPolicy.effectiveTier(verified(MintTier.PRO,MintEntitlementState.CANCELLED),now))
        assertEquals(MintTier.FREE,MintMembershipPolicy.effectiveTier(verified(MintTier.FAMILY),now))
    }
    @Test fun noPaidProductsOrPricesAreInvented() {
        val offers=MintMembershipPolicy.offers()
        assertEquals(4,offers.size)
        assertEquals("Free",offers[0].formattedPrice)
        assertTrue(offers.drop(1).all { it.productId == null && !it.storeVerified })
        assertTrue(offers.none { MintMembershipPolicy.checkoutEnabled(it,true) })
        val fake = MintOfferDisplay(MintTier.PRO,"Pro","Extra", "mint_pro_monthly","₹499",false)
        assertFalse(MintMembershipPolicy.checkoutEnabled(fake,true))
        assertFalse(MintMembershipPolicy.checkoutEnabled(fake.copy(storeVerified=true),false))
    }
}
