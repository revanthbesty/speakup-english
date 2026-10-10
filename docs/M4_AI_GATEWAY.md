# M4 Production AI Gateway + Model Registry

## Contract
All learner-facing AI calls enter through the AI Gateway. Domain modules do not call providers directly.

## Guarantees
- Provider/model abstraction
- Capability-based routing
- Timeout and circuit-breaker foundation
- Structured output boundary
- Safety pre-check
- Fallback to deterministic/mock provider
- No learner input/output written to ordinary logs
- Provider health endpoint
- Configurable endpoint/model/API key
- Interaction telemetry schema

## Endpoints
- `POST /api/v1/ai/generate`
- `GET /api/v1/admin/ai/providers`

## Production hardening still required
- Provider-specific adapters and response schemas
- Persistent interaction telemetry wiring
- token/cost extraction from provider responses
- golden evaluation datasets
- prompt/config version registry persistence
- advanced moderation and prompt-injection isolation
- real STT/TTS/pronunciation provider integrations
