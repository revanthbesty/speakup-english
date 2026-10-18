# English Mint Sprint 26 — Grammar Arena staging integration

Base: `feature/english-mint-s26-family-teacher-pilot` (Sprint 26 draft PR #46).

## Implemented
- Pure Kotlin `MintGrammarDraft`, `MintGrammarGate`, and option-specific answer feedback contract.
- Defensive approval gating: draft/validated/rejected content cannot be loaded into the publishable batch; reviewer, timestamp and rights evidence are required.
- Tests for draft rejection, correct/wrong explanations, incomplete metadata and duplicate IDs.

## Explicitly not yet integrated
- The 500-question `English_Mint_9500_500_All_Rewritten_DRAFT.xlsx` is NOT checked in and NOT loaded in the Android app.
- The 500 drafts include 4 duplicate prompt occurrences and 15 missing examples; zero questions have independent editorial approval.
- No actual Excel/JSON parser, Android asset import, content selection or persistent quiz scoring was changed.
- Existing hard-coded Grammar Arena questions and Sprint 26 Teacher/Family pilot behavior remain untouched.
- GitHub PR #36 is a separate tooling branch based on main; this branch does not merge it.

## Next acceptance gates
1. Editorially resolve duplicate prompts/missing examples and verify one defensible answer and the three option explanations per question.
2. Package approved-only content in a versioned text format with stable IDs; compare with existing app bank.
3. Add runtime importer and migration compatibility tests on top of the current Android branch.
4. Run Android JUnit, lint, APK build, navigation and accessibility device checks at a pinned commit.
5. Keep all work in draft PRs until approved merge and separate deployment verification.

## 500-question Android authoring export (prepared, not committed)

Local exported file: `English_Mint_500_Android_Draft_Bank.json` (500 draft questions, schema version 1).

- Export SHA-256: `c62d673ae52897ea91760c0955d355c260c1da678ffe10b3240c33c118b8d0ef`
- Unique IDs: 500; distinct normalized prompts: 496; missing example sentences: 15
- The JSON file is **not checked into GitHub** because the current connector cannot upload the prepared ~481 KB binary/source artifact as a direct local file.
- `MintGrammarJsonLoader.parseDraftBank(json)` parses metadata and all three answer explanations; `playableQuestions(json)` rejects draft content.
- The loader is intentionally not connected to the scored Compose quiz screen. Existing questions continue to work unchanged.
- Parser instrumentation/unit test setup is outstanding (Android `org.json` is not supported reliably by plain JVM local unit tests without suitable test runners).

To finish data ingestion after corrections: add the JSON through an approved file-transfer or source-commit workflow, test the actual Android asset read path, and secure independent editorial approval before enabling playable content.

## Update — 500 draft records checked in

The complete 500-record JSON asset is now committed at `apps/android/app/src/main/assets/grammar_arena_500_draft.json`. `MintGrammarDraftPreview.load(context)` reads it locally for authoring/review; `MintGrammarDraftPreview.playable(context)` enforces the approved-only gate and currently returns no questions because all 500 are drafts.

**This supersedes earlier notes saying the JSON was not checked in.** No scored Grammar Arena screen or rewards were connected. This is staging integration, not a playable release.

Outstanding: correct four repeated prompts and 15 missing examples; independent editorial review; UI preview integration, Android compilation/instrumentation, real-device accessibility and navigation tests. Do not merge or deploy.

## Sprint 26 draft-review panel update

- The checked-in 500-question authoring asset now has 500 distinct IDs and 500 distinct normalized prompts. Four repeated prompts were rewritten; 15 missing example sentences were supplied. All records remain `draft`.
- `MintGrammarDraftReviewPanel()` is available inside the existing Grammar Arena screen, with A/B/C explanations and grammar rules shown after selecting an answer.
- The preview is explicitly unscored, never modifies learner records, and leaves the existing scored quiz intact. No draft record is promoted to approved.
- No claim of independent editorial validation: 500 items still need human language review. Semantic near-duplicates and ambiguous answer keys are still possible.
- Android tests, lint and APK build have **not been executed** for this update. These must pass before any merge.
