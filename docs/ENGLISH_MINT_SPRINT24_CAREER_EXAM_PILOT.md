# English Mint Sprint 24 — Career & Exam Packs pilot

Recovered scope: interview, travel and selected exam content, requiring a content review and rubric QA gate. This **small pilot** includes six original offline scenarios (two per track); it does not claim official IELTS/TOEFL affiliation or complete the full original sprint.

## Built
- Games > Career & Exam with three paths: Interview, Travel, Unofficial Exam Practice.
- Two authored situational multiple-choice items per path, three options and a single defensible correct key, explanatory feedback and optional speaking/text practice via existing Speak flow.
- Persist once-only correct-answer mastery under `mint_career_awards`; count awards as Blooms in Garden without changing legacy XP data.
- Pure tests for options, correct keys, track distribution and stable reward IDs.

## Acceptance / remaining
- Device checks: each path, wrong/right correction, Speak fallback, Garden count and persistence on restart.
- Android CI/debug APK/lint, accessibility and real device QA.
- Independent editorial content and competency/rubric assessment remain pending; current questions are code-authored demo fixtures and not an accredited scored exam.
- Full mock interviews, multi-turn travel plans, mock-exam assessment and wider approved content packs require later work.
- This is stacked on unmerged Sprint 23 draft PR #42; no merge/deploy.

## Safety
No licensed exam items copied, no payment, no external AI use, no recording uploads, no certified scores, and no deletion/migration of historical rewards.
