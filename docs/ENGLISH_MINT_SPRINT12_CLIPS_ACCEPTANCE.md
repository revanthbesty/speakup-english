# English Mint Sprint 12 — Finite Clips Playback MVP

Base: draft PR #23 (`feature/english-mint-sprint-11-expansion-safety`), stacked atop unmerged PR #22 and its predecessors. **No production merge.**

## Implemented
- `MintClipsPlayback`: pure finite-navigation and progress rules with explicit empty-list bounds, swipe threshold, frame advancement, and autoplay eligibility.
- `MintFeedScreen`: uses bounded player rules; vertical swipe does not wrap; same story remains at feed ends; frame indicator remains bounded; safe next/previous and new swipe hint.
- Scene autoplay checks reduced-motion, paused state, word-context dialog, speaking moment and report/safety dialog states.
- Opening audio controls, safety controls, private reaction and speaking prompt pauses animated scene playback.
- Existing captions, original offline illustrated scenes, story challenge, local-only reporting, age filtering and optional speaking handoff remain intact.
- Unit tests for finite swipes, progress and reduced-motion/dialog-autoplay rules.

## Honest scope
This is an **offline illustrated scene-player MVP**, not streamed video. No approved video media assets, Media3 player, adaptive streaming, buffering/download manager or backend exists in this PR. Do not present screenshots as streamed video and do not claim the 20–30/day editorial target is live. No AI provider, billing, voice storage, social posting or rewarded behaviour changes.

## User flow
Home/Clips or Games/Stories -> age-appropriate finite story -> view timed/manual scenes -> swipe up/down or Next/Previous Story -> show/hide captions and optional audio -> contextual word sheet -> quiz -> optional Pause & Speak -> Speak/Type or Skip -> natural end-of-feed state.

## Required acceptance and outstanding Sprint 12 deliverables
- [ ] Android unit test, lint, debug APK, monorepo CI and release gates on **exact final commit**.
- [ ] Device test: compact 320dp, typical 360/412dp, tablets, landscape, dark/light/night, 200% fonts, TalkBack, gesture and three-button navigation.
- [ ] Verify 10+ rapid swipes, age-filter/hidden-story changes, no index crashes, foreground/background, rotation, offline and unavailable device audio.
- [ ] Play/Pause/Next/Previous, caption toggles, dialog pause, reduce-motion, swipe competing with parent scroll, end-of-feed stopping.
- [ ] Existing XP once-only award, settings and saved progress unchanged after signed upgrade.
- [ ] Original/licensed short-form video assets, Media3 player with seek/buffering, caption timing, data saver/cache and downloadable video require separate implementation and approval.
- [ ] Owner screenshots and explicit device approval before merge or release.

## Change restrictions
Do not modify `com.speakup.app`, existing `speakup_learning` progress, award IDs or signed installation path. No paid services, social network, live AI or production deployment.
