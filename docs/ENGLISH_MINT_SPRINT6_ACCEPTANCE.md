# English Mint Sprint 6 — Entertainment Feed, Safety and Moderation Boundary

## Verified starting point
Stacked on Sprint 5. Android app uses local `speakup_learning` SharedPreferences and `com.speakup.app`, with no approved cloud social identities, licensed external-video library or active staffed moderation API. The existing admin has a general safety page but is **not** treated as a validated user-generated-content moderation backend.

## Implemented
- `MintFeedCatalog.kt`: 12 original illustrated, captioned English short-story sequences. All have three authored visual/caption/spoken-line frames, comprehension questions with validated choices, difficulty classification and age minimum.
- `MintFeedScreen.kt`: modern vertically swipeable finite feed, frame-by-frame controlled playback (2.8-second transitions when playing), pause/manual navigation, captions toggle, optional TTS via existing `ListeningAudio`, comprehension answers, speaking missions, and explicit end-of-feed stop. The app honors reduced motion and does not autoplay indefinitely.
- `MintSocialSafety.kt`: auditable closed-by-default action policy. Watch, private reactions and private speaking practice are available; comments, direct messages, new posts, follows, shares and friend challenges are **disabled for every age** pending secure verified identity, age/guardian consent, moderation and abuse-response operations.
- `MainActivity.kt`: new Home/Feed/Games/Speak/Profile bottom navigation, retaining the Progress/Review page via Home. Preserves previously implemented games, speaking and XP.
- Private reactions `mint_private_reactions`, hidden clip IDs `mint_hidden_clips`, local report keys `mint_local_clip_reports` and completed comprehension tasks `mint_story_completed` stored only in the original app preferences. No secrets, public posts, files, microphone recordings, network transmission or access to strangers.
- Hiding removes a clip from the local feed; restore hidden items available in Profile. Report categories are shown with a blunt disclosure that reports are **not submitted to moderators**. Report status is never falsely represented as submitted or reviewed.

## Test plan and evidence
`MintFeedSafetyTest.kt`: exact curated catalog coverage and content validation, age/difficulty filtering, blocking, closed child/adult public interactions, and local-report validity.
Android automated workflow: `:app:testDebugUnitTest`, `:app:lintDebug`, `:app:assembleDebug`, APK and verification reports. Monorepo CI and release gates run on draft PR.
Physical swipe gesture/accessibility/large font, TalkBack, TTS interruption, reduced motion and app-upgrade smoke tests remain Sprint 10 device acceptance as agreed.

## Honest limitations / hard gates
- These are original **illustrated animated micro-reels**, not licensed externally hosted video clips or actual encoded video files. A production MP4/streaming feed still needs owned/licensed assets, captions, CDN, bandwidth management, moderation, consent and media player integration.
- There is **no public community** or real-time remote reporting. A local report is a private on-device record, not a moderation case. Report/appeal SLA, staffed dashboard, blocklist sync, child consent and abuse handling must be developed and verified before any user-generated-content launch.
- Public social features are intentionally not enabled even for adults. No unrestricted child-to-stranger chat.
- No endless recommendation feed, push notifications, paywall or external analytics. Content is finite and non-manipulative.
- No invented remote likes or global engagement counts: all reactions are private.
- Child-suitable content catalog must undergo educator/child-safety editorial review before public release.

## Exit gate
Sprint 6 code tests/lint/build/CI green and reviewable PR; preserve all prior progress; do not merge/publish/enable externally hosted user content. User first installs after Sprint 10.
