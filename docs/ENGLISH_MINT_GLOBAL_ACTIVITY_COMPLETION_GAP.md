# English Mint — Cross-Activity Completion UX Follow-up

Owner feedback (Sprint 18, 19/40 checks): Every learning activity should clearly confirm completion, show **actually earned** rewards reflected on Home, and provide a next-activity or next-level action. This is larger than Grammar Arena alone.

## Implemented first integration increment
- Shared `MintActivityCompletion` component with explicit success, actual XP for the latest newly mastered question (or 0 on replay), next-item CTA and Home dashboard CTA.
- Integrated after successful checked answers in Riddles, Vocabulary, Situations, Listening, and Reading. Existing once-only mastery sets and `total_xp` are the source of truth, and the Home dashboard already reads that same `xp` state.
- Preserves the separate Grammar six-question set completion from PR #31. New question banks or changed XP rates are not introduced by this follow-up.

## Known missing acceptance
- The entire activity taxonomy is **not yet covered**. Word Builder, Mint Quests, Lessons, Skills, Guided Speaking, Five-Word Story quiz, Clips quizzes, dialogue and Review still need the same integrated completion semantics, distinct award eligibility, route-aware next-level choices, and UI tests.
- Existing Next buttons are still shown alongside the new completion CTA in covered modes; UI simplification requires visual/device testing.
- There is no new dashboard per-activity achievements ledger or certificate screen; the Home XP total is updated using existing state. XP may be zero on replays, which is intentional.
- Real Android device, progress persistence, accessibility, navigation and reward anti-farming regression tests are required before the entire requirement can be declared done.
- No merge, deploy, paid AI or app ID/storage migration.

## Owner acceptance
Complete one new activity and confirm “completed”, +10 XP, Next activity and Home show current total; repeat it and confirm zero additional XP. Test each of the five covered modes, return/back and session switching, and report screenshots for crowded controls.
