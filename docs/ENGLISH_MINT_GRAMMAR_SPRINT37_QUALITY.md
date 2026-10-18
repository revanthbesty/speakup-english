# English Mint Grammar Sprint 37 — Content Quality and Accessibility Baseline

## Implemented
- Structural validation for the 18 existing offline authored Grammar Arena items: nonempty prompt, exactly three options, no blank/duplicate options, exactly one answer match, nonempty explanation and duplicate prompt detection.
- Grammar Arena displays a visible warning if current pilot data violates those structural checks.
- Grammar choices have explicit accessibility descriptions including answer position and text.
- Pure Kotlin validator tests cover malformed answers, missing explanations, duplicate choices and duplicate prompts.
- No data migration, new permissions, external AI, network dependency, reward change, merge or deployment.

## Still required
- Independent editorial review for pedagogical correctness, answer ambiguity, discrimination and CEFR level.
- Full TalkBack, font scaling, device orientation and low-end Android acceptance.
- Human-review-controlled 500/9,500-question workbook import; this branch checks only 18 local sample questions, not Excel/JSON corpus.
- Exact-head Android CI/debug APK success and on-device smoke test.
- Previous stacked drafts and remaining release blockers still unresolved.

## Acceptance
1. Games > Grammar Arena; options read aloud with index/total when accessed by TalkBack.
2. Invalid local fixture introduced in test triggers warning; valid content shows no warning.
3. Existing bookmarks, mastery, XP and Mint Garden remain intact.
4. Complete questions on light/dark/night, large font and reduced-motion settings.
5. Confirm no claims of reviewed/accredited educational content.
