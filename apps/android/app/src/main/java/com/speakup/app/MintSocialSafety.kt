package com.speakup.app

/** Closed-by-default social policy. Public posting, follows, DMs and comments remain disabled. */
internal enum class MintSocialAction { WATCH, REACT, PRIVATE_PRACTICE, COMMENT, POST, MESSAGE, FOLLOW, SHARE, FRIEND_CHALLENGE }
internal enum class MintReportReason { INAPPROPRIATE, BULLYING, PERSONAL_INFORMATION, MISLEADING, OTHER }
internal data class MintContentReport(
    val clipId: String,
    val reason: MintReportReason,
    val createdAtMillis: Long,
    val sentToModerators: Boolean = false
)
internal object MintSocialSafety {
    fun allowed(ageBand: String, action: MintSocialAction): Boolean = when (action) {
        MintSocialAction.WATCH, MintSocialAction.REACT, MintSocialAction.PRIVATE_PRACTICE -> true
        // Public-facing actions require a real auth, consent, moderation and appeals backend.
        MintSocialAction.COMMENT, MintSocialAction.POST, MintSocialAction.MESSAGE,
        MintSocialAction.FOLLOW, MintSocialAction.SHARE, MintSocialAction.FRIEND_CHALLENGE -> false
    }

    fun validReport(report: MintContentReport, catalogIds: Set<String>): Boolean =
        report.clipId in catalogIds && report.createdAtMillis > 0 &&
            !report.sentToModerators // no trusted remote moderation endpoint is connected

    /** A report is local-only until a real secured submission API has been reviewed. */
    fun reportKey(report: MintContentReport): String =
        "${report.clipId}:${report.reason.name.lowercase()}"

    fun canViewClip(clip: MintClip, minimumAge: Int, blockedIds: Set<String>): Boolean =
        clip.minAge <= minimumAge && clip.id !in blockedIds
}
