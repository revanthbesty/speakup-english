# English Mint Sprint 43 — Email & Business Writing Pilot

## Implemented
- Professional English > Email & business writing pilot.
- Four original offline questions in two categories: Email (effective subject lines, courteous document review requests) and Business Writing (action summaries, honest progress updates).
- Three options per question, explanatory corrective feedback, topic switching, and next-exercise navigation.
- Optional private writing field (600-character limit) for personal practice; text lives only in current Compose state and is discarded on topic/question change or when screen state is destroyed.
- Pure Kotlin tests verify unique IDs, category coverage, and answer/explanation integrity.
- No email transmission, external AI, grading of free-form writing, accounts, extra XP, Mint Bloom or certification.

## Important scope limitations
- Initial four-item pilot only, not a full workplace writing curriculum, actual email editor, communication delivery service or reliable rubric-based feedback.
- User-entered draft is neither saved across app restart nor transmitted; do not imply durable storage or automated evaluation.
- Content has not undergone independent editorial/level approval.
- App navigation, Compose visual accessibility, large fonts, offline mode, theme modes and real-device QA still require verification.

## Acceptance
1. Home > Professional English > Email & business writing pilot; verify both categories and four items.
2. Choose incorrect answer, check explanation; choose suitable response and verify feedback.
3. Enter an optional draft; change question/category and verify it clears; never claim saved draft.
4. Verify no network/email permission, external processing, microphone, XP or additional Bloom.
5. Reopen Professional Hub and existing Interviews, Meetings, Workplace pilot navigation.
6. Check same-SHA Android CI/build, accessibility, airplane mode and user-device acceptance.

Stacked on unmerged Sprint 42 draft PR #63. No merge/deployment.
