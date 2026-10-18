# English Mint — UI Sprint 3 audit and acceptance

## Integration chain
This branch is stacked on the still-unmerged Sprint 2 PR #18, then UI Sprint 1 PR #17, then UX rescue PR #16, then Sprint 10 PR #13. Do not merge independently or change production/default branch without owner review.

## Verified content available in current Android source
- **Games:** Mint Quests, Word Builder, Riddles, Vocabulary, Grammar, Situations, Listening, Reading, Dialogues all have existing game UI handlers. Quest selection and rewards already use MintDailyQuest/MintGameEngine; these engines are unchanged.
- **Grammar:** Six locally authored, three-choice questions with answer and explanation strings. A/B/C choices and constructive explanation are now UI-formatted. No 10,000-item dataset is claimed or generated.
- **Stories:** MintFeedScreen supplies existing age-filtered studio-authored offline captioned scenes, story choice challenge, private reactions, local report safety, optional speaking link. Stories are a single Feed/Clips destination, reachable from Games; no duplicate bottom tab or fabricated video player.
- **XP:** Existing award ledger and preference keys remain in charge. UI-only files changed, no new XP logic.
- **Settings:** Day/night/system theme code and reduce-motion story settings are retained.

## Before -> after routes
| Before | After | Target |
| --- | --- | --- |
| Games default quest + overwhelming optional filters | Games Hub: featured quest + two concise Grammar/Stories entries | Existing game engine |
| Grammar buried among filter chips | Games > Grammar: Grammar Arena header, six actual questions, A/B/C choices, clear explanation | Existing grammar questions/award keys |
| Feed "Mint Reels" and long introductory sentence | Clips > Animated Stories, shorter labels and quiz guidance | Existing offline captions and story challenge |
| Games to stories | Games > Stories -> Feed/Clips | Existing story screen |
| Home daily adventure | Unchanged from Sprint 2 | Mint Quests |

## Outstanding scope — not release accepted
1. A full category detail screen, dedicated result/rewards display, 13 individual pixel-matched screens, new mascot illustrations and animated 2D asset system are **not yet implemented**.
2. Grammar category lock/coming-soon library and question-bank search are not built; only six verified questions are represented.
3. Story video streaming/audio is not promised; current offline scene player and existing TTS remain.
4. Instrumented screen-route/back-gesture tests, contrast screenshots at 200% font, compact phones/tablets, dark/light/system and offline/empty-state acceptance remain to be tested.
5. Need independent verification of physical APK, scoreboard correctness, route regressions and owner sign-off.
6. No production merge and no paid AI/monetization/backend changes.

## Manual test matrix
- [ ] Home > Games > Mint Quest > Answer > Check > Next > Speak
- [ ] Games > Grammar > A/B/C selection > Check > Why > Next > Back
- [ ] Games > Stories > captions toggle > play scenes > quiz > optional speaking
- [ ] Reenter completed quest and grammar, confirm no duplicate XP
- [ ] Switch dark/light/system/night, screen-reader order, 200% font, 320dp and tablet
- [ ] Validate matched-signed upgrade preserves preferences, XP, profile, award ledger and reports
- [ ] Obtain annotated before/after device screenshots and owner approval
