# English Mint external 4,000-row workbook — import readiness audit (2026-10-18)

Source: user-provided `English_Mint_4000_Learning_Activities_Master-v3.xlsx`, inspected **locally only**, not copied to repository.

## Structural findings
- 11 sheets: Dashboard, eight categories, Validation_Report, README.
- 500 activity rows in each of Word Builder, Riddles, Vocabulary, Situations, Listening, Reading, Dialogues, Stories; total 4,000.
- 4,000 unique Activity_ID values.
- Within each category: A1 100, A2 125, B1 125, B2 100, C1 50.
- Correct_Answer is `Option_A` in **every one of the 4,000 rows**. This is a severe answer-position leakage problem.
- Distinct Question strings: Word Builder 499/500, Riddles 454/500, Vocabulary 446/500, Situations 490/500, Listening 121/500, Reading 121/500, Dialogues 1/500, Stories 1/500.
- Sampled Vocabulary answers include `The primary meaning of time` and filler distractors.
- Sampled Riddles expose the target word directly in their clue.
- Sampled Situations match unrelated topic nouns to generic phrases.
- Sampled Dialogues and Stories are repetitive/question stems do not test a specific text's meaningful details.

## Import decision: BLOCKED
The spreadsheet has the right **quantity**, not acceptable demonstrated **quality**. Do not import, publish, auto-approve or reward these items.

## Required remediation
1. Replace generic definitions, nonsense clues and meaningless distractors with original context-correct activities.
2. Make reading/listening comprehension questions passage-specific; verify answers can actually be derived from supplied text.
3. Rewrite dialogues and stories for variety, coherent plot/turns and CEFR-level progression.
4. Balance correct option position after checking full answer-option consistency; balancing alone is not educational validation.
5. Run semantic near-duplicate detection and independent language/editorial review.
6. Map accepted items to actual Android activity contracts and verify progress/TTS/next/navigation on device.
7. Retain stable IDs only for items accepted unchanged; document changes to existing identifiers and avoid progress corruption.
8. Test release gate, CI, Android build and sample all 8 categories × 5 levels.

**No import or merge/deploy performed.** This is a preliminary structural/pattern audit, not a complete linguistic review of all 4,000 items.
