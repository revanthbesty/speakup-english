# English Mint — 4,000 activity content sprint: foundation and release gates

## Scope and current truth
Source: owner-supplied 4,000 Learning Activities Master Content Sprint (18 Oct 2026).
This branch is an **initial, incomplete foundation**, not a claim that 4,000 authored activities exist.
All new content must be free and offline-first. No paid AI, merge, release, deployment or user-data reset.

## Existing stack and base selection
Created from `feature/english-mint-grammar-arena-sprint29-audit-schema` rather than stale `main`.
Draft PRs #31–34 contain stacked progress, completion and rewards work; preserve their data contracts.
PR #33 reports only five integrated collectible completion modes (Riddles, Vocabulary, Situations, Listening, Reading).
Do not claim global Reward Universe or 10,000 Grammar Arena questions are shipped.

## Content contract
Exactly 500 independent playable items in each:
`word_builder`, `riddles`, `vocabulary`, `situations`, `listening`,
`reading`, `dialogues`, `stories`.
Per-category CEFR: A1=100, A2=125, B1=125, B2=100, C1=50.

Minimum JSON keys: `id`, `category`, `cefr`, `title`, `objective`,
`content`, `prompt`, `correct_answers` (string array),
`explanation`, `estimated_seconds`, `content_version`.
Optional `options` string array must include the accepted answers.
Listening requires `transcript`; reading requires `passage`;
dialogues require `turns`; stories require `story`.
Adapter to Android's actual typed models and persistence is not yet implemented.
The validator's JSON format is a portable interchange contract, **not** a new runtime database.

## Work still required before this sprint may be called complete
1. Audit the actual Android activity model, routes, completion ledger and test fixtures on this branch.
2. Create 500 **editorially distinct** content items per category. Keep provenance and reviewer approval.
3. Have a competent human review representative samples of every category and CEFR level; automated validation cannot establish semantic uniqueness, correct CEFR assignment, pedagogy or natural English.
4. Extend typed category-specific models only where required; integrate with the existing eight screens and local asset-loading architecture.
5. Preserve stable previous IDs and stored user progress; add migration tests before changing key formats.
6. Test TTS capability/replay/accessible transcripts on real devices; a transcript alone is not a playable listening activity.
7. Connect completion to existing Mint Garden/rewards only for verified first-time completions; no invented streaks, new XP farming, or mandatory Reward Universe dependency.
8. Test all start/continue/next/home flows, route restoration, offline mode, Android CI and actual APK.
9. After verification, report exact counts and tested commits; only then permit a release-readiness review.

## Validator
Run `python3 scripts/validate_english_mint_activities.py content.json`.
The default command **fails closed** until all 4,000 items and 40 category/CEFR allocations pass.
`--allow-partial` supports draft fixture development only and is not publish approval.

## Current counts and validation evidence
- Authored production content added in this branch: **0 / 4,000**.
- Integrated and Android-verified new activities: **0**.
- Structural validator added; CI integration and actual pack validation not yet completed.
- Human content approval: **pending**.
- Device playback and route testing: **pending**.

**Do not merge or deploy this draft.**
