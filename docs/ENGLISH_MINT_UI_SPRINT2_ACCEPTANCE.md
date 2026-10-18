# English Mint UI Sprint 2 — implementation and acceptance

Stacked baseline: PR #17 (`feature/english-mint-ui-sprint-1-foundation`), which itself depends on still-unmerged PR #16. Sprint 10 and PRs #14–15 remain separate; do not blindly merge.

## Changed experience
- New four-step first-run Compose screen: welcome, age band, English goal, ready. Back and skip work; selections persist using existing learning/profile preference keys and additive `mint_onboarding_complete`. Permission is deferred until microphone use.
- Age band remains a distinct safety preference; under-18 messaging reminds the guardian of private content. This is NOT verified guardian consent functionality.
- Optional placement launches the **existing** `MintPlacementBank` questions; skipping has no assessment result.
- Home has one bright main CTA starting existing Mint Quests, Continue Learning using first incomplete lesson, Games/Clips/Speak/Progress shortcuts, honest lesson progress, and expandable first-use guidance.
- No new AI, game evaluation, XP award, social, billing, remote storage or account migration.

## Routes / behavior
| Screen | Target | Evidence |
| --- | --- | --- |
| Fresh install | 4-step welcome | `chosenPath.isBlank()` |
| Ready / Start | Today | existing preference keys written |
| Optional level check | existing placement | never forces microphone |
| Daily adventure | Games / Mint Quests | real catalog |
| Continue Learning | Learn or Review | based on completedIds |
| Games, Clips, Speak | Games, Feed, Speak | real current main routes |
| Progress / learning path | Review / Learn | existing pages |
| Help | Inline expandable guidance | no new inaccessible destination |

## QA and known gaps
- [ ] New-build Android unit tests, lint and debug APK; monorepo CI and release gates
- [ ] Cold start, interruption/restart, return user, optional placement / skip, no microphone
- [ ] Check stored age/goal and XP/lesson history survive upgrade signed by the same key
- [ ] Dark/light/system/night, 320dp, tablet, 200% font, TalkBack and navigation bars
- [ ] Before/after screenshots compared with owner's board; owner device sign-off
- [ ] Test Home and Profile help reachability (Home implemented; dedicated Profile quick help still outstanding)
- [ ] Full daily mission completion screen, tablet-specific layout, illustrated splash/mascot, screenshot tests and comprehensive loading/empty/error state coverage remain outstanding; do not claim A–K fully complete

No merge or production deployment approved.
