# English Mint Grammar Sprint 38 — Final Integration and Release Readiness

## Decision: NO-GO (preparation only)
Sprint 38 closes the initial Grammar Arena 29–38 *development draft* sequence, not the quality or production-release gates. The branch retains **18 authored pilot questions**; the requested 500 and 9,500/10,000 Excel content datasets are not established as imported/approved here.

## Implemented
- Pure Kotlin release policy: content editorial verification, stable ID migration, unit/instrumentation, same-SHA APK, accessibility device, offline device, privacy and reward QA, PR-stack integration review, and explicit owner approval.
- Release policy fails closed if any gate lacks accepted evidence and a nonblank reference. Tests enforce rejection of successful APK alone and mismatched corpus approval counts.
- Grammar Arena UI warns that the current pilot is not release-approved. No code triggers publishing, merging, rollout, or data migration.

## Blocking requirements, to be independently verified
1. **Content**: independently audit supplied Excel/JSON grammar bank; document duplicates, one-correct-answer, explanation accuracy, difficulty/CEFR tagging, IP rights and reviewer approvals. Pilot fixtures do not count as production-approved.
2. **Integration**: audit the many stacked draft PR bases and reconcile with main. Use a controlled merge strategy only after explicit permission; rerun tests on final integration commit.
3. **Data preservation**: stable IDs and lossless migration of legacy index-based `grammar_mastered`, existing bookmarks, mistake flags, XP and Mint Garden identities before replacing any question index.
4. **Device regression**: API 29/35, TalkBack, large text, dark/night, reduced motion, offline/denied microphone, answer checking, revision, saved questions, challenges and rewards.
5. **Automation**: full CI, Android compile, instrumentation, lint, signed/nonproduction APK attribution and same-commit artifact verification.
6. **Safety/privacy**: no hidden paid API, no unsafe student data sharing, no duplicated achievements or inaccurate statistics.
7. **Launch operations**: owner approval, rollback instructions, pilot cohort, support and privacy-respecting incident reporting.

## Release decision record
- `VERIFIED_CONTENT`: not established.
- `STABLE_IDS_AND_MIGRATION`: not established for 9,500/10,000 import.
- `UNIT_AND_INSTRUMENTATION`: exact-head checks pending.
- `APK_SAME_COMMIT`: exact-head build/artifact pending.
- `ACCESSIBILITY_DEVICE`: pending.
- `OFFLINE_DEVICE`: pending.
- `PRIVACY_AND_REWARDS`: cross-feature review pending.
- `INTEGRATION_REVIEW`: stacked unmerged branches, pending.
- `OWNER_APPROVAL`: not given.

## Acceptance
Publish and merge must remain disabled. Once requirements are independently evidenced, manually update the release record with traceable references; never auto-mark a gate complete from a GitHub badge alone.

No production deployment, release publishing, automatic PR merge, or public beta activation.
