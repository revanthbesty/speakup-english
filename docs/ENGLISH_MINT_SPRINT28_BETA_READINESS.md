# English Mint Sprint 28 — Staged Beta and Launch Readiness

## Original recovered requirement
Sprint 28 in the English Mint 19–28 SRS: **Staged beta & launch gate** with A/B experiments, metrics, rollback and support. Exit requires security, policy, usability sign-off. This is **a preparation/audit increment only**, not an approval to beta launch or production deployment.

## Evidence-backed baseline
- Base is Sprint 27 draft PR #49, commit `9e4d518787cc47688ac2b9ffa6dfe7d4187889e6`.
- That base's GitHub **CI, Android Debug APK, release-gates** completed successfully. An APK is only a smoke-test artifact, not end-to-end product acceptance.
- Earlier work is stacked across unmerged draft PRs. A PR merge/rebase and regression plan is required prior to shipping.

## Code added
- `MintBetaReadiness`: pure explicit evidence gate with mandatory CI, Android APK, accessibility device QA, offline device QA, privacy/consent, child safety, editorial content review, data migration, documented rollback and owner approval.
- All gates fail closed when evidence is missing/unverified; automatic inference of a release from a successful build is forbidden.
- Regression unit tests ensure CI and APK alone cannot authorize shipping, missing references fail, complete separately verified evidence would be required.
- Profile label identifies this as a development build, not a public-launch authorization.

## Unresolved exit-gate / release blockers
| Priority | Blocker | Validation required |
|---|---|---|
| P0 | Unmerged stacked PRs and integration | Controlled base rebase/merge order, CI after integrating actual target branch, rollback |
| P0 | Physical-device and TalkBack testing | Small/low-end phone, API 29/35, text scaling, reduced motion, dark/night and navigation |
| P0 | Child safety/privacy for co-op and family | No online room launch without validated identities, consent, moderation and role tests |
| P0 | Speaking Passport full scope | Record/replay/compare with explicit consent, storage export and deletion QA; current notes-only prototype is not equivalent |
| P0 | Creator Lab publishing | Authenticated role-based review, rights evidence, audit trail, versioning, rollback; publication presently disabled |
| P0 | Editorial content volume | Human review of large-scale quests/stories, proof of rights and avoidance of duplicate/ambiguous answers |
| P0 | Progress migration and backups | Non-destructive upgrade verification with representative legacy local preferences and rewards |
| P1 | Offline + low-end performance | Airplane mode matrix, memory/scroll/startup profiles, caching behavior |
| P1 | Reward Universe completeness | Audit all activity completion screens, no visible primary XP and once-only Mint Bloom semantics |
| P1 | Beta operations | Defined cohort, support escalation, crash/error metrics with privacy safeguards, rollback owner and A/B design |
| P1 | External AI and billing | Cost-free deterministic fallbacks verified, no paid endpoints/billing secretly active |

## Manual acceptance matrix
1. Backup a nonproduction test profile, install debug APK over previous debug build and compare legacy lessons, XP ledger, quests, blooms and privacy preferences.
2. Verify bottom tabs Home, Games, Clips, Speak, Profile and contextual Professional route; confirm back navigation and no dead-end loops.
3. Test Easy/Medium/Hard for representative activities and newly introduced sections: Real-World Quests, Mint Coach, Speaking Passport, Co-op Stories, Career & Exam, Creator Lab, Family Studio.
4. Deny microphone and network; verify quiet text or skip alternatives. Never count a skip as a verified voice event.
5. Confirm repeat completion gives no additional collectible; Home Mint Garden totals are stable after restart.
6. Confirm no public chat, payments, unapproved creator publishing or external role access.
7. Profile > appearance: system/light/dark/night, reduced motion, large text, accessibility.
8. Inspect git branch integrations and same-commit unit/CI/debug APK reports.
9. Owner decides launch readiness only after independently verified security, policy, usability and rollback gates.

## Explicit release decision
**NO-GO / draft-only** until all gates are evidenced. There has been no live beta cohort, A/B experiment, launch metric collection, staged rollout or production deployment performed by this Sprint 28 PR. Do not merge or deploy without explicit permission.
