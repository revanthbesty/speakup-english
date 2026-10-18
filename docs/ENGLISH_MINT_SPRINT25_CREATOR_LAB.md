# English Mint Sprint 25 — Creator Content Lab (draft-only prototype)

## Original roadmap
Recovered Sprint 19–28 SRS: Sprint 25 calls for Creator Lab with **rights, editorial approval, scheduling, and rollback**, and the exit gate that **no unpublished draft can bypass approval**. This branch supplies a deliberately limited, local-only demonstration of the draft and approval-sequence UI; it is **not** an authenticated editorial CMS or publishing system.

## Implemented
- Home > Creator Content Lab entry point; author can type an original short title, scene/dialogue and rights note and preview locally.
- Pure Kotlin editorial state machine: DRAFT → RIGHTS_CHECKED → LANGUAGE_REVIEWED → SAFETY_REVIEWED → READY_FOR_SCHEDULING; no transition that produces a PUBLISHED state.
- The preview UI requires demonstration checkboxes before moving between states and explicitly states that these are NOT verified independent staff approvals.
- Content is held only in Compose screen memory. It is not synced or persisted; leaving the screen may discard it.
- No public publishing, upload, schedule, real reviewer identity, rights verification, app entitlements, payment, AI calls, media upload or external side effect.
- Unit tests verify draft cannot progress without its rights note, review sequence is ordered, and publishing is always denied.
- No Mint Bloom reward is awarded merely for editing or previewing a draft.

## Mandatory original-sprint gaps and blockers
- Authenticated author/editor/moderator permissions and audit trail.
- Durable draft storage, history and data recovery across app restart.
- Actual intellectual-property rights evidence; verified staff review and role segregation.
- Content scheduling/publish/retire processes, rollback, and safe moderation/appeals.
- Policy-controlled publishing backend, content revision IDs, access control, privacy protections and rollback QA.
- End-to-end accessibility, device and adversarial publishing bypass tests.

## Test and acceptance plan
1. Home > Creator Content Lab: preview a draft after entering title and script.
2. Rights stage cannot advance without rights note and explicit local confirmation.
3. Language and safety stages require separate confirmations, and no Published button appears.
4. Exit/reenter: preview remains non-persistent; explain this is a prototype and not saved user work.
5. Android release build, unit tests, lint, CI and gates must pass for exact head.
6. Screen-reader, font scaling, reduced motion and no unauthorized network/content publication verified on physical device.

**Draft stacked on Sprint 24 PR #44. No merge, deployment, or activation of creator-generated public content.**