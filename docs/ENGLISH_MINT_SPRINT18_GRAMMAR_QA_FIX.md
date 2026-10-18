# Sprint 18 Device Finding — Grammar Arena sets and rewards

Reported after 19/40 owner checklist items: Grammar Arena previously had only six questions. After completing those questions, the existing Next control wrapped immediately to question one and showed no dedicated set-completion or earned-XP summary.

## Fix
- Preserve the existing six questions and their ID-based once-only XP records, then append 12 independently authored grammar questions to form three six-question sets.
- Require a checked correct answer before the learner can advance, so the completion screen reflects six answered questions rather than skipped items.
- After the sixth correct answer in each set, show an explicit completion message, **earned XP in this run**, and an actionable **Start next set** button.
- On the last set, clearly offer **Review from Set 1** instead of suggesting nonexistent new content.
- Keep +10 XP **only for each newly mastered question**, using the unchanged `grammar_mastered` ledger and `total_xp` preference. Previously completed questions show +0 earned on repeats; never duplicate XP.
- This is a local question bank and simple progression, not generated AI questions.

## Acceptance
1. Enter Games > Grammar; see Set 1 of 3 and Question 1 of 6.
2. Wrong answer shows explanation; Next is disabled until correct answer is checked.
3. Finish question 6; see Set 1 completion and actual XP earned; use Start next set to reach seven *new* questions.
4. Complete all 18 questions and verify Review from Set 1.
5. Repeat the first set and ensure no additional XP, no broken state and older recorded mastery is preserved on app upgrade.
6. Test leave/re-enter and restart, 320dp, dark/night mode and TalkBack.
7. Exact-head Android tests/lint/debug APK and CI must pass. Device acceptance remains owner-driven.

## Safety
No merge, deployment, changes to app ID, payment service, paid AI or destructive preference migration.
