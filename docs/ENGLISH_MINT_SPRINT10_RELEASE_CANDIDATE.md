# English Mint — Sprint 10 Release Candidate Acceptance and Blockers

Release candidate: Android `applicationId=com.speakup.app`, `versionCode=2`, `versionName=0.10.0-rc1`. Development branch `feature/english-mint-sprint-10` stacked on Sprint 9. **Internal/debug build only — not a signed Play Store release.**

## Engineering changes
1. Corrected local JSON export to preserve all completed lessons/reward records even when a set contains more than 1,000 entries. Prior behavior silently truncated entries, risking unsafe manual restoration.
2. Wired selective private-feed deletion back to Compose state so removed local reactions/reports/hidden clips are not shown until restart.
3. Bumped Android candidate versionCode from 1 to 2 to distinguish the latest candidate and avoid accidental numeric downgrades when signing-compatible installations are available.
4. Added `MintReleaseContractTest` covering stable app identity/storage, all 72 authored quests and 8 mechanics, first-mastery XP idempotency, age controls across games/feed/speaking, social fail-closed behavior, no active billing/unchecked certificates, non-lossy progress export and truthful quiet-mode speech credit.
5. Preserved prior branch history and all existing user preference names. No live AI/billing/social backends, no cloud account mutation, no publication, and no destructive data migration.

## Automation gate — actual status must be obtained from the matching GitHub Actions run
- `gradle :app:testDebugUnitTest :app:lintDebug`: Kotlin unit and lint regression gate
- `gradle :app:assembleDebug`: debug APK compilation gate and Actions artifact
- Monorepo `pnpm` lint, typecheck, test and build CI
- Release-gates workflow; branch must remain reviewable and unmerged until approved
- APK download through the **GitHub Actions artifact**, not an invented local file URL or Google Play listing
- Check output artifact `English-Mint-debug-apk` and `Android-verification-reports` for the exact Sprint 10 commit.

## First installed-device review — USER ACTION REQUIRED, NOT YET VERIFIED
**Protect existing SpeakUp user data.** An APK from GitHub-hosted Android CI may be signed with a fresh GitHub runner debug key. Android rejects updates over an installed application signed by a different key. **Do not uninstall an existing SpeakUp/English Mint installation merely to install this debug candidate; uninstalling can erase its local learning history.** Use a spare device/emulator or a candidate signed with the matching existing app key, and keep a backup. No signing keys should be transferred into chat or committed.

Test on device:
- Fresh installation on a spare Android 8+ device; launch, logo, page navigation Home/Feed/Games/Speak/Profile, orientation/responsive rendering
- Confirm age bands and journeys, level-appropriate placement, skip/retake and saved learner goals
- Complete existing lessons/game types; 72 authored quests across 8 mechanics should support hints, answer, first-XP-only, replay and age filters
- Test Bluetooth/no-device audio, TTS missing, speech recognizer missing, microphone denial, recognizing accent variety, quiet text mode without speaking XP
- Check 12 finite illustrated Reels, captions/manual playback, vertical swipes, no stranger chat; local report says **not submitted** and private hide/restore works
- Validate reward level/badge evidence, XP and local coins; no invented certificates or purchase checkout
- Export the complete data JSON using the picker, verify non-truncated entries, cancel/failure behavior and the private-file warning
- Clear private feed data and ensure other XP, lessons and progress remain unchanged **without restart**
- Light/Dark/System/Night modes; scheduled rollover, eye comfort, 200% font, TalkBack, reduced motion, screen magnifier and keyboard/switch control
- Test offline mode, flight mode, low memory, background/return, time-zone change and Android 8/modern OS samples
- For genuine in-place upgrade testing, ONLY use an APK signed by the same identity as the installed app, then verify XP/lessons remain unchanged. APK package name alone is not proof of an installable upgrade.

## Not production complete / launch-blocking gaps
- The SRS asks for 500+ curated game variants and 50 mechanics. **Confirmed new Mint Quest catalog is 72 authored variants and 8 mechanics** (plus preserved older game modes).
- Feed consists of 12 original illustrated/captioned reels, **not production video streaming or licensed MP4 clips**.
- Offline scripted conversation is not provider-backed AI; no verified pronunciation analysis or opt-in recorded-audio playback.
- Android app is **not integrated with secure authenticated cloud account sync** or online account deletion. Existing API and database alone do not prove user migration. Needs backend validation, consent and rollback drill.
- No staffed moderation, production report delivery, child guardian verification or public community. Direct messages/public social actions are disabled.
- No Play Billing purchase products, verified server entitlements or live payment collection; Plus/Pro are informational only.
- No real certified credentials or third-party exam scores. No measured pilot crash-free rate, professional security assessment, accessibility certification or performance p95 evidence.
- No release signing keystore, Play integrity check, brand/trademark clearance, support/privacy policy legal approval, external AI budget approval or Play Store publication.
- Testing on Android hardware is explicitly deferred to this Sprint 10 handoff; unit-test success does **not** count as device acceptance.

## Decision
A successful Sprint 10 GitHub Actions run permits marking **Android debug release candidate ready for device QA**, not claiming **complete production-quality SRS acceptance** or publishing. Close remaining gaps with explicit post-acceptance work packages; do not fabricate 500 games, social moderation or true AI features. Owner signs off on device tests, merges and release separately.
