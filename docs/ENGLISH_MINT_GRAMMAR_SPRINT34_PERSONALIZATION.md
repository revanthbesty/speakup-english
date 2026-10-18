# English Mint Grammar Sprint 34 — Offline Practice Personalization

## Increment
Adds transparent, on-device recommendations for the existing 18-question Grammar Arena pilot. Prioritizes previously missed questions, then user bookmarks, then unmastered questions, then optional mastered-question refresh. Stable deterministic ordering and plain-language recommendation reasons; no cloud AI, user profiling, paid APIs or inferred CEFR claims.

## Code and acceptance
- `MintGrammarPersonalization` sorted ranking and reason; pure tests for priorities and empty content.
- Games > Grammar Arena > Recommended practice, with reason and navigation to the existing question flow; transient pack state resets.
- Existing once-only mastery XP/Bloom semantics and local data retained.
- Verify a missed question is recommended first, bookmark second, unmastered third. Complete question and verify rank changes.
- Test accessibility, pack switching, no duplicate Bloom and offline mode on device.

## Still missing
Only 18 existing draft questions. This is not the reviewed 9,500/10,000 grammar bank and is not a machine-learning coach, spaced-repetition scheduling, skill/topic diagnostics or validated personalized learning outcomes. Human content approval and device verification are still required. Stacked on unmerged Sprint 33 PR #54. Do not merge or deploy.
