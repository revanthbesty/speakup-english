# English Mint Sprint 48 — Professional English integration and release audit

## Release decision: NO-GO

This branch stages the Professional English release decision for original roadmap Sprints 39–48. Evidence must be independently verified for thirteen conditions: rights-cleared editorial content, role control, no paid AI dependency, learner data retention, migrations/rollback, physical-device accessibility, privacy and child safety, navigation/reward regression, Android API29/35 instrumentation, same-SHA APK, working editorial backend, and owner approval.

## Status and blockers
- Sprints 39–44 contain original offline exercise pilots, **not** an approved complete multi-profession curriculum.
- Sprint 45 has a local ID/review model and a truthful recommendation preview, **not** tested persistent repetition/rewards.
- Sprint 46 has approval-policy tests, **not** a secure editorial CMS, moderation or publication backend.
- Sprint 47 has offline/free access invariants, **not** verified dynamic-type/TalkBack/real-device performance.
- Sprint 48 enforces independent evidence before release; CI passing alone does not satisfy these requirements.
- AI track Sprints 49–56 currently branches directly from Sprint 45, while this professional 46–48 branch is separate. They must be reconciled on a private, tested integration branch before proceeding to any merge.

## Manual acceptance and data protection
- Verify Home > Professional English > Industries, Workplace, Interviews, Meetings, Writing and Negotiation; all back-stack routes and bottom navigation.
- Check airplane mode, no accounts/payment, user progress/lesson persistence, learning goal and selected difficulty; no false certificates or XP.
- Test API29/35 instrumentation, TalkBack, large text, dark/night themes, low-RAM device and retry/error flows.
- Confirm no external model network egress, no fraudulent local content editorial approval, no child transcript sharing.
- Compare exact Android APK source SHA to build and test result; check migration and rollback on a real upgraded installation.

All release evidence currently **unverified**. No merge to main, launch, paid procurement, or deployment.
