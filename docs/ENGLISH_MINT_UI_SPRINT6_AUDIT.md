# English Mint UI Sprint 6 — integration and acceptance audit

Source branch: `feature/english-mint-ui-sprint-5-professional` (draft PR #21), stacked on Sprint 4 PR #20, Sprint 3 PR #19, Sprint 2 PR #18, Sprint 1 PR #17 and UX rescue PR #16. **No PR in the stack has been merged by this sprint.** All changes remain review-only.

## Existing screen inventory and reuse
| Feature | Current source | Sprint 6 action |
| --- | --- | --- |
| Profile and editable learner preferences | MainActivity Profile, legacy SharedPreferences | Add short progress and Help entry, retain editable form under existing controls |
| XP, skill breakdown, badges | MainActivity Review, MintRewards | Existing evidence-based page linked directly from Profile; no new reward IDs or balances |
| Certificate eligibility | Existing MintRewards certificate status | Preserve locked / assessment-interest status; no fake PDF/download links |
| Plans | Existing MintMembershipScreen | Preserve informational status; no purchase/restore actions added |
| Privacy & export | Existing MintPrivacyScreen | Preserve controls and data |
| Theme, motion, eye comfort | Profile settings | Preserve stored Light/Dark/System/Night choices and existing toggles |
| Help | No dedicated reachable Help route | New offline Help screen with Getting Started, Games, Speaking, Stories, Privacy topics and working navigation |
| Notifications / downloadable offline media / favorites | No verified end-to-end feature services in inspected Android route | Do not expose fabricated notifications, files or bookmarks |
| Profile photo | No verified avatar upload or image persistence service | Not added |
| Public leaderboard | No verified online privacy service | Not added |

## Route matrix
| Start | Entry | Target | Back |
| --- | --- | --- | --- |
| Profile | View progress & achievements | Review | Home via existing Back |
| Profile | Help & guidance | Help | Profile via Android Back / Up |
| Help > Games | Open Games | Games | Home |
| Help > Speak | Open Speak | Speak | Home |
| Help > Clips | Open Clips | Feed | Home |
| Profile > Edit profile and settings | Inline settings/edit | Profile | Close settings |
| Profile > Privacy | Existing Privacy route | Privacy | Profile |
| Profile > Membership | Existing Membership route | Membership | Profile |

Five persistent tabs retained: Home, Games, Clips, Speak, Profile. No extra tab. Previous Sprint 1–5 contextual destinations retained.

## Changed files
- MintHelpScreen.kt: local/help documentation UI, functional linked navigation
- MainActivity.kt: accessible summary and Help entry, Help parent Back handling
- MintUiNavigation.kt and MintUiNavigationTest.kt: map Help to Profile as parent tab, regression test

## Preserve / risk and verification gates
- Existing Android package/application ID, preference key schemes, XP/mastery/lesson data and storage calls unchanged by these commits.
- No paid AI, payments, certificate printing, network backend, fabricated notifications or production deployment.
- [ ] GitHub Actions Android unit tests, lint, debug APK and monorepo CI on *exact final commit*
- [ ] Real device Profile > Help > topic > Back and Profile > Review
- [ ] Profile > Edit > Save > reopen; no progress loss
- [ ] Dark/Light/System and large text, TalkBack, 320dp/large/tablet, landscape, keyboard inset
- [ ] Before-and-after on-device screenshots and explicit owner acceptance

**Incomplete Sprint 6 scope:** dedicated achievement/certificate details, settings architecture, persisted notification preferences, favorites and downloads screens with verified data, screenshots/instrumented UI tests, responsive tablet layout and exhaustive 1–6 route matrix have not been completed. This is a targeted UI consolidation, not an accepted final release.
