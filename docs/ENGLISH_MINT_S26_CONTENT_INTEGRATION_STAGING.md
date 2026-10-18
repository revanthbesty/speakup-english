# Sprint 26 — 288-content offline integration status

## Implemented and checked
- All 288 candidate records from PR #35 are packaged in the Android assets as `english_mint/draft_replacements_288.json`.
- Offline `MintDraftLibrary` checks the asset count, duplicate IDs, category distribution, levels and unapproved review labels.
- `MintDraftLibraryPreview` provides a read-only editorial browser inside **Home → Teacher & Family Studio → Enable private activity preview**.
- The APK, CI and release-gates workflows for commit `207a8710` completed successfully.
- `MintApprovedGamesBridge` introduces a future Games integration gate with an **empty independent approval allowlist** and tests. Drafts cannot be submitted as playable Games questions or earn rewards.

## What is blocked
The activities have not passed qualified human linguistic review or original-source import parity checks. The current preview is not an assessment engine. The existing Word Builder/Reading game engines still use their own established activity sets. The new bridge does not set scores, track progress, launch TTS or modify achievements; those are acceptance tasks only **after** approval.

## Educational review work still required
- Check each sentence for grammar and meaningful context, distractor correctness, alternative accepted answers and CEFR difficulty.
- Review reading/story word counts and listening transcripts for realistic length and suitable nuance.
- Confirm that each original workbook ID, category and level matches its replacement.
- Assess content repetition and accessibility.
- Independently sign and version an approval manifest. Do **not** infer approval from `review_status` in the JSON.
- After approval, map each approved record to the correct existing Games-specific question/answer format and reuse native device TTS and reward pathways. Avoid double-granting rewards.

## Safety conditions
No real teacher/guardian linking, roster, child progress sharing, score or reward creation, backend credentials, paid services, merge or deploy. Branch remains an unmerged draft PR.
