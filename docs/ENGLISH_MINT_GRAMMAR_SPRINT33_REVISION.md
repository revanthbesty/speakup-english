# English Mint Grammar Sprint 33 — Progress & Revision Pilot

## Scope
Incremental Grammar Arena review features on the existing **18 authored offline pilot questions**, stacked on Sprint 32 PR #53. This is not the imported or human-approved 9,500/10,000-question bank.

## Implemented
- Grammar Hub shows mastered questions and percentage using valid legacy `grammar_mastered` IDs only.
- Revision queue combines local bookmarks, missed-answer markers and unmastered questions, with a shortcut to the first candidate.
- Missed-answer markers persist under `mint_grammar_revision_missed` and are removed after a correct answer; repeat mastery cannot award a new Bloom or XP.
- Explanation and six-question pack result dialogs preserved.
- Pure Kotlin tests cover review deduplication, stale keys and correct progress percentages.

## Known limits
- The shortcut opens the first review item within existing sequential pack navigation; there is no dedicated spaced-repetition player or learning scheduler.
- Current attempt-specific first-try and pack-scoring statistics remain scoped to the current pack, not a longitudinal analytics service.
- Existing IDs are index-based. A reviewed new corpus import needs stable content IDs and a lossless legacy mapping before replacement.
- Saved review markers are local to this device; no account sync, cloud analytics or individual proficiency claims.
- Human content review, physical device QA, accessibility acceptance and integrated release gates remain outstanding.

## Acceptance
1. Miss a question, reopen Grammar; revision count includes the missed item even after restart.
2. Correctly answer the missed question; missed marker is removed, but a deliberately bookmarked or unmastered question may remain in revision.
3. Repeat mastered questions; no duplicate Bloom or XP.
4. Restore 18-question pilot progress, verify percent excludes unrelated stale legacy IDs.
5. Switch packs, use bookmarks and explanations, and verify no navigation dead ends.
6. Android build, unit tests, release gates and same-SHA APK must pass before device acceptance.

No merge or deployment.
