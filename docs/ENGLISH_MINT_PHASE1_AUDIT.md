# English Mint — Phase 1 repository audit and migration architecture
Date: 2026-10-18 | Source: English Mint Master SRS v1.0 (15 pages), repo `revanthbesty/speakup-english`.

## Verified baseline (not projected completion)
- `main` is a pnpm/TypeScript monorepo: `apps/web` (Next.js 15/React 19), `apps/admin` (Next.js), `apps/api` (TypeScript API + PostgreSQL), `database/migrations/001..016`, contracts and tests. Main Android includes a Compose skeleton; the actively developed Android app is on `build/android-debug-apk`.
- The Android development branch contains a large Kotlin/Compose `MainActivity.kt` with onboarding, lessons, Today, games, speaking, progress, profile and local SharedPreferences. Its last audited debug APK CI completed successfully before this modernization. The Android code is currently monolithic (~107 KB), which is a refactoring risk.
- Android namespace/application ID: `com.speakup.app`. Existing persisted file: `speakup_learning`. **Neither is renamed in Phase 1**, to retain APK update compatibility and local scores. Current `versionCode=1`, `minSdk=26`, `targetSdk=35`.
- Existing Android microphone functionality relies on an external Android speech recognizer intent; this does NOT establish reliable streaming AI speech recognition or licensed AI scoring. Existing reward bookkeeping includes locally persisted XP, completed lessons, puzzle awards and revision counts. Not cloud-synced in Android.
- API modules present: identity/password/session, onboarding, diagnostic, adaptive recommendation, speaking/evaluation and mock speech, AI gateways, gamification/rewards and daily modules. Provider configuration defaults can run with mocks; do not assume paid live AI integration.
- The monorepo includes PostgreSQL migration files, but the Android local SharedPreferences data is not automatically reconciled with server tables. No tested account merge or cross-device sync path was established in this audit.
- `.github/workflows/ci.yml` handles pnpm lint, typecheck, tests and build on PR; Android workflow builds debug APK, runs unit tests and lint on configured branches.

## SRS gap analysis
| Workstream | Confirmed existing baseline | Gap / implementation boundary | Proposed phase |
|---|---|---|---|
| Brand | SpeakUp Android label and wordmark | English Mint label, tokens, adaptive icon, original mascot/art rights, store clearance | 1–2 |
| Learning journeys | 5 legacy path IDs and learner type inputs | 5 new journeys, validated ages 4–7/8–12/13–17/18–49/50+, per-skill calibration | 2–4 |
| Games | Word Builder and other existing activities | reusable 50 mechanics, truly curated 500 variants, educator QA | 3, 8 |
| Speaking | external recognizer, prompts and practice | consent, accurate ASR confidence, recorded-session opt in, multilingual speech, safe real AI | 4 |
| Home and UX | Today/Learn/Games/Review/Profile and existing themes | SRS Home/Feed/Games/Speak/Profile IA, reduced motion, scheduled night, animations | 1–2 |
| Rewards | Local XP, persistent one-time puzzle awards | server ledger, coin economy, idempotent cross-device merge, authentic certificates | 5 |
| Feed/social | No independently verified production moderation | licensing, captions, finite feed, social gates, reports/blocks, staffed moderation | 6 |
| Monetization | No verified Android Play Billing server integration | server-owned Free/Plus/Pro, restore/cancel/grace; no live purchases pending approval | 7 |
| Security/privacy | API sessions and crypto; local progress | age verification, guardian workflows, export/delete, consent, retention, audits | 6–8 |
| QA | Existing CI and unit tests | test coverage by feature, migration fixtures, device + TalkBack + 200% font, pilot metrics | all |

## Safe migration strategy
1. Preserve `com.speakup.app`, Kotlin package names, database migrations, all existing SharedPreferences keys and original XP/achievements. Change **only user-facing** application name now.
2. Store new `mint_theme_mode` preference separately; if absent, use old `dark_appearance=true` as a legacy fallback, otherwise follow system. Do not overwrite the old key.
3. Adopt the SRS Appendix C palettes (#F6FAFA/#101923 canvas, #102A33/#E8F3F3 foreground, mint accents). All colors still require device-side contrast testing. No claim that dark mode prevents harm.
4. Gradually extract Android UI, domain and persistence into modules; keep backwards-compatible adapters and feature flags instead of a rewrite.
5. Before any server data migration: produce anonymized/synthetic old-state snapshots, rollback path, conflict semantics (e.g., MAX earned progress with unique event IDs), and repeatable tests. Account linking requires explicit consent.
6. Do not turn on paid AI, cloud storage, public feeds, child messaging or billing until provider credentials, budget, policies, moderation operators and safety checks are approved.
7. Maintain a safe PR from `feature/english-mint-phase-1` into `build/android-debug-apk` (Android baseline); do not merge or deploy automatically. `main` has different web/API assets, so integration back into `main` must be reviewed rather than assuming equivalence.

## Phase 1 actually implemented in this PR
- English Mint launcher and header/tagline, preserving APK identity.
- Design tokens aligned with the SRS, night-friendly alternate surfaces.
- System/Light/Dark appearance override with legacy dark preference fallback.
- `MintIdentity` contract and JUnit tests locking brand identity and local progress key list.
- PR Android workflow for lint, unit tests and build; traceability and audit documentation.
- No new network providers, billed features, user data deletion or production deploy.

## Decisions requiring explicit approval later
- Commercial trademark/domain clearance; final brand asset ownership and mascot license.
- Child privacy policy and country-by-country age consent, guardian identity and staffed moderation.
- Actual speech/AI provider and cost envelopes; billing products, price localization and revenue assumptions.
- Storage retention durations, consent wording, safe content production and education assessment rubrics.

## Phase 1 exit conditions
Code compiles and Kotlin/JUnit + Android lint pass on the PR; Android debug artifact built. Existing progress keys and package name unchanged. Device-scale/contrast/upgrade tests remain a **deferred** gate until the user chooses to review after Sprint 10; not falsely counted as passed.

## Follow-up: level-appropriate placement (2026-10-18)
- Replaced the fixed three-question diagnostic with four content banks: Easy, Medium, Hard and Exam. Each bank has six validated-answer items; each short session draws three, rotating between sets on subsequent attempts.
- Initial placement bank follows the learner's selected difficulty, learning journey and exam goal; learner can explicitly override it in the assessment without affecting XP or lesson history.
- Stores the latest bank in `placement_level` and retake offset in `placement_attempt` in the original `speakup_learning` preference file. Old `placement_score` and `placement_done` remain intact.
- JUnit coverage in `MintPlacementBankTest.kt` checks answer integrity, bank separation, retakes, level selection and recommendations. This diagnostic is for initial routing only, not certified proficiency or psychometrically validated adaptive testing.
- Follow-up roadmap: educator-reviewed item pools, per-skill vocabulary/listening/speaking/reading/writing calibration, age-banded presentation and adaptive item selection based on response accuracy.
