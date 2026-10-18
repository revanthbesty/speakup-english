# Grammar Arena Screenshot Acceptance Fix — October 18, 2026

User demonstrated on-device Set 1 of 3, Q6/6: still shows "XP once per new correct answer"; last-question score unclear; completion disabled after incorrect answer; no animated set-result popup. This is a concrete device QA finding, not evidence that all Reward Universe activity paths are complete.

## Fix on this branch
- Keep six questions per set and the existing one-time `grammar_mastered` / `total_xp` preferences unchanged.
- Remove the numerical XP-oriented Grammar Arena practice label, replacing it with Grammar Bloom mastery copy.
- Count each question correct on **first checked try** or requiring another try. Retried answers are not counted repeatedly.
- Final set result uses a scrollable popup with "Set completed", a reduced-motion-aware Bloom reveal, first-try correct/mistake totals, and individual explanations of concepts initially answered incorrectly.
- Learner can start the next available set (or review Set 1 when no further set exists), or view the saved Mint Garden on Home.
- The next/complete action remains disabled until a correct checked response. A missed answer is corrected by picking the right choice and checking again. This is intentional, and the final result reports first-try misses rather than pretending all six were initially correct.
- Reward originality is grounded in the same preserved once-only XP/mastery ledger; a replay cannot award a new Bloom.

## Validation required
1. Finish six questions, getting some wrong first: see accurate first-try correct and mistakes totals that sum to six, then per-question grammar explanations.
2. Finish another set, verify Bloom celebration animation and dashboard mastery increase only for newly mastered IDs.
3. Repeated checking/replaying should not inflate counts or rewards.
4. Use Next Set and Garden on 320dp, large-font and dark-mode screens. Toggle Reduce animations and confirm a static reveal.
5. Automated Android tests and debug APK must pass for exact new commit; physical device signoff remains outstanding.

## Out of scope
This is Grammar-specific owner feedback. Other game modes and the full 10,000 question bank remain separate unfinished acceptance gates. No merge/deploy/paid services or destructive migration.
