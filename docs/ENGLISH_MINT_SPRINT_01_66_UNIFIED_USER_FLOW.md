# English Mint — Unified user journey across development Sprints 1–66

Status: canonical flow contract for UI implementation. **Design/QA specification, not evidence of final visual approval.**
Hierarchy: original approved UI Sprint 1–6 boards > recovered development mockups > functional SRS for any missing screen. All learner routes must remain in ONE app.

## Five persistent top-level destinations
Home (`Today`) · Games (`Games`) · Clips (`Feed`) · Speak (`Speak`) · Profile (`Profile`). No secondary sprint feature becomes a sixth tab.

## Recommended beginner journey
Launch -> existing profile? Home : four-step Onboarding -> optional level placement -> Home -> Today's Adventure -> Games / Quest -> question -> check -> explanation -> next/result -> Speak optional -> truthful Progress/Review -> Home.

## Destination and return contracts
| Entry point | Child destination | User task | Parent tab | Back/up |
|---|---|---|---|---|
| Home | Learn | Continue next lesson | Home | Home |
| Home | Review | Skills, history, rewards | Home | Home |
| Home | Professional | Industry selector / professional practice | Home | Professional nested Back -> hub -> Home |
| Home > More | AILifePilot | Offline missions, City, Story, Words, Buddy | Home | AI Life child -> AI Life hub -> Home |
| Home > More | FamilyPilot | Private family/teacher preview | Home | Home |
| Home > More | CreatorLab | Local creator preview | Home | Home |
| Games | Mint Quests / grammar / all supported modes | Choose -> answer -> check -> explanation -> next/result | Games | Activity -> all games; Games tab -> Home |
| Games > Stories | Feed | Authored illustrated Clips / story | Clips | Return to Games entry context |
| Clips | FiveWord | Five-word story scenes and challenge | Clips | Clips |
| Speak | Guided conversation / lesson | Respond with optional mic/text | Speak | Speak or Home as appropriate |
| Profile | Help / Privacy / Membership | Settings, data, help, plan information | Profile | Profile |

## Must-hold UI behaviors
1. Visible action to start each actually available feature; unknown or unsupported services use truthful locked/unavailable state, never dead buttons.
2. Primary CTA above distracting secondary actions; one question/learning task at a time; explanations shown after answer checks.
3. Persisted progress and rewards may only reflect verified learning activity; no duplicate XP and no fabricated AI/certificate/streaming claims.
4. Back gestures, tab switching and deep navigation preserve intended parent; do not discard entered answers without a tested path.
5. Age/privacy, TTS/mic permissions, offline fallbacks and empty/error states have routes and accessibility labels.
6. Use original images where licensed/available. Newly designed UI only for real gaps in approved boards, clearly recorded in traceability matrix.
7. Screens and user journeys need light/dark, 320dp/360dp/412dp, 200% font, TalkBack, Android API 29 and 35 device acceptance.

## Verification
Automated: `MintUiNavigationTest` verifies five tabs and secondary parent mapping.
Outstanding: instrumented full-journey tests, Android Back/rotation restoration across flows, actual screenshots, source-to-66-sprint route coverage, owner review. Do not mark complete until proved.

No merge/deployment without explicit owner permission.
