# English Mint Grammar Arena Sprint 36 — Challenges & Achievements Pilot

## Delivered
- Games > Grammar Arena: Grammar Garden Challenge based on six distinct, previously verified mastered questions from the existing 18-question offline bank.
- Progress indicator and one-time manual Quest Emblem collection using local `mint_grammar_challenge_awards`. The saved emblem is included in the existing Mint Garden quest count.
- No timed pressure, user competition, leaderboard, subscription, online services or fabricated mastery.
- Eligibility checks filter old mastery indices to current real questions; tests cover five versus six, stale indices, empty bank and duplicate prevention.

## Important limitations
- This is one original local achievement, not a full challenge calendar, tournament, multiplayer, trophies catalog or adaptive streak system.
- Existing reward and XP histories are not migrated or rewritten. The emblem only becomes claimable after genuine grammar mastery.
- Physical-device QA, accessibility, content scaling and current-commit Android build, CI and release gates remain required.
- Stacked on unmerged Sprint 35 PR #56, which itself depends on an unmerged draft stack. No merge or deploy.

## Acceptance
1. With fewer than six distinct verified grammar questions, button is absent.
2. Complete a sixth unique question; collect once and verify Garden Quest Emblem increases.
3. Restart and revisit challenge; button is absent and award persists.
4. Replay mastered questions: no duplicate award; no second XP or bloom.
5. Run TalkBack, large text, small devices and offline mode tests.
