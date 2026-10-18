# English Mint Grammar Arena — Sprint 29 Baseline, ADR and Release Gate

**Reference inputs**: uploaded Grammar Arena SRS v1.0 (18 Oct 2026), Codex Master Prompt, UI/UX Design Board, and supplied CSV of 25 example questions. Stacked base: unmerged Reward Universe PR #33, itself based on PR #32 and earlier development PRs. **No code merge or deployment.**

## Verified starting point
- `MainActivity.kt` includes an existing six-question Grammar Arena expanded to **18 locally hardcoded grammar questions** by draft PR #31. It uses a 6-question set loop, correct-answer explanation, and a legacy `grammar_mastered` once-only award ledger plus `total_xp` preservation. It does not implement a 10,000-question content service.
- `MintGrammarUiContract.kt` formats three-option question labels. Existing five primary app tabs must remain Home, Games, Clips, Speak and Profile.
- PR #33 provides a first, incomplete Reward Universe collection adapter and Home garden; collectible screens across all activities are not finished.
- Uploaded **CSV has 25 examples**, with `status=sample_not_approved`. Approved content count attributable to this upload = **0**, published content count = **0**. These examples are not evidence of editorial acceptance or publication.
- The design board illustrates a topic hub, tier selection, mixed quiz formats, results, explanations and progress panels. It is a **concept design**, not functional UI or rights-cleared media.

## Architecture decision
**ADR-GRAMMAR-001: Extend the existing Compose app; do not create a separate app or replace legacy question IDs.**
- Add a strict, independently testable, versioned question content contract and publishing gate first.
- Keep unapproved imports quarantined and separate from the live engine; no fabricated question-count banners.
- Exactly three nonblank, case-insensitively distinct options keyed A/B/C, one valid correct key, substantive explanation, locale, age segment, topic/subtopic, outcome, provenance, approval and reviewer.
- Visual formats require alt text; audio requires transcripts. Real images/audio require license review and genuine assets.
- Only approved, reviewed, rights-cleared data can be eligible for future scored sessions; gate currently defined in a pure policy, **not yet connected to an import pipeline or admin service**.
- Reward Universe will award meaningful Grammar Blooms only from real mastered question events and never from simply entering a screen. Preserve `grammar_mastered` and XP records for upgrades.

## Target content manifest
25 domains × 400 unique independently vetted questions = **10,000 target**. Each domain: Easy 120, Medium 140, Hard 140; global Easy 3,000 / Medium 3,500 / Hard 3,500.
Current imported/published counts in this PR: **0 imported, 0 approved, 0 published**. Current separately hardcoded legacy Grammar Arena: **18**. Sample CSV: **25 unapproved**, not yet an app import. Do not present 10,000 as live.

## Gap matrix
| Capability | Current evidence | Next action |
| --- | --- | --- |
| Existing grammar lessons and once-only progress | Hardcoded six-question sets and `grammar_mastered` | Preserve IDs and test upgrade |
| 25-domain/3-tier content manifest | SRS plan, new contract | Approve domain taxonomy and metadata |
| 10,000 vetted questions | Not built | Author/import and independent review in batches |
| Exactly A/B/C and media accessibility | New pure validator/tests | CSV ingestion, content QA, UI/player wiring |
| Topic Hub / level selector | Illustrated on board only | Feature-gated Compose screens |
| Image, dialogue, audio, story templates | Concept SRS | Licensed assets, alt/caption/TTS fallback |
| Weak-topic recommendations and bookmarks | Proposed | Persist per-question history and review queue |
| Animated Mint Bloom and garden | PR #33 partial | Wire Grammar completion, safe migration |
| Editorial CMS and secure publishing | PR #27 policy prototype only | Authenticated moderation/review backend |
| Accessibility and real-device QA | Automated CI incomplete evidence | 320dp/tablet/TalkBack/dark/reduced-motion test |
| 100 approved pilot | None | Human review then staged release |
| Paid AI / cloud services | Not authorized | Keep disabled |

## Sprint gates following SRS
Sprint 29 audit, schema, flags and risk register (this PR); Sprint 30 pilot quiz engine and approved 100 (requires editorial reviewers); Sprint 31 hub and progress; Sprint 32 multimodal templates; Sprint 33 CMS/validation workflow; Sprint 34 adaptive; Sprint 35 rewards; Sprint 36 offline/night polish; Sprint 37 content scaling; Sprint 38 full regression. These are **planned stages**, not shipped features.

## Risks and rollback
Duplicate or ambiguous questions; superficial paraphrase inflation; unequal difficulty distribution; sensitive voice/child data; visual media licensing; lost `grammar_mastered` mappings; fragile large unmerged PR stack. Remediation: fail-closed publishing, independent review, staged opt-in feature flag, stable IDs, separately stored revisions, test debug builds, no irreversible migration. Rollback by leaving new catalog disconnected until accepted.

## Sprint 29 acceptance
- [ ] New pure validator passes unit and repo CI.
- [ ] Validate a real imported CSV, duplicate stems/distractors and independent reviewer IDs **before** approving a pilot.
- [ ] Actual Android test, lint, debug APK green at same SHA.
- [ ] Real app/device interaction and visual review still outstanding.
- [ ] Owner confirms topic taxonomy and authoring budget (zero paid AI tokens for now).

**No merge, deployment, app ID change, new billing, paid AI, XP destruction or claim of approved 10,000 questions.**
