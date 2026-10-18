# English Mint Sprint 22 — Speaking Growth Passport (first privacy-safe increment)

Recovered original roadmap: Sprint 22 **Speaking Passport** requires consent-based recording comparison, export and deletion, with privacy/deletion end-to-end QA as its exit gate. The current code delivers a **strictly narrower local notes-only prototype** and must not be declared a finished Sprint 22.

## Implemented
- Speak screen Passport section with disabled-by-default consent toggle `mint_passport_consent`.
- Optional 7/30/90-day reflection prompt templates; locally stored self-authored text with basic length/format validation and dates.
- Saved history from dedicated `mint_passport_reflections` SharedPreferences key; delete-all action removes stored Passport notes.
- No microphone recording, audio saving, uploading, external speech analysis, generated confidence score, social sharing or fake rewards.
- Tests of note validation/round trip, invalid input rejection and empty deleted history.

## Missing and blocked
- Consent-based real *voice recording* with explicit microphone permissions, encrypted private storage, retention deadline and safe deletion.
- Verifiable export and ownership/deletion of recordings; audio waveforms/comparison against equivalent prompts, accessibility and recording error handling.
- No scope expansion to cloud transcripts, paid APIs or public sharing without additional owner approval and privacy review.
- Manual 320dp/tablet/TalkBack/large text, dark/light/reduced motion and physical device QA.
- The Reward Universe should reward genuine optional practice but **never** award a Voice Bloom from writing a note or toggling consent.

## Acceptance for current prototype
1. Fresh install: Passport consent off; no saved notes.
2. Enable, choose each prompt, save a reflection, relaunch: notes are preserved.
3. Disable consent: editing/history hidden. Reenable: existing notes visible until deleted.
4. Delete all notes: they remain absent after restarting; existing lessons/XP and speech state are preserved.
5. Notes cannot exceed 240 chars; invalid data not silently approved.
6. CI, Android build, lint and unit tests run for current branch; device review remains necessary.

**Draft only. No merge, deploy, billing, app-ID changes or irreversible migration.**
