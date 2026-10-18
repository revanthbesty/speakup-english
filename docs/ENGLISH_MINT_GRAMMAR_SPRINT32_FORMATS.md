# English Mint Grammar Arena Sprint 32 — Multimedia Formats Pilot

## Scope
A small, original, safe and offline-first increment introducing dialogue, listening-style and illustrated-cue grammar practice. Three original items only; no 10,000-question corpus or rights-cleared audio packs.

## Implemented
- Games > Grammar Formats, separate from the existing 18-question Grammar Arena pilot.
- Dialogue gap fill with contextual explanation.
- Listening exercise: optional on-device TextToSpeech with always-visible transcript fallback; TTS availability depends on device speech engine. No cloud audio service, recordings, paid model or API.
- Visual-context exercise with emoji pictogram rather than licensed images.
- Three choices and answer-specific correctness message plus grammar-rule explanation, retry without leaking answer as a reward.
- No XP, Mint Bloom or mastery mutation for preview items.
- Pure model tests verifying formats, unique ids and answer keys.

## Pending to claim full Sprint 32
- Reviewed high-volume dialogues, artwork rights, original audio assets and accessibility transcripts.
- Audio engine behavior on devices with no TTS engine, missing language support or changed settings.
- Screen reader/focus order, font scaling, tablet, dark/night and low-end performance testing.
- End-to-end integration and real device QA; all three checks must pass against exact PR head.
- User-provided 9,500 / 10,000-question bank import remains a separate audited content pipeline, not implemented here.

## Manual checks
1. Games > Grammar Formats: switch between Dialogue, Listening and Visual; verify state reset.
2. Press Play device voice with a TTS engine; verify transcript stays visible and unavailable fallback is truthful.
3. Choose incorrect answer and read rule feedback; retry correct response.
4. Restart and verify existing Grammar Arena mastery, bookmarks, reward history and XP unchanged.
5. Confirm offline operation, no microphone request, no network account service, no duplicate awards.

Stacked on Sprint 31 draft PR #52. No merge, rollout or deploy.
