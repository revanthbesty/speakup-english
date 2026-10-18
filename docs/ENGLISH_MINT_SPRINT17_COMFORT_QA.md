# English Mint Sprint 17 — Comfort and Accessibility Increment

Parent: unmerged Sprint 16 PR #28. Sprint 17 is stacked, draft and not released.

## Changes verified in source
- `MintFeedScreen.kt`: local user-toggleable standard/large caption text, saveable across Compose state restoration; keeps captions and narration text distinct.
- The illustrated scene now exposes its authored caption as an accessibility description.
- `animateContentSize` is disabled when the existing reduced-motion preference is enabled.
- Existing bounded swipes, explicit previous/next buttons, finite feed, offline illustrated frames and no-mic Speak fallback remain intact.
- No changes to app ID, SharedPreferences keys, XP, media permissions, billing, backend or published content.

## Honest scope
The current Clips player renders locally authored illustrated scenes rather than streamed video. This sprint does not add an adaptive media cache, verified downloadable video, data-saver networking, full device typography settings, captions synchronized with licensed video, or a new artwork pipeline. The existing system/light/dark/night and eye-comfort preferences remain in Profile. No claims that theme choices prevent eye damage.

## Route and QA acceptance
- [ ] Verify Clips > Larger captions > Standard captions, including rotation and large system font.
- [ ] Reduced motion pauses manual scene autoplay and prevents content-size transition.
- [ ] TalkBack announces the scene description and interactive action labels.
- [ ] Verify 320dp handset, tablet, portrait/landscape, light/dark/night and hardware keyboard.
- [ ] Replay, scene navigation, word save, speaking overlay and age-restricted content regressions.
- [ ] Verify Android unit tests, lint, debug APK and monorepo/release checks at exact commit.
- [ ] Real-device screenshots and owner approval before merge.

## Outstanding Sprint 17 full scope
Image/art accessibility review, low-end performance profiling, extended offline pack handling, caching/storage budget and eviction, night-mode device contrast inspection, TalkBack route matrix, reduced-motion animation audit across all six UI sprints, and full tablet responsiveness.

No merge to main, production deployment, paid provider, release signing, or user-data migration.
