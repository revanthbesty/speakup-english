# English Mint — Sprint 18 Integration and Release Hardening Audit

Repository: revanthbesty/speakup-english. Parent: Sprint 17 draft PR #29 at `93fe76835ce46778613fb7c677457f7bc603560d`. This PR is **stacked and unmerged**, not a production release.

## Evidence-backed integration baseline
All reviewed predecessor PRs were open and unmerged when the sprint began. Sprint 10 PR #13; UX rescue PR #16; UI Sprints 1–6 PRs #17–22; Post-Sprint-10 Sprints 11–17 PRs #23–29. Multiple legacy repair PRs #14–15 also exist; they must be triaged, not merged independently over the newer UX rescue. The branch follows the intended linear series via its immediate parent; a CI green result does not mean that the chain is present in main.

- Persistent navigation: five tabs Today(Home), Games, Feed(Clips), Speak, Profile.
- Nested: Learn and Review -> Home; Professional -> Home; FiveWord -> Clips; Help, Privacy, Membership -> Profile.
- Existing learning progress, XP and privacy use local storage, notably the legacy `speakup_learning` preferences; no destructive migrations requested.
- Clips contain offline illustrated scenes, not real licensed streaming video. Sprint 13 five-word stories are a two-pack offline demonstration, not a dated publishing service.
- Device speech recognition remains optional with text fallback; no paid inference or certified pronunciation scoring.
- Membership is informational, and no verified production Billing and entitlement restoration exists. Do **not** turn it into checkout.

## Sprint 18 changed files
- `MintUiNavigation.kt`: explicitly parent Professional route to Home without introducing a sixth tab.
- `MintUiNavigationTest.kt`: route and saved-tab restoration regressions.
- `MintReleaseGate.kt`: pure release-readiness contract requiring independently verified Android build, tests/lint, preserved data migration, accessible physical-device acceptance, all-route physical-device acceptance, reconciled integration stack and explicit owner approval.
- `MintReleaseGateTest.kt`: CI alone cannot signal production release eligibility; tests reject each critical omitted gate.
- This report: precise unresolved acceptance.

## Route acceptance matrix — requires physical verification
| Flow | Expected behavior | Evidence state |
| --- | --- | --- |
| Cold start/onboarding -> Home -> start learning | Existing onboarding and lessons retained | Automated existing contracts; physical device pending |
| Home -> Games -> Grammar / difficulty / back | No duplicate questions or XP farm | Physical device pending |
| Home -> Clips -> swipe/scene/caption/word save | Finite, age-filtered, remembered UX | Physical device pending |
| Clips -> FiveWord -> scenes/quiz -> Speak or Type -> Back | No skipped voice attempt; story returns | Physical device pending |
| Home -> Professional -> Industries -> lesson / guided Speak -> Back | Unpublished career courses are honest | Physical device pending |
| Speak -> mic consent/cancel/denied -> quiet text | Provider use disclosed; no fake scoring | Physical device pending |
| Profile -> settings -> Light/Dark/System/Night/reduce motion -> restart | Persistent themes, legible contrast | Physical device pending |
| Profile -> Review/achievements -> Back | XP is evidence-based, no fake certificates | Physical device pending |
| Profile -> Membership / Privacy / Help -> Back | No fake purchasing, privacy controls reachable | Physical device pending |
| App upgrade -> saved learner progress, XP, rewards | Same app ID and signature; migration/no loss | Signed device upgrade pending |
| Compact 320dp, tablet, 200% font, landscape, TalkBack, offline | Touch size, contrast, navigation, graceful fallback | Physical-device/screenshot evidence pending |

## Safety and release blockers
1. Integration: reconcile PR stack and competing branches after a full diff/rebase/conflict pass; do not merge this draft directly to main or deploy.
2. Android: build, unit tests and lint must pass for this exact commit; instrumented and physical-device routes are still separate.
3. Full SRS gaps: production Media3 licensed video feed, CMS rights workflows, 20–30 daily clips, timed five-word publishing, mature offline packs, downloadable videos, SRS vocabulary review, full analytics and notification settings are not verified implementations.
4. Billing: no verified Google Play product IDs, sandbox transactions, subscription restoration or backend-issued entitlements. Leave Free/Plus/Pro informational.
5. Security & privacy: formal child-policy audit, microphone vendor disclosure/denial across devices, licensed media rights and moderation service not completed.
6. QA: actual light/dark screenshots, TalkBack, small/large layouts, performance and crash metrics, upgraded-install preservation and sign-off not available.

## Go / no-go
The new gate is a reusable checklist logic contract, **not connected to a deployment pipeline**. This is deliberate: there is no authority to merge, deploy, change Play publishing or billing. CI green can qualify the debug build for testing but **not** the whole product for release.

**Current release recommendation: NO-GO** until PR stack reconciliation, complete Sprint 1–18 issue closure, physical-device accessibility/data upgrade evidence and explicit owner approval. Remain draft.
