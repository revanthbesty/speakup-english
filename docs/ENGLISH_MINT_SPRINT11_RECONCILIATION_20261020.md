# English Mint Sprint 11 — Reconciliation checkpoint (20 October 2026)

Source: current active PR #94, base commit `57b82a4cd47646d9aca68d98c64dae68c3436ca0`. Separate stacked branch; no merge or deployment.

## Evidence and implementation
- Historical Sprint 11 PR #23 supplied the opt-in Pause & Speak dialog; current `MintFeedScreen.kt` already includes it, its suggested authored line, Skip, and handoff to the existing Speak/Type flow. Avoid cherry-picking the old patch.
- Historical Sprint 12 PR #24 supplied bounded scene navigation and playback rules; current `MintClipsPlayback` and `MintFeedScreen` already integrate them.
- Sprint 4 extracted Profile, Games, Grammar, Vocabulary and Riddles screens. Learner settings, XP and persistence remain owned by `MainActivity.kt`.
- Fixed the featured Games Hub button: the prior callback reselected the hub sentinel `Mint Quests`, displaying the same screen. It now opens the existing playable `Word Builder` mode. A pure Kotlin contract and JVM regression test enforce a valid target.
- No preferences, database schema, app ID, rewards, game IDs, microphone permissions, or billing rules were changed.

## Current scope and remaining gates

| Gate | Evidence/status |
| --- | --- |
| Featured game button code path | Fixed in current branch; runtime UI click still needs device verification |
| Featured mode contract JVM test | Added; CI result required before marking passed |
| Offline illustrated Clips | Existing; not streaming video |
| Pause & Speak with optional Skip | Existing; no credit on skip in presentation callback |
| Return to exact prior Clip after Speak tab handoff | Not confirmed; requires navigation persistence/device test |
| Android JVM unit/lint/debug APK | Await current-branch CI |
| Android API 29/35 instrumentation | Not verified; emulator runs required |
| Dark/light, TalkBack, 320dp and tablet screenshots | Not verified |
| Matched-signing data-upgrade test | Not verified |
| Full approved UI designs and content coverage | Not finished; owner acceptance required |

## Architecture and cost boundaries
The current feed is an offline captioned illustrated-story component. Media3/ExoPlayer, adaptive media buffering, licensed video packs, signed catalog versioning, editorial approval, CDN, cache/download manager, authenticated moderation and consent-aware speech feedback are future design requirements, **not** deployed capabilities. A future optional media adapter must validate licensing and checksums, bound disk storage, persist resumable scene positions, and retain offline text fallback. Recording must remain user-initiated and privacy-aware. Free learning, accessibility and text practice must continue without subscriptions. Do not activate a paid service in this sprint.

## Release decision
Draft / no-go until latest head CI and APK pass, API 29/35 instrumentation and user-flow tests are evidenced, the approved screenshots are accepted and the owner authorizes progression. Do not merge PR #94, historical PRs #13/#23/#24 or this branch into main automatically.
