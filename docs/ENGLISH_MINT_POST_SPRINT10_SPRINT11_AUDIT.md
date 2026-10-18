# English Mint — Post-Sprint 10 Expansion / Sprint 11 Baseline and Safe Increment

**Status:** Draft proposal plus first UI-only prototype. **Not production or device accepted.** Source: `feature/english-mint-ui-sprint-6-profile-quality` at `35c0acd1ab6824ea1bc2996c48972f1281cd2eec`, which is stacked on **unmerged** PRs #21, #20, #19, #18, #17, #16 and Sprint 10 PR #13. `main` is not assumed to contain this stack. Do not independently merge the overlapping PRs.

## Evidence-based feature matrix
| Expansion requirement | Existing baseline evidence | Sprint 11 action | Remaining |
|---|---|---|---|
| Clips vertical feed | `MintFeedScreen.kt` has finite, offline captioned `MintFeedCatalog` scenes, swipe up/down, next/previous, captions, manual/timed frames and local safety actions | Reuse current player; do **not** market as streamed video | Media3 player, buffering/low-data, playlist media rights and adaptive playback |
| 5-word daily stories | Existing offline stories are illustrated scenes but have **no validated 5-word StoryPack and release/version schema** | Definition and validation proposed; no misleading published packs | Exactly 5 target words, 4–8 scenes, narrated synchronized media, review schedule, word metadata, cache |
| Pause-and-speak | Existing Clips links to Speak and existing Android speech recognition/quiet text | **Implemented:** optional paused modal with suggested reply, Speak-or-Type or Skip | Permission/retry state matrix, user-consented feedback adapters, accuracy QA |
| Night comfort | `MainActivity.kt` stores system/light/dark/night and reduce motion/eye comfort preferences | Retained | Caption-size setting, deep contrast/flash accessibility verification |
| Curated daily content | Finite studio-authored local `MintFeedCatalog`, hidden/local reports only | No publishing service enabled | Moderated CMS, editorial approvals/rights/version/rollback |
| Rewards | Existing XP and mastery ledger with unique award identifiers | No reward code touched; skipping Speak does not award XP | Active retrieval outcome auditing and analytics |
| Monetization | Existing informational Free/Plus/Pro UI | Unchanged, no billing | Entitlement backend, approvals, sandbox restore |
| Personal data | `com.speakup.app` Android application and `speakup_learning` legacy preference records | No changes to package, preference keys or storage logic | Matched-key upgrade test on real device |

## Visual design-to-system reconciliation
The newly attached Post-Sprint 10 board (Mint Clips / Five Word Story / Speak Moment / Night Mode) is a conceptual design reference. The older UI 1–6 stack adopts exactly five bottom tabs **Home, Games, Clips, Speak, Profile**. New daily stories are contextual from Home/Clips; new speaking moments are overlays on Clips, not additional bottom tabs. SRS counts (five words, 4–8 scenes, 20–30 clips/day) are future specifications, not current catalog metrics. Theme proposal: light #F4FAF8/#FFFFFF, dark #101B28/#172636, mint #0C9F81/#43DDB3, violet #7852D6/#AD8AFF, readable text roles. No asset/licensing changes yet.

## Proposed rollout and gates
| Sprint | Deliverable | Acceptance gate | Cost assumption/risk |
|---|---|---|---|
| 11 Audit / architecture | Baseline, route matrix, UI prototype, flags | Compile, unit/lint, review; no rollout | Local prototype $0 external spend |
| 12 Clips MVP | Vertical accessible player, captions, pause, error state | Swipe/rotation/audio/device performance tests | Media delivery/licensing not free; require approval before cloud spend |
| 13 Daily Story | Five-word packs, 4–8 scenes, vocabulary and offline | Exact-schema tests, consent-free read, rights checks | Human-edited original artwork/content time |
| 14 Speak overlay | Permission boundaries, quiet text, suggested reply, safe feedback | Denied mic/low confidence/retry/child-safe tests | On-device speech only unless explicit provider approval |
| 15 Content studio | Rights ledger, human moderation, versioned publishing | No unreviewed/uncleared content publicly exposed | CMS/media/rights costs require quote/approval |
| 16 Recommendations | Finite diversified ranking, recall XP, review analytics | Replay cannot farm XP; offline/age tests | Analytics cost unknown |
| 17 Visual & accessibility | Light/dark/night, text scaling, reduce motion, performance | TalkBack/320dp/tablet/low-end-device screens | Art/QA resources |
| 18 Release hardening | Existing entitlements, safe migrations, staged build | Full regression, signing & restore, device acceptance, explicit release approval | Provider/Play billing costs scoped separately |

## New user flow: Clips -> Speak Moment
1. Open a curated clip from Clips and view a scene.
2. Tap **Pause & speak (optional)**; playback is paused before dialog.
3. Tap **Suggested reply** to view a sample existing authored line. No personal speech recording takes place here.
4. Tap **Speak or type** to hand off to the existing speaking experience; permission is still requested there only when a mic is chosen.
5. Tap **Skip** to dismiss without awarding XP or logging completion. No fabricated speech-quality score.

## Required QA matrix (not yet device verified)
- [ ] Exactly five main tabs, Clips to overlay and back, scene Next/Previous, up/down 10+ rapid swipes, resume index after recreation.
- [ ] Overlay while autoplay is active: pauses reliably, dismiss/cancel/Skip do not log a speech attempt, suggested reply toggles.
- [ ] No-mic/denied permission, child age filter, no unmoderated social features, no external billing or AI.
- [ ] Dark/light/night readability, 320dp and tablet/large fonts, TalkBack, reduced motion.
- [ ] Matched-signing Android migration preserves app ID, `speakup_learning` user records, mastery, rewards.
- [ ] Android unit/lint/debug compile and monorepo CI; exact source SHA recorded in PR.
- [ ] Physical device screenshots and explicit owner approval before merge or deployment.

## Known release blockers
A streaming video backend, approved media catalog, voice feedback service, five-word daily pack content, CMS and rigorous physical-device QA do not yet exist as verified deliverables. Avoid calling the prototype complete or activating any paid provider, production analytics, billing or social activity.
