# English Mint Sprint 19 — Reward Universe Increment and Coverage Audit

Base: unmerged PR #32. All earlier integration branches remain unmerged.

## Implemented in this draft
- `MintRewardUniverse`: immutable collectible taxonomy, deterministic counts derived from preexisting verified unique mastered IDs. No invented voice mastery from speech attempts; no conversion from raw XP totals.
- `MintActivityCompletion`: shared themed reward reveal for five integrated activity modes (Riddles, Vocabulary, Situations, Listening, Reading). Repeats show a previously-completed message. Next activity and My Mint Garden actions remain present; reduced-motion preference controls scale.
- Home: My Mint Garden shows live counts from existing lesson, grammar, riddle, vocabulary, reading, listening, situation, puzzle and quest mastery. Voice count stays zero unless verified voice award data becomes available.
- Unit tests verify legacy ledger mapping and nonrepeat reward behavior.
- Existing `total_xp`, mastery sets and event identifiers are not changed or rewritten. Dashboard and completion cards use the same source state.

## Still missing before the specification's full definition of done
- Shared collectible success overlay **across all modes**: current UI is an inline success card, not an app-wide animated full-screen popup. Grammar uses its older set completion panel. Lessons, skills, word builder, Mint Quests, guided dialogue, Clips, Five-Word Stories, speaking and Review still need eligible-event mapping and shared celebration/navigation.
- No durable new collectible event ledger beyond preexisting verified completion sets. Milestone Stars and Learning Flame require eligible data and validated rules; do not fabricate daily consistency history.
- Home has a summary garden, not rich cards, reward gallery animation or achievements inventory.
- Existing numeric XP may remain in other surfaces. A cross-screen copy audit is required before calling numeric XP replaced.
- Physical devices, TalkBack, high font scale, system/dark/night and migration/upgraded APK acceptance pending.

## Device QA
1. Complete new Vocabulary, Listening, Reading, Riddle and Situation items: appropriate themed reward appears; Home tally increases.
2. Repeat a previously mastered item: no new XP, no extra collectible count, return to garden works.
3. Close and relaunch; counts remain stable; existing progress is preserved.
4. Toggle Reduce animations; the reveal should not perform scaling.
5. Verify 320dp, tablet, dark mode, TalkBack and portrait/landscape.
6. Verify all route entry/return actions and no collisions with existing Next controls.
7. GitHub exact-commit Android tests/lint/debug APK, monorepo CI and release gates; owner approval before any merge.

**No paid AI, billing, backend, migration, deploy or merge.** This is an incremental prototype; do not mark global Reward Universe complete.
