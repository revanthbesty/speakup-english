# English Mint Sprint 15 — Editorial Governance Prototype

Source: draft Sprint 14 PR #26 (unmerged); all predecessor UI/expansion PRs are stacked and unmerged.

## Scope and evidence
Existing MintFeedCatalog ships 12 studio-authored **offline illustrated** Clips; MintFiveWordCatalog ships two locally defined five-word packs. Neither is a live streaming inventory nor a remotely managed content catalog. Local reports do not reach a moderator. The Post-Sprint 10 SRS proposes a rights-cleared editorial CMS and an aspirational 20–30 clips/day; it is NOT live.

## New code
- MintEditorialPolicy.kt: testable local editorial item and rights metadata, version, age floor, reviewer and publication-date requirements.
- Explicit state graph Draft → Reviewed → Approved → Scheduled → Published → Archived, with rollback to Draft before publication.
- Safety gate that rejects content without provenance, reviewer, schedule, valid metadata or conflicting duplicate ID/body.
- MintEditorialPolicyTest.kt: rights and reviewer omissions, state bypasses, future dates, duplicates and legitimate transition tests.

## Publication architecture proposal — NOT IMPLEMENTED
Dedicated staff-only CMS with authenticated roles and reviewer audit trail, backend schema for content/asset provenance, image/video license verification, automated moderation with human decision, release scheduling in configured locale/timezone, versioned content API, signed media URLs, staged rollback, safe caching, and an incident queue. Do not expose these capabilities as operational.

## Operational gates
1. Register owner, license, attribution, expiry, asset hash and supported distribution territories for each asset.
2. Separate content author from human approver and store review reasoning/timestamps.
3. Validate age policy, spelling/readability, rights, duplicate fingerprints, caption/narration sync and security/privacy.
4. Publish only human-approved, rights-cleared items; support revocation/rollback and cache invalidation.
5. Pilot modest editorial volume; budget video licensing, art and CDN only with explicit user approval.
6. Keep all public comments, stranger messaging and external moderation report submission disabled until robust safeguards exist.

## Acceptance and unfinished scope
- [ ] CI release gates, Android unit tests/lint/debug APK and exact SHA artifacts.
- [ ] Real CMS authentication and admin UI, production publish API, secure storage, durable moderation queue, robust hashes, rights expiries, schedule timezones and revocation mechanism.
- [ ] Actual content editorial/legal approval of every current/future asset.
- [ ] Integrated user-facing content loading, device QA, release review and owner acceptance.
- [ ] No merge, deployment, paid AI, or media delivery service without separate approval.

This increment is architecture and unit-tested editorial policy only. It does not publish videos or claim the full Sprint 15 specification is complete.
