# English Mint Sprint 20 — Real-World Quest MVP

## Requirement source and scope
Recovered `English_Mint_Sprint19_Plus_Master_SRS_Designs_AI_Prompt.docx`: Sprint 20 is **Real-World Quest MVP**, two realistic scenarios and speech steps; exit gate is end-to-end quest and safe skip tests. This branch is stacked on the latest draft PR #38, **not merged to main**.

## Implemented
- Games > **Real-World Quests**: two original, offline authored scenarios, **At a café** and **At a train station**.
- Every quest has a realistic partner line, three original answer options, one correct key, context explanation and an optional speaking prompt.
- Use existing just-in-time Android speech recognizer consent path. Text is offered without microphone access and **Skip speaking without penalty** is available. A skipped step is not a completed voice attempt or pronunciation score.
- Quest completion requires a correct checked A/B/C reply **and** a voice transcript, typed reply, or explicit skip. Navigation alone earns no reward.
- Stable local once-only completion records under `mint_real_world_quests_completed`, using unique `rwq_<scenario ID>` keys. Existing learner progress/XP records unchanged.
- Saved real-world quest awards feed the **My Mint Garden** Quest Emblem collection, with a themed Mint Bloom success card and Next Quest/Garden actions.
- Unit tests for exactly two safe distinct cases, option integrity, optional step semantics, and stable reward keys.

## Acceptance matrix
1. Games > Real-World Quests > Café: choose wrong, see correction, choose correct, use typed answer, Complete; see Bloom and Garden count.
2. Repeat Café; see “Previously completed” and **no additional reward**.
3. Station: choose correct; skip speaking, Complete; no voice attempt claimed.
4. Speech path: prompt for device consent, verify recognized transcript when available; microphone cancel/denial retains typing and skip.
5. Switch missions and restart app: existing quest completions persist; neither overrides Grammar, Clips, Speak, lesson or XP state.
6. Verify reduced-motion option, dark/night, TalkBack, large text, navigation and small phone manually.
7. Same-commit GitHub CI, Android unit tests, lint and debug APK must succeed; device acceptance is separate.

## Explicit limits
- This is a **two-scenario MVP**, not a complete authored real-world quest library, not automated speech scoring, and not a paid AI feature.
- Quest progress is local; no cloud sync or social sharing.
- Existing learner data is not migrated or deleted. No merge, deployment or external billing or API changes.
