# English Mint — Sprint 2 implementation / acceptance
Base: `feature/english-mint-phase-1` (verified Sprint 1). Branch: `feature/english-mint-sprint-2`.

## Implemented in Sprint 2
- Five SRS journey titles are shown using the existing persistent learning-path identifiers (`zero`, `basic`, `speaking`, `advanced`, `exams`) so existing user progress and recommendations remain mapped.
- SRS age-band selection (4–7, 8–12, 13–17, 18–49, 50+) is stored in `mint_age_band`; the legacy `learner_age` category remains populated for older flows. No public child social interactions are enabled.
- Speaking-first mint-green home adventure with direct Speak and Play actions; stats, lessons, and prior XP/progress remain intact.
- Bottom navigation now includes direct Speaking Studio, Games, Progress and Profile actions.
- System, Light, Dark and scheduled Night (20:00–06:00 local device time) appearance settings. Existing `mint_theme_mode` and legacy `dark_appearance` values retain fallback behavior.
- Eye-comfort visual mode reduces hero saturation/brightness, and manual Reduce animations honors device global animator-duration setting. Progress tiles and tab color animations respect reduced-motion preference.
- Accessible, horizontally scrollable age selection; optional nickname and placement/assessment navigation retained.

## Compatibility
Package/application ID remains `com.speakup.app`, preferences remain `speakup_learning`. Nothing deletes XP, completed lessons, puzzle awards, revision counts, speaking attempts or stored assessment answers. No new cloud services, purchases, secrets, social messaging or AI spend.

## Verification
- `MintExperiencePolicyTest` covers all age-band mappings, under-18 age safety, five journeys, night-mode schedule, invalid theme fallback, and reduced-motion conditions.
- Android PR workflow: `testDebugUnitTest`, `lintDebug`, `assembleDebug`, artifact upload.
- Monorepo CI: lint, typecheck, test, build.
- Device-level checks postponed until Sprint 10 per product owner: TalkBack, narrow screens, font 200%, color contrast in all modes, animations off, device time-zone changes, legacy update over installed APK, and screen/voice permissions.

## Not represented as finished
- Production animated mascot splash and brand-owned/adaptive icon require licensed original artwork, asset generation and device acceptance.
- Interactive short-video Feed and moderation are in Sprint 6, not a production Feed yet. Current navigation retains Review/Progress instead of a fake Feed.
- Full game-engine scale is Sprint 3; production conversational AI is Sprint 4.
- Scheduled night appearance evaluates device time when UI composes/changes state; reliable exact schedule rollover while foregrounded will need an event/tick mechanism and tests before release.
- Child/guardian consent verification, legal review and moderation are required before any public/social child feature is enabled.

## Exit gate
Android and monorepo CI green on final commit; document deviations above; PR review-ready without merging, deploying or asking the user to install until Sprint 10.
