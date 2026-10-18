# English Mint Sprint 49 — zero-paid AI baseline audit

Repository baseline: branch `sprint45-professional-personalization` is still in the stacked draft history. This audit must not be mistaken for release evidence.

Observed:
- Android Compose application already has rule-based learning practice and on-device TTS sample formats.
- AndroidManifest declares RECORD_AUDIO and a TTS service visibility query. It does **not** declare INTERNET in the inspected manifest. Review merged manifests and library transitive manifests before making a zero-egress claim.
- Existing locally authored Grammar Arena has 18 pilot questions and must remain authoritative for correctness and Bloom rewards.
- No local-LLM runtime, validated model license, checksum-controlled model acquisition, device benchmarks, offline-ASR accuracy matrix or secure model deletion is established by this sprint.
- Paid APIs and tokens are prohibited. No cloud AI fallback may exist.
- Low-end device, accessibility, thermal and offline traffic measurement are mandatory future gates.

Go/no-go: **NO-GO** for shipping optional generative inference. Run actual device benchmarks and inspect all runtime network activity before approval.

Roadmap per approved 49–56 SRS: 50 router, 51 speech privacy, 52 optional local model, 53 Buddy, 54 grounded adapters, 55 polish, 56 strict regression/release gates.
