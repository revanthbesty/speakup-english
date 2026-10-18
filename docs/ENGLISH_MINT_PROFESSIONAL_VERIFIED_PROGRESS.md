# English Mint — Sprint 45 Verified Professional Practice Completion

## Implementation on top of Sprints 45–66 integration
- Uses a separate persisted set of **stable authored content IDs** in existing `speakup_learning` SharedPreferences: `mint_professional_verified_practice_v1`.
- Four industry communication pilot questions, four interview practice items, four meeting/presentation items, four business-writing items and four negotiation/problem-solving items earn an entry only when the checked **preferred** option is selected.
- Two workplace roleplay scripts earn completion only after the final correctly verified dialogue turn; answering earlier turns does not record full activity completion.
- The current pilot has **22 identifiable activities**: 20 single-question practices and two three-turn conversations. Stale/unrecognized IDs are ignored in the displayed count, but not destroyed in on-device storage (future migration protection).
- Completing the same item again does not create an additional entry. No XP, Mint Bloom, certificates, endorsement or proficiency rank is generated.
- The Professional Hub now reports verified count from local evidence and derives a deterministic next practice suggestion from remaining authored IDs. The previous `next(emptySet(), null)` preview did not use real progress.
- A separate confirmation allows deleting **only** this new Professional completion ledger without deleting XP, rewards or standard lessons. No personal text or workplace information is saved in the ledger.
- Unit tests cover missing/invalid IDs, checked-correct gating, once-only updates and unique IDs across six authored module sources.

## Important limitations
This verifies a **multiple-choice learning task**, not a recording, interview skill, speaking fluency, workplace performance or independent human-reviewed proficiency. There is no spaced repetition scheduling, separate per-account identity, sync or remote analytics. A shared local Android profile uses the pre-existing SharedPreferences namespace. Retain existing preference keys and do not migrate historic XP or lesson IDs without an approved migration.

## Device QA
From Home > Professional English, complete a single original item after selecting the preferred answer and checking, return to Hub, verify count +1, reopen application and confirm count persists. Select a wrong option and confirm no count increment. Replay an already completed item and confirm no duplication. For Workplace, only completion after third correct turn should count. Test all six module routes, airplane mode, confirm-to-delete and Cancel, TalkBack, API 29/35, dark/night, large font, and legacy saved data.

Original Professional English Sprints 45–48 remain incomplete on editorial CMS, rights review, cross-device accessibility and release gates. Do not merge/deploy.
