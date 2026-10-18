# English Mint — consolidated UX rescue / device acceptance

Baseline: Sprint 10 (PR #13). This branch builds on PR #15, NOT a merge of competing PR #14 and #15. PR #14's overlapping guidance/theme/inset intentions were reviewed; its alternative MainActivity implementation was not cherry-picked. PR #15 retained Profile disclosure, Games disclosure, speaking choice disclosure and Feed contrast surface.

## Included in this consolidated branch
- Home presents one primary action: **Start my 5-minute English adventure**, entering the Mint Quest game before an optional spoken conversation step.
- Games offers **Step 2: Practise speaking** without hiding any preexisting mechanic; all modes remain available via **Change game or difficulty**.
- Speaking conversation introduces **Step 2**, with level/topic controls optional. Audio and microphone disclosures remain unchanged.
- Feed has concise, plain-language guidance. All existing captions, private interactions and local reports remain unchanged.
- Light and dark Material3 color roles include primary/onPrimary, background/onBackground, surface/onSurface, surfaceVariant/onSurfaceVariant, primary containers, and outline.
- Scrolling content uses statusBarsPadding; Material3 NavigationBar keeps its built-in windowInsets (avoid duplicate padding).

## Explicit limits
The user has **not** accepted this UX on hardware. The first action starts a game and speaking is guided but optional, not an enforced timed workflow. There is no claim of live AI, actual video streaming, operational public moderation, valid certificate, billing or production readiness.

## Regression / QA matrix
- [ ] GitHub Android unit tests, lint, debug APK and monorepo CI complete on **this branch SHA**.
- [ ] Cold start: onboarding/skipped assessment -> Home -> Start adventure -> game question -> Check -> Step 2 conversation -> optional microphone permission OR quiet typing -> progress -> relaunch.
- [ ] 320dp / 360dp / 412dp viewport, status bar and gesture/3-button navigation; buttons visible and not clipped, including at 200% font scale.
- [ ] Light / dark / system / night appearance: read heading, caption, hint, chip, disabled button, speech prompt, feedback, dialogue, Feed and bottom nav.
- [ ] TalkBack focus order, touch targets >=48dp, labels, reduced motion and orientation reflow.
- [ ] Upgrade with SAME signing key from prior SpeakUp installation; validate application ID com.speakup.app and SharedPreferences speakup_learning retention of XP, awards, mastery and all lesson progress. Never uninstall a user's sole copy to force a mismatched-signature debug APK.
- [ ] Browse full 72 authored quest items / 8 mechanics and 12 reels; complete one of 18 guided turns; XP awarded only once for same mastery ID.
- [ ] Capture device screenshots: Home, Games before/after challenge, Speaking with dialog, Feed, Profile, all in dark and light.

## Review and rollout
Keep PR unmerged until CI, screenshot comparison, physical-device feedback and explicit owner sign-off. No paid AI, payment processing, UGC, cloud spend, signing-key change or production deployment.
