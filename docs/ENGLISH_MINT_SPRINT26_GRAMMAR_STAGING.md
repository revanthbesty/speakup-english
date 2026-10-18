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
