# English Mint v1.0 — Release Readiness / NO-GO

**Baseline:** Sprint 11 draft PR #96 at `2b547a6f76be333d0985fadcca2d69cc78597123`. New sprint is stacked on that branch. **No merge, store publication, production rollout or paid-service activation authorized.**

## Evidence verified at kickoff
- Android debug APK workflow #332 passed and uploaded artifact 12220509181.
- Monorepo CI #413 and release-gates #413 passed.
- Android app version remains `0.10.0-rc1` with `com.speakup.app`, minSdk 26 and targetSdk 35.
- Sprint 11 existing optional offline Clip pause/speak and Sprint 12 finite scene playback present.
- Sprint 4 / Sprint 11 UI changes exist, but owner previously rejected full visual appearance.
- Prior PRs are stacked and open; green checks on one draft cannot validate merging all historical work.
- Device instrumentation on API 29 and 35, signed-upgrade preservation, TalkBack/screenshots, Play-ready AAB and owner sign-off remain unverified.

## MVP launch scope
Core onboarding, Home, Games (authored and actually enabled), Grammar, Vocabulary, Stories/Clips as honest offline illustrated content, Speak with typed fallback, local Progress, Profile, privacy, saved data and accessible preferences. Do not market planned LLM, streamed video, 9,500-question bank, certificates, cloud sync or active premium billing as shipped.

## Mandatory ordered release gates
1. Reconcile feature/route inventory and remove duplicate/dead entry points without deleting functional content.
2. Verify complete Android test/lint/APK build and web CI on final SHA.
3. Compile instrumentation and execute full tests on API 29 and API 35; retain logs.
4. Exercise onboarding → lesson → XP → Games → Clips → Speak → Profile → restore and Back flows.
5. Verify idempotent XP, persisted learner settings/achievements/pets, age filtering, offline fallback and privacy controls.
6. Upgrade a disposable test install with **matching signing key**; verify local histories; never uninstall real learner data for QA.
7. Complete accessibility sweep: TalkBack, keyboard, 320dp, tablets, font scaling, reduce-motion, dark/light/night.
8. Capture screenshot comparisons with approved UI Sprints 1–6; obtain explicit owner visual approval.
9. Complete privacy/security/disclosure and content/editorial rights review. Do not claim cloud moderation/AI capabilities not implemented.
10. Create verified signed release AAB using a safe secret-handling path; inspect package/version/permissions; prepare truthful store metadata.
11. Obtain explicit final owner approval. Only then may a separately authorized process publish.

## Fail-closed policy
`MintV1ReleaseGate` is a pure Kotlin evidence checklist with JVM tests. It is **not wired to GitHub deployment environments, cryptographic attestation, or the Play Console**; it should not be represented as a server-side blocking control. Treat this document as an acceptance checklist, not proof that gates have passed.

**Decision at kickoff: NO-GO.**

## 20 October — Additional emulator regression coverage
- Android API 29 and 35 emulator smoke-test workflow #3 passed on `4337a2e`. It checked Activity launch/recreation, package identity, legacy preferences accessibility, and pure navigation contracts. It did **not** exercise screen taps, restore data across signed APK versions, or constitute full visual sign-off.
- Expanded instrumentation includes a temporary and cleaned-up `speakup_learning` preference probe across Activity recreation and five unique bottom-tab labels. These new tests require passing API 29/35 results at their final commit before they can be considered verified.
- Further acceptance still requires real UI interactions, matched-signing installation over an existing build, varied viewport screenshots, accessibility and owner review.
