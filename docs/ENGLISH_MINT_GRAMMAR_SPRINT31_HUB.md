# Grammar Arena Sprint 31 — Pilot Hub, Bookmarks, Explanations and Existing Results

## Roadmap and status
Original Grammar Arena SRS Sprint 31 calls for Topic Hub, Results, Bookmarks and Explanations. This change delivers the **pilot-safe** hub for the actual 18 authored questions, rather than fabricating topic assignments, levels or imported items.

## Implemented
- Grammar Hub summary of three practice packs and bookmarked question count.
- Save or remove current question; stable versioned bookmark keys and persistence in `mint_grammar_bookmarks`.
- Show saved questions and jump to an exact existing item. The jump resets transient scoring/mistake state without changing mastery or historical XP.
- Optional explanation visibility on each question. Existing result dialog from Grammar Sprint 30 remains: first-try result, corrections and Bloom indication.
- Unit tests for stable IDs, valid ranges, counts and zero-question case.
- Existing 18 authored pilot prompts, answer validation, once-only mastery and reward ledger preserved.

## Unmet / blocked
- True topic taxonomy, Easy/Medium/Hard filtering and recommended paths require independently classified and reviewed records rather than guessing topic labels.
- No 100-approved-item pilot or 10,000-item corpus imported; saved questions apply only to current 18 authored local items.
- Cross-device bookmark sync, search and rich analytics are not available.
- Need Android exact-head CI/lint/debug APK and owner accessibility/device QA before acceptance.
- Current branch is stacked on **Sprint 28 PR #51**, itself part of an unmerged PR stack. No merge/deploy.

## Device checks
1. Games > Grammar > Pack 1; save a question. Return to saved-only list; verify it can be opened.
2. Restart application; bookmark persists. Remove and verify it disappears.
3. Read/hide grammar explanation, answer wrong/right, verify existing results.
4. Switch packs and jump from saved questions; no invalid index or duplicate Bloom.
5. Check empty saved state, screen reader, small device and text scaling.
