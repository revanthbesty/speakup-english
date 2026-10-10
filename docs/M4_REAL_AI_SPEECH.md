# M4 Real AI Provider + Speech Boundary

Adds a provider-agnostic speech gateway for STT, TTS, pronunciation and fluency. Production providers are configured server-side and remain behind the AI gateway boundary. Mock speech remains available for deterministic development.

Endpoints: POST /api/v1/speech/transcribe, /synthesize, /pronunciation, /fluency; GET /api/v1/admin/ai/speech/providers.

Raw audio is not persisted by this module. Technical provider failure returns a technical-failure response and must never be converted into learner failure, XP loss, mastery loss, or incorrect evidence.
