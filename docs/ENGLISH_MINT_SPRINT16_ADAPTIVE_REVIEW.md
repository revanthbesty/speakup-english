# English Mint — Sprint 16 Adaptive Feed and Local Word Saves

**Parent:** unmerged Sprint 15 PR #27. No production merge, backend activation, billing or data migration.

## Implemented
- Deterministic local `MintClipRecommendations`: eligible clips retain their age, difficulty and hidden-story filters from `MintFeedCatalog.visible`. Incomplete story challenges sort ahead of completed ones; private reactions refine ordering. A finite diversified selection helper exists for later curated recommendation surfaces (no server ranking).
- `MintSavedVocabulary`: normalized alphabetic words, stable offline add/remove behavior, no invented translations or dictionaries.
- `MintFeedScreen`: clear local ranking label; a caption word sheet now offers up to eight individual word-saving controls from actual authored dialogue. The existing story completion and private reaction states are used for ordering.
- `MainActivity`: new `mint_saved_vocabulary` Set in existing local preferences, preserved alongside existing learner data, XP, challenge completion, and consent settings.
- Unit tests for bounded ranked selection, unique results, and saved-word toggles.

## Important limitations
The feed remains finite, illustrated offline scenes, not streamed video. This sprint **does not** implement a timed SRS/scheduled vocabulary review calendar, analytics backend, secure cloud sync, AI recommendations, production ranking metrics, or new XP awards. The finite diversity helper has no new public tab or daily network feed. Rankings can reorder when a challenge completes; check current-item continuity on physical devices.

## Review and safety gates
- [ ] Android tests, lint and debug APK on exact PR head; monorepo CI and release gates.
- [ ] Clips Easy/Medium/Hard, age restrictions, hidden clip handling, word sheet, save/remove word and restart persistence.
- [ ] Verify completed challenges cannot farm XP, react/unreact behavior, no duplicate recommendation entry, finite end-of-feed.
- [ ] Device TalkBack, dark/night mode, tablet and compact layouts, 200% font, rapid swipes and orientation/resume.
- [ ] Explicit acceptance and merge approval, plus backend/privacy design before telemetry or cloud recommendations.

**No fabricated progress, usage analytics, pronunciation scoring or recommendations from private inferred data.**
