# English Mint — UI-only Sprint 1 route and acceptance inventory

Design source: `English_Mint_UI_Sprint1_Codex_Prompt(1).txt` and visual board provided by the product owner. Design is a concept, not an approval of device screenshots.

Baseline: `fix/english-mint-ux-rescue-consolidated` (PR #16), on top of Sprint 10. Branch intentionally stacks on the unmerged rescue; do not merge into main or independently merge overlapping PRs #14/#15.

## Main navigation

| User label | Internal route | Entry | Notes |
| --- | --- | --- | --- |
| Home | Today | bottom tab | daily adventure, Learn/Review shortcuts |
| Games | Games | bottom tab | retained Mint Quests and legacy game modes |
| Clips | Feed | bottom tab | underlying feed route deliberately unchanged |
| Speak | Speak | bottom tab | guided conversation and legacy lessons |
| Profile | Profile | bottom tab | settings and profile disclosure |

## Secondary routes / back behavior

| Route | Parent / entry | Back action |
| --- | --- | --- |
| Learn | Home (Learn a phrase) | Android Back goes Home |
| Review | Home (View progress) | Android Back goes Home |
| Privacy | Profile > Edit profile and settings > Privacy | Android Back returns to Profile (current PR #94) |
| Membership | Profile > Edit profile and settings > Compare | Android Back goes Home (nested stack improvement pending) |
| Speaking Lessons | Speak > Speaking lessons | Switch back to Conversations |
| Game modes | Games > Change game or difficulty | Select other mode or return to Games tab |

Selected main tab persists to existing `speakup_learning` preferences under the new additive `mint_selected_tab` key. Invalid saved route falls back to Home. Internal route IDs, established XP/reward IDs, legacy lesson content and user records remain unchanged.

## UI changes
- Brand palette aligned with conceptual UI Sprint 1 board: off-white `#F8FAFC`, deep night `#0F172A`, night card `#1E293B` and accessible mint variants.
- Bottom labels and route ordering updated to Home, Games, Clips, Speak, Profile.
- Main-tab active indicator recognizes Learn and Review as Home, Privacy and Membership as Profile.
- Hardware Back returns Profile children to Profile, Clips entered from Games to Games, and advanced Home destinations to Home; nested Professional and AI Life flows have local Back handlers. Instrumented navigation verification remains pending.
- Restores selected main tab across app recreation.
- Existing prior UX fixes remain; no new LLM, social, billing or backends.

## QA gates (not equivalent to physical acceptance)
- [ ] Android lint, unit tests, debug APK build on exact branch HEAD
- [ ] Monorepo lint, typecheck, tests and build
- [ ] On-device 320dp/360dp/412dp, gesture/3-button navigation and status bar
- [ ] Font scale 1x/1.3x/2x; TalkBack order, contrast, touch targets and landscape
- [ ] Device screenshot review of Home/Games/Clips/Speak/Profile plus onboarding
- [ ] Verify old stored progress, XP, profile, mastered keys and local reports unchanged after a matching-signed update
- [ ] Confirm the new navigator does not introduce duplicate destinations or inaccessible screens
- [ ] Obtain product-owner approval before merge

## Limitations
This sprint applies foundation/navigation changes; it does not implement the full pixel-perfect board, new artwork/assets, full reusable design primitives, complete nested navigation stacks or instrumentation screenshots. Those remain open acceptance gaps. No production merge or payment activation is authorized.

## Current Sprint 1 design-system extension (PR #94)
- `MintApprovedUi.kt` defines brand mint/teal/purple/navy/off-white and semantic primary, selected-answer and secondary controls.
- Shared progress and empty/error/offline information components added; use them as later UI features are migrated, without claiming all screens already consume them.
- Screenshot reference is a conceptual UI board; exact final imagery and screen-level device parity are **not yet verified**.
- Visual acceptance remains **OPEN**, including typography/illustration parity and 320dp/200% font/TalkBack screenshots. CI success is not device acceptance.

## Device-test feedback — 19 October 2026
Tester reported **8 of 10** guided Sprint 1 scenarios completed with **no problems reported**: Launch, Onboarding, Home, Navigation, Back button, Answer selection, Text and screen sizes, Saved progress. This is user-reported functional acceptance, not independently reproduced automated or screenshot evidence.

**Not yet tested:** Feedback (incorrect/correct answers, explanations, Check → Continue) and Themes (Light, Dark, Follow System). Do not mark either as passed.

**Open visual gates:** approved concept-board illustration parity, screenshot comparisons, distinct small-device breakpoints, 200% font scale, TalkBack traversal, contrast, full gesture/3-button and landscape matrix. No evidence for these specific variants was provided by the device-test summary.

**GitHub verification at feedback intake:** PR #94 head `f32617de78fcba88200bf056ca051c216abeffe0`; CI, Android debug APK and release-gates completed successfully for that commit. PR remains draft and unmerged.

**Triage outcome:** No reproducible device defect was identified by the 8 completed checks; no speculative runtime fix is warranted. Complete the two remaining scenarios and visual/accessibility matrix before Sprint 1 sign-off.

## New product-owner animation direction (19 October 2026)
Apply tasteful, consistent animation to **all screens** where user feedback benefits: button press, navigation, loading, progress, answer feedback, appreciation, daily motivation, streaks, XP rewards, badges, awards and milestone celebrations. Never animate every element continuously; avoid distraction during reading/listening/speaking.

Use one shared motion language: micro-feedback about 150–300ms and screen transitions about 250–450ms; respect Android animator scale and the existing reduced-motion preference. Provide immediate static equivalents, readable text and screen-reader labels when motion is disabled. Never award XP twice or animate fabricated awards. Reward celebrations must consume existing verified progression/reward events.

Implemented foundation: `MintAchievementCelebration` in `MintApprovedUi.kt` accepts caller-controlled visibility, verified award copy and `reduceMotion`. Wiring to the reward engine and screen-by-screen coverage remains pending; do not claim every screen is animated yet.

## Mintverse living-world foundation — 19 October 2026
- New `MintverseGarden.kt` presents an original Compose Canvas plant illustration with growth determined by the **existing completed lesson count**. Stages are mapped to completion thresholds 0, 1, 3, 7, 14; no invented XP, badge issuance or server data.
- Home (`MainActivity.kt`, Today destination) now renders `MintverseGarden` using persisted `completedIds` and the existing `motionReduced` preference. Growth animates 420ms when permitted and immediately updates when reduced motion is enabled.
- Visible text explains the stage and total earned progress. Offline, deterministic display-only behavior; no new permissions or paid integrations.
- Not implemented by these commits: 3D world, character companion, constellation unlocks, storyline, all-screen motion, screenshot fidelity verification. These require separate development and testing.
