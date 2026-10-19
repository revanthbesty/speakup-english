# English Mint — Master development and quality sprint audit
Updated: 2026-10-19. Source: PR #94, working branch `ui/approved-design-rebuild-phase1`.
This is a code-level audit, **not** visual or on-device acceptance.

| Area | Confirmed implementation | Remaining acceptance gap |
| --- | --- | --- |
| Onboarding | Selected-pet flow with skip option; stored pet preference | Approved-design screenshot parity; Back/rotation and 320dp/large-text device QA |
| Profile | Selected-pet summary with explicit Change companion action; optional pet sound previews | Screenshots in both themes; TalkBack and persistence after force stop |
| Six companions | Compose Canvas character-specific markers and distinct movement/copy | Production-ready approved artwork, comprehensive idle/blink/tail/wing motions |
| Games, Grammar, Vocabulary, Riddles, Reading, Listening, Situations | Checked-answer pet reaction in the named routes | Per-route instrumentation; feedback coverage of all additional learning modes |
| Mintverse | Growth stages based on completed lessons; mini-adventure invitation | Rich living-world interactions, milestone animations, quest integration |
| Sound | Opt-in offline chimes, six pitch families, manual Profile previews, silent and muted volume checks | Audio focus and safe audio interruption, automatic synchronization, separate background-music setting/player |
| Learner progress / XP | Existing stored preferences and once-only award paths retained in touched code | Regression tests for all award paths, repeat/retry and restart scenarios |
| Accessibility | Reduced-motion behavior implemented for pet poses | Screen-reader and 200% font coverage, contrast and small-screen device QA |
| Android builds | Workflow CI / release gates / Debug APK triggered per commit | Check exact latest commit results; confirm Android API 29 and 35 instrumentation coverage and lint tasks |
| Application-wide UI | Shared design components exist | Full screen/route inventory, original UI Sprint 1–6 boards comparison and deduplication |

## Immediate acceptance checklist
1. Install the latest **successful** APK matching PR HEAD, not an older successful build.
2. Verify onboarding pet pick/skip, then Profile summary / Change companion / Cancel.
3. Repeat for six companions, force-stop/relaunch, verify selection persists.
4. Check correct/wrong/retry for all six integrated activities; confirm rationales and no duplicate XP.
5. Toggle motion reduction, sound opt-in, silent/vibrate/media mute. Do not use sound previews while recording/listening.
6. Check Home Mintverse stages, garden navigation, dark/light themes, large fonts, accessibility labels.
7. Capture device screenshots and compare to approved English Mint UI Sprint 1–6 reference boards.
8. Block merge until visual acceptance and user approval. No deploy.
