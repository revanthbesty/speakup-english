# English Mint — Rejected Android UI: full approved-design acceptance matrix

Owner device review (19 Oct 2026): existing build NOT APPROVED. Screenshots show text-heavy Home, grammar, progress, profile/settings and repetitive pilot/release notices. This document is an acceptance gate for continuing UI-only implementation; it does not imply these changes are complete.

## Source-of-truth hierarchy
1. English_Mint_Master_SRS_with_UIUX_Designs.docx and UI Sprint 1–6 approved visual boards/prompts (consult originals, not descriptions alone).
2. Approved onboarding and screens for the five persistent tabs: Home, Games, Clips, Speak, Profile.
3. Screenshot evidence of the current build identifies defects, never visual approval.

## Rebuild screen inventory
- [ ] Welcome/splash: mint visual identity, illustration, clear Continue/Skip; no microphone access until speaking.
- [ ] Onboarding: four short steps welcome, age, learning goal, ready; optional assessment; restore/back/skip; age-band and goal data persist.
- [ ] Home: one primary daily adventure CTA above fold, continue lesson and learning path; discover routes in compact category tiles; Garden/Coach/AI Life advanced pilots behind Explore More, not large first-screen sections.
- [ ] Games: illustrated browsable Mint Quest, Grammar, Vocabulary, Stories, Real-World Quests, Co-op Stories, listening/reading categories where implemented; single entry point per feature, no duplicate hubs.
- [ ] Grammar: one-question-at-a-time card, explicit answer selection/check action, explanations for correct AND incorrect answers, progress and review flow; do not represent 18 pilot questions as a verified 10,000 bank.
- [ ] Stories/co-op/quests: dedicated immersive activity screens, clear instructions, progress and back-to-hub controls.
- [ ] Clips: video/media card, easy captions/replay/vocabulary, offline-availability truthfulness.
- [ ] Speak: guided conversation and microphone modes separated, typed fallback, permissions requested at use.
- [ ] Progress: compact skill dashboard and rewards with accessible drill-down, not repeated long counters/empty progress bars.
- [ ] Profile: learner summary and links to separate Settings, Help, Privacy, Appearance, Achievements and Membership pages; beta/release messages in About/Development, not main learner UI.
- [ ] Utility states: empty, loading, error, offline, dark/light/night, large font, orientation/tablet where applicable.

## Non-negotiable acceptance
- [ ] Exact approved color tokens, illustrations, spacing, typography, radii, iconography and layouts copied from approved boards; no generic dark-card replacement.
- [ ] Five persistent accessible tabs in the agreed order Home/Games/Clips/Speak/Profile; single coherent back stack; clear current state.
- [ ] Existing features, grading, lesson completion, rewards anti-duplication, data/local storage and accessibility preserved; no paid AI API/token costs.
- [ ] Screenshots for EACH screen and onboarding step (light and dark), compared side-by-side against approved design; physical-device owner approval required.
- [ ] No clipped headers or content behind system/status/bottom bars; 48dp touch targets; TalkBack; adequate contrast; large font; keyboard navigation.
- [ ] Verify Android API 29 and 35 build/instrumentation; report actual CI artifacts, tests and known failures.
- [ ] No merge or deployment until all visual and functional acceptance gates pass and owner explicitly signs off.

## Delivery tracking
PR #94 is PHASE 1 ONLY, covering the first illustrated onboarding + simplified Home + tab icons. It is NOT a full UI rebuild. Subsequent implementation PRs must link to this checklist and provide proof of each completed screen.
