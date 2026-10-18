# English Mint Sprint 39 — Professional English Expansion Pilot

## Goal and baseline
Start the Professional English Sprints 39–48 expansion without replacing existing Home, Games, Clips, Speak, Profile tabs or disrupting previously saved learning records.
Existing Professional English Hub already lists Business, IT & Technology, Healthcare, Finance, Customer Service, Education, Hospitality and Retail pathways. Its unpublished courses correctly indicate that full industry courses are unavailable.

## This sprint
- Four original, offline three-choice communication scenarios in Business (status updates), IT & Technology (bug reports), Customer Service (complaint empathy) and Hospitality (clear guest directions).
- Industry-page pilot activities use the existing professional hub, provide immediate correct/incorrect feedback and explanation, and clear transient selection when selecting another industry.
- Other four industries remain browsing-only until reviewed content exists. All full professional courses are still unpublished.
- Pure unit tests for unique IDs, nonblank choices, exactly three distinct options and a valid single answer index.
- These are **preview activities**: no credential, certificate, paid AI, XP, Mint Bloom, cloud action or mastery claim.

## Still required
- Verify original Sprints 39–48 SRS track-by-track and obtain expanded, reviewed and rights-cleared domain-specific lesson banks, realistic roleplays, competency rubrics and measured learning outcomes.
- Accessibility QA for TalkBack and large text, offline mode, low-end devices, dark/night themes, navigation and no learner progress regression.
- Exact-head CI, release gates and debug APK; final acceptance on device.
- Review unmerged stacked PR dependencies, especially Sprint 38 PR #59, before any integration with main.
- No merge or deployment in this sprint.

## Manual acceptance
1. Home > Professional English > Explore career topics > Business, IT & Technology, Customer Service and Hospitality: each shows its relevant offline scenario.
2. Choose wrong answer, inspect explanation; select correct response and verify correction.
3. Visit Healthcare, Finance, Education, Retail: no fabricated course or content is shown.
4. Switch industries; answer selection resets, no automatic reward or XP.
5. Return Home, enter Speak/Learn/Review using existing navigation, check no loops.
6. Deny network and microphone; these text pilot exercises still work.
