# English Mint Sprint 27 — Offline, Accessibility and Polish Increment

## Source
Recovered 19–28 SRS Sprint 27: dark/night themes, motion, offline experience and performance. Exit gate: accessibility and low-end Android device regression.

## Existing implementations confirmed
- System/light/dark/night theme preferences and eye-comfort switch already exist in Profile.
- Reduced-motion support already exists for major activity celebrations and navigation.
- Original offline learning text is available in quests, cooperative stories, career practice and the opt-in on-device Coach.
- Device speech recognition may depend on installed provider and connectivity.

## Added in this increment
- Profile > Settings: clear Offline & Low-Data guide, differentiating truly offline text content from voice provider and non-existent multi-account sync.
- Fallback guidance for typed alternatives, reduced-motion and operating-system text size.
- Pure policy tests ensure supported features and provider-dependent capabilities are labelled without misleading promises.
- No new external requests, app permissions, storage migration, subscriptions or reward-awarding paths.

## Still required before Sprint 27 can be called complete
- Actual low-end device performance measurements, startup / scroll / frame timing and memory profiling.
- UI regression across system/light/dark/night, large text, TalkBack, rotated/small devices and reduced-motion.
- Offline airplane-mode test matrix and error/empty-state verification across all screens.
- Verified downloadable media packs, robust offline synchronization/retry and long-term cache management are not implemented here.
- User testing, regression and production hardening on an Android test APK.
- Prior Sprints 22/23/25/26 full original acceptance requirements remain outstanding.

## QA
1. Profile > Edit profile and settings > Offline & low-data guide: verify feature labels and fallback instructions.
2. Toggle system/light/dark/night, reduced motion, eye comfort; verify settings persist and do not hide content.
3. Run quests/co-op/coach/career without network (text options only); speech failure should allow quiet text.
4. Ensure historical lessons, Mint rewards and learner preferences are preserved.
5. CI, Android build, lint and same-commit APK before owner device acceptance.

Draft branch stacked on PR #46, not directly merged to main. No deployment.
