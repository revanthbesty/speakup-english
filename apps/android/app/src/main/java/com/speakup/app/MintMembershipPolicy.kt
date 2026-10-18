package com.speakup.app

/**
 * Server-authoritative membership model. The Android client cannot upgrade itself
 * by changing a preference or selecting a plan. Without verified server proof all
 * premium entitlements fail closed and meaningful core learning stays Free.
 */
internal enum class MintTier { FREE, PLUS, PRO, FAMILY }
internal enum class MintMembershipFeature {
    CORE_LESSONS, CORE_GAMES, CORE_SPEAKING, BASIC_PROGRESS,
    EXPANDED_GAME_PACKS, PREMIUM_ADVENTURES, EXTRA_AI_CONVERSATION,
    ADVANCED_SPEAKING_ANALYSIS, EXAM_PREPARATION, DEEP_PROGRESS_REPORTS
}
internal enum class MintEntitlementState { NONE, ACTIVE, GRACE, EXPIRED, CANCELLED, UNVERIFIED }
internal data class MintMembershipGrant(
    val tier: MintTier,
    val state: MintEntitlementState,
    val verifiedByServer: Boolean,
    val expiresAtEpochMillis: Long,
    val serverSignatureValid: Boolean = false
)
internal data class MintOfferDisplay(
    val tier: MintTier,
    val title: String,
    val summary: String,
    val productId: String?,
    val formattedPrice: String?,
    val storeVerified: Boolean
)
internal object MintMembershipPolicy {
    private val freeFeatures = setOf(
        MintMembershipFeature.CORE_LESSONS, MintMembershipFeature.CORE_GAMES,
        MintMembershipFeature.CORE_SPEAKING, MintMembershipFeature.BASIC_PROGRESS
    )
    private val plusFeatures = freeFeatures + setOf(
        MintMembershipFeature.EXPANDED_GAME_PACKS,
        MintMembershipFeature.PREMIUM_ADVENTURES,
        MintMembershipFeature.EXTRA_AI_CONVERSATION
    )
    private val proFeatures = plusFeatures + setOf(
        MintMembershipFeature.ADVANCED_SPEAKING_ANALYSIS,
        MintMembershipFeature.EXAM_PREPARATION,
        MintMembershipFeature.DEEP_PROGRESS_REPORTS
    )

    /**
     * Server verification and expiry are required even when a claimed plan says PRO.
     * GRACE stays available only while a verified server grace deadline is valid.
     */
    fun effectiveTier(grant: MintMembershipGrant?, now: Long): MintTier =
        if (grant != null && grant.verifiedByServer && grant.serverSignatureValid &&
            grant.tier != MintTier.FAMILY &&
            grant.state in setOf(MintEntitlementState.ACTIVE, MintEntitlementState.GRACE) &&
            grant.expiresAtEpochMillis > now
        ) grant.tier else MintTier.FREE

    fun allowed(feature: MintMembershipFeature, grant: MintMembershipGrant?, now: Long): Boolean {
        val tier = effectiveTier(grant, now)
        return feature in when(tier) {
            MintTier.FREE, MintTier.FAMILY -> freeFeatures
            MintTier.PLUS -> plusFeatures
            MintTier.PRO -> proFeatures
        }
    }

    fun offers(): List<MintOfferDisplay> = listOf(
        MintOfferDisplay(MintTier.FREE, "Free", "Games, private speaking and essential progress", null, "Free", true),
        MintOfferDisplay(MintTier.PLUS, "Plus", "Expanded game packs, adventures and larger AI allowances", null, null, false),
        MintOfferDisplay(MintTier.PRO, "Pro", "Advanced speaking, exam pathways and detailed progress", null, null, false),
        MintOfferDisplay(MintTier.FAMILY, "Family (future)", "Guardian-managed learning for the family", null, null, false)
    )

    /** Never expose a fabricated price or pretend this is an actionable purchase. */
    fun checkoutEnabled(offer: MintOfferDisplay, approvedBilling: Boolean): Boolean =
        approvedBilling && offer.storeVerified && !offer.productId.isNullOrBlank() &&
            !offer.formattedPrice.isNullOrBlank() && offer.tier in setOf(MintTier.PLUS, MintTier.PRO)
}
