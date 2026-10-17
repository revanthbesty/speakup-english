# Sprint 1 — Android acceptance checklist

Scope: learner onboarding, placement, personalized Today dashboard, Material navigation, and accessibility.

## Implemented
- Age choice (Kids, Teen, Adult) and five pathway choices saved in SharedPreferences.
- Daily practice goal (5, 10, 15 minutes) saved with learner profile.
- Three-question non-certified starting assessment, persisted score, skip and retake.
- Personalized recommendation card linking to lessons or speaking practice.
- Today's next incomplete lesson, completed lesson count, XP and progress.
- Material NavigationBar across Today, Learn, Games, Speak, Review.
- Semantic labels for navigation icons and completed-lesson progress.
- Existing lessons, speaking recognition, games and revision preserved in source.

## Automated acceptance
- Android CI must pass for final Sprint 1 commit; attach artifact and run ID.
- Check app builds without Kotlin compilation errors.
- Verify no regressions to existing local preference keys.
- Confirm score=-1 means skipped, not a beginner classification.

## Device acceptance deferred until Sprint 10 (per product owner)
- Fresh install: select each age/path, choose a daily goal, finish or skip assessment.
- Force close/reopen: preferences, XP, lessons and assessment state persist.
- Retake assessment, check recommendation and navigate to next activity.
- Check TalkBack labels and large-font layout at 200% font scaling.
- Verify narrow-screen bottom navigation labels and touch targets.
- Exercise speech recognition, games, revision and lesson completion.
- Check dark system mode contrast; app currently uses explicit light surfaces.

## Deferred items / limitations
- A dedicated dark theme is not shipped; light UI remains intentionally consistent.
- Formal automated Compose UI tests and device accessibility audit not yet in place.
- Placement is a brief routing aid, not a proficiency certification.
- The current lesson library is a prototype; full curriculum belongs to Sprint 2.
