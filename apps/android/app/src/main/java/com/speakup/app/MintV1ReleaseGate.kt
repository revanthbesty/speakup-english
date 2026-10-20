package com.speakup.app

/**
 * Release authorization is evidence-based, not inferred from a green debug build.
 * This policy deliberately defaults to NO-GO until every required gate is confirmed.
 * It does not publish, sign or deploy anything.
 */
internal enum class MintV1ReleaseCheck {
    INTEGRATED_FEATURES,
    ANDROID_UNIT_LINT_BUILD,
    API_29_INSTRUMENTATION,
    API_35_INSTRUMENTATION,
    CORE_NAVIGATION_AND_REWARDS,
    MATCHED_SIGNING_UPGRADE,
    PRIVACY_SECURITY_REVIEW,
    ACCESSIBILITY_AND_DEVICE_UI,
    APPROVED_VISUAL_DESIGN,
    RELEASE_AAB_AND_STORE_METADATA,
    EXPLICIT_OWNER_APPROVAL
}

internal data class MintV1ReleaseEvidence(
    val verified: Set<MintV1ReleaseCheck> = emptySet()
) {
    val missing: Set<MintV1ReleaseCheck>
        get() = MintV1ReleaseCheck.entries.toSet() - verified

    val canPublish: Boolean
        get() = missing.isEmpty()
}

internal object MintV1ReleaseGate {
    // Never store a hard-coded approval or infer completion from existing build status.
    fun evaluate(verified: Set<MintV1ReleaseCheck> = emptySet()): MintV1ReleaseEvidence =
        MintV1ReleaseEvidence(verified)
}
