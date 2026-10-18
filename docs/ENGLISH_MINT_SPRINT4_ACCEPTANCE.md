# English Mint Sprint 4 — Speaking-first studio, privacy and AI boundaries

## Verified baseline
Branch from Sprint 3 (`feature/english-mint-sprint-3`). Existing Android speech recognizer is a delegated system `RecognizerIntent`; existing `ListeningAudio` uses local Android TextToSpeech. The monorepo contains server-side speech/AI abstractions, but live paid provider credentials, prices, audio retention compliance and child consent remain unapproved.

## Implemented
- `MintConversationEngine.kt`: deterministic offline conversational practice, six curated situations (introductions, travel, team collaboration, job interview, debate, professional presentation) and 18 authored dialogue turns spanning beginner/intermediate/advanced, age filtered. Each turn is tagged Speak-to-Complete, Listen-then-Speak or Play-then-Speak.
- `MintGuidedConversation.kt`: choice of level and scene, three-turn progress, speaking prompt and model response, listening playback through existing Android TTS, hint, recognized response or quiet text, feedback, next/restart navigation and completion progress.
- `MainActivity.kt`: direct Speaking Studio tabs for Conversation Quests and preserved lessons. Voice game follow-ups and legacy speaking practice remain operational.
- Permission/privacy: an explicit disclosure is shown before the Android external recognizer starts. Users can dismiss; if recognizer is missing or blocked, quiet text practice remains available. No invented continuously recording microphone UI.
- Speech confidence and pronunciation are **not** scored. The engine does not know real speech-recognition confidence and explicitly communicates uncertainty, including on accent variation. Recognition or keyword evidence is treated as coaching, not certified proficiency.
- Persistence stores only unique voice-practised turn keys (`mint_guided_voice_turns`) in the existing `speakup_learning` preference file. Transcripts remain in the active UI state and are not deliberately persisted/uploaded by English Mint; the device's external recognition provider may process audio independently. Quiet text completion is never stored as a voice turn.
- Existing package ID, app data, XP, completed lessons, games, puzzles, existing speaking counts and achievements unchanged.

## Tests
`MintConversationEngineTest` covers unique scenes/turns, age restrictions, level selection, three modalities, voice-vs-text completion, empty/short transcript handling, and non-numeric supportive feedback.
Android CI must pass `:app:testDebugUnitTest`, `:app:lintDebug` and `:app:assembleDebug`. Monorepo CI and release gates must be green on the PR head.

## Safety and production boundaries
The new roleplay is **scripted offline conversation**, not a deployed live AI language model. A real AI provider must be configured in server-side gateways only after the user approves its provider, terms, pricing and credentials; never embed provider keys in APKs.
The speech recognizer is OS-provider delegated, can be unavailable, and may process audio externally. Consent disclosures, withdrawal, guardian consent, provider regional privacy and retention policies require release review. No raw audio recording or playback of user speech is implemented; do not represent TTS playback of prompt as recording replay.
Full UI/device accessibility, microphone denial on hardware, backgrounding, no-network and performance acceptance are deferred to Sprint 10 per user instruction. No app installation requested before then.

## Remaining product work beyond this sprint
Provider-backed adaptive AI roleplay and safe prompt policies, meaningful verified pronunciation/fluency assessment, opt-in recorded audio playback and delete/export, secure server sync and cross-device history, guardian verified consent, regional content QA, and an extensive speaking curriculum.
