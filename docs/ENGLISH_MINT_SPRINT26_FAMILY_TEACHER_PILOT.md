# English Mint Sprint 26 — Teacher & Family Studio Pilot

## Recovered scope
Sprint 26 from the original English Mint 19–28 SRS: **Teacher/Family Pilot**, role-based assignments and dashboards, with permission-matrix and minors' data testing as the acceptance gate.

## Delivered: deliberately limited, local-only pilot
- Home > Teacher & Family Studio · private pilot, opt-in disabled by default.
- Three original sample activity assignments (Word Builder, Reading, guided Speak) that route to existing learning screens.
- Pure local assignment-policy data and tests for valid routes, evidence-gated completion, and denial of access to another learner's progress.
- Explicit labels explain that the samples are not real teacher-assigned tasks or classroom enrollment.
- Existing completion/reward records remain managed by the underlying learning activities. The preview cannot generate Mint Blooms or declare assignments complete.
- No roster, backend account, invitations, guardian connections, social access, child data sharing, or paid services.

## Missing from original exit gate
- Real verified teacher/caregiver identities, relationship proofs, age-gating and explicit consent.
- Classroom/family assignment persistence and provenance; due dates, scoped dashboards and revoked access.
- Secure role-based backend, authorization checks, revocation, access log, data retention/deletion, legal review, and test fixtures representing minors.
- End-to-end child privacy / permission matrix and adversarial security tests. These are prerequisites to actual multi-user launch.
- Device, accessibility, localization, font scaling, dark/light and Android CI acceptance remain necessary.

## Manual acceptance for local preview
1. Open Home > Teacher & Family private pilot; toggle defaults off.
2. Enable; verify three sample activity buttons lead to existing screens.
3. Toggle off and restart; setting persists. No existing progress is altered.
4. Verify no learner roster, child profiles or external share controls exist.
5. Existing activity rewards remain evidence-gated, with no extra Bloom from previewing.
6. Verify build, unit tests, Android lint and same-SHA debug APK.

Stacked on Sprint 25 draft PR #45. No merges or deployment.
