# English Mint — Sprint 14 Speaking Safety and Consent Boundary

Stacked base: unmerged Sprint 13 PR #25. No production merge or deployment.

## Evidence from the existing application
- Android invokes the external device speech recognizer via `RecognizerIntent.ACTION_RECOGNIZE_SPEECH` after an in-app explanatory consent dialog. This is **not** a paid LLM nor an accuracy/phoneme scoring system.
- When the provider returns recognized words, existing speaking UI places the transcript into the editable response, and its legacy attempt count increases only for a nonempty recognition result.
- Quiet text input is available without speech recognition. No raw-audio recording storage was introduced.
- Clips already had optional Pause & Speak, Suggested Reply and Skip (Sprint 11); five-word stories had a direct optional speaking handoff (Sprint 13).

## Sprint 14 changed
- `MintSpeechSafety.kt`: explicit outcomes NOT_STARTED, SKIPPED, CANCELLED, UNAVAILABLE, TEXT_SUBMITTED, RECOGNIZED; only RECOGNIZED is a valid recognized-voice outcome; **no** outcome provides a pronunciation accuracy score.
- `MintSpeechSafetyTest.kt`: all-outcome tests for non-fabricated scoring and skipped/unavailable messaging.
- `MainActivity.kt`: clarify device-provider disclosure, optional text fallback, no raw recordings, unscored recognition and distinct no-attempt cancellation/errors. Existing recognizer, XP and data storage untouched.
- `MintFiveWordStoryScreen.kt`: story-end optional speaking dialog with Suggested Reply, Speak or Type, and Skip. Skipping does not route, ask permission or log a voice attempt. Existing Clips overlay retained.

## Route matrix
| Action | Target | Evidence / note |
| --- | --- | --- |
| Clips > Pause & Speak > Suggested Reply | Offline authored line | No speech recorded |
| Clips > Skip | Back to Clips | Does not log speaking |
| Clips > Speak or type | Existing Speak lesson prompt | Further mic permission optional |
| Five-Word Story > Pause & Speak > Suggested Reply | Authored scene | No generated AI claim |
| Five-Word Story > Skip | Reader | No speech attempt |
| Five-Word Story > Speak or type | Existing Speak route | Text-only route available |
| Speak > Microphone | Device recognizer with in-app disclosure | Android/device privacy terms apply |
| Recognizer canceled/absent/denied | Speak text fallback and clear status | No fabricated pronunciation score |

## Remaining Sprint 14 work / blockers
- Actual on-device microphone permission outcomes and accessibility/large-font screenshot tests remain unverified.
- No paid LLM, pronunciation-quality service, phoneme/scoring rubric, secure per-attempt backend, speech recording retention UI or server feedback. These need separate privacy review, explicit approval and end-to-end tests.
- Confirm real device recognition, cancel, denial, provider absence, offline text and age-safe operation; capture screenshots in dark/light/night and tablet/320dp.
- Run exact final-commit unit tests, Android lint/debug build, monorepo CI, release gates; preserve existing app ID `com.speakup.app` and preference file `speakup_learning`.
- Keep this PR in draft pending user approval and stacked integration.

No paid API, billing, moderation upload, data migration or production deployment.
