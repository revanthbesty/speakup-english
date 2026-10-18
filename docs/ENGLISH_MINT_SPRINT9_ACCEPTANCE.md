# English Mint — Sprint 9 Integration, Security and Data Protection

Date: 2026-10-18. Stacked on verified Sprint 8 branch (`feature/english-mint-sprint-8`).

## Repository audit and integration decision
The repository retains the TypeScript API identity service with PostgreSQL-backed users, password verification, and sessions, and a separate Android Compose application that currently persists progress in `speakup_learning`. **There is not yet an authenticated Android-to-API account migration path.** Automatically attaching anonymous local XP or child profiles to arbitrary API identities is unsafe, so this sprint does not claim cloud syncing is operational. Android package identity `com.speakup.app` and local key names remain unchanged.

## Delivered code
- `MintDataProtection.kt`: versioned `english-mint-local-export` JSON structure, explicit allowlist of progress/profile/theme/feed keys, deterministic set ordering, control-character-safe JSON escaping, reject unknown preference keys (such as credential or transcript values), cap oversized set exports, and non-mutating integrity warnings. Revision due/stage keys use a narrowly validated numeric suffix.
- `MintPrivacyScreen.kt`: opt-in Android Storage Access Framework CreateDocument export to user-selected JSON location, privacy notice before creating a copy, failure/cancel messaging, and separate confirmation to clear only private feed reactions, hidden clips, local reports and story completion marks. The app does not delete XP, lessons, games, voice-turn achievements, badges, or history in this operation.
- `MainActivity.kt`: Profile navigation to Privacy & Data without changing existing navigation, local account data or app identity.
- `AndroidManifest.xml`: cleartext app network traffic disabled. No new cloud API keys, microphone recording storage, internet permission, OAuth or third-party SDK dependencies.
- `MintDataProtectionTest.kt`: compatibility, whitelist/secret redaction, JSON escaping, size bounds, integrity warnings, selective-deletion preservation and unchanged APK/storage identifiers.

## Safe server migration blueprint (not activated)
1. Finish authenticated Android account connector against existing identity API, with TLS/pinning policy review, session rotation, logout and API tenancy checks. No client access to PostgreSQL and no credential commits.
2. Require user-driven account linking; obtain guardian consent for applicable minors. Do not silently mix progress across accounts.
3. Capture an immutable local snapshot *before* network migration; map legacy XP, activity IDs, source version, assessment score, voice participation keys and timestamps where real timestamps exist. Verify idempotent event IDs server-side.
4. Produce a dry-run diff and conflict resolution rubric, then use server-side transactions and rollback/restore tools. Duplicate XP is not added, completed lessons are unioned only after identity verification, and absent timestamps do not become fabricated achievements.
5. After server confirmation, retain original local history until the user has verified restoration; never automatically clear the device merely because an upload succeeded.
6. Before public release implement a genuine user-account deletion endpoint with authenticated identity, privacy/export workflow, retention and legal hold policy, and tested backups. The current Privacy UI **does not pretend to delete online accounts**.
7. For child-facing social features, the platform needs a verified guardian/age policy, moderation infrastructure, abuse escalation, blocking/report submission and audit logs before any social activation.

## Security and QA claims
- Existing local progress is exportable with explicit user action. Export files contain personal learning data and are not automatically encrypted; users are warned to select a private location.
- Clearing private feed interactions does not remove previously captured files or any remote accounts. Local reports are not online moderator reports; public posting and child-to-stranger messaging remain disabled.
- This Android build has no proven account login/sync workflow; authenticating to the existing web API requires a separate reviewed backend/app integration. Payment and live AI services remain inactive.
- Kotlin unit tests, Android lint/build, repository CI and release gates must pass on final PR head. Automated green CI is not evidence of pentesting, legal privacy compliance or device-specific accessibility.
- Device install/upgrade, Android SAF export correctness, full deletion lifecycle, performance, TalkBack, large fonts, network failure, Android OS compatibility, consent and privacy reviews remain for Sprint 10 and external dependencies.

## Sprint 9 exit gate
Code, migration plan, and safeguarding tests checked by CI; review-ready PR. Do not merge, deploy production, publish, start paid services or request installed-device review before Sprint 10. A functional production account/cloud migration is **not** claimed as completed.
