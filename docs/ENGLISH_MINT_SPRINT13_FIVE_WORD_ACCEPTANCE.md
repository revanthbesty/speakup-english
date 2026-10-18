# English Mint Sprint 13 — Five-Word Daily Stories

Stacked base: Sprint 12 PR #24 (unmerged). No changes to `main`, application ID, XP awards or `speakup_learning`.

## What has been implemented
- A local story-pack schema with **exactly five distinct alphabetic English words**, four to eight complete scenes, three unique quiz choices, one valid answer, and an age floor.
- Two original studio-authored demonstration packs: *The Lost Basket* and *A Rainy Day*, each with five scenes and five target words. They are local text/emoji artwork, not licensed streamed video.
- Clip-tab entry to a new reader with illustration, captions, device's existing ListeningAudio, bounded Previous/Next scene controls, vocabulary list, quiz and optional Speak-or-Type handoff to the existing speaking activity.
- No automatic XP awarded by quiz selection. Existing reward keys and progression are unchanged.
- Saveable in-session pack/scene indices and choice; navigation uses existing internal route `FiveWord` and highlights Clips as parent.
- Unit tests for five unique vocabulary words, 4–8 scenes, quiz validity and age eligibility; route regression coverage.

## Scope and gaps
This is the initial **offline daily-story MVP**, **not** a published daily editorial scheduling system. Missing: licensed illustrations, formally synchronized prerecorded narration, rotating dated pack schedule, per-word definitions and spaced-repetition persistence, ingestion/editor workflows, user-facing error handling for TTS provider unavailability, live AI and certificate features. The original app's offline Clips remain available separately.

## Manual acceptance gate
- [ ] Android unit tests, lint, debug APK; monorepo checks and release gates on exact commit.
- [ ] Clips > Five-Word Story > five scenes > quiz > replay/next story > optional Speak > Clips/Back.
- [ ] Saveable scene state through device rotation; no index errors from repeat Next/Previous.
- [ ] Speech provider denied/offline behavior; quiet text fallback; no hidden microphone request in story reader.
- [ ] 320dp phones, tablets, 200% font, TalkBack, dark/light/system/night.
- [ ] Upgrade with same signature, confirm legacy profile/mastery/XP and `speakup_learning` survive.
- [ ] Content editorial review and owner screenshots/approval before merge.

No production deployment or paid service activated.
