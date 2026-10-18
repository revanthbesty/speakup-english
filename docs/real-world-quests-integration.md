# English Mint — 100 Real-World Quests integration

Status: **integration preparation only**. Do not publish content without editorial approval.

## Source
User-provided `English_Mint_100_Real_World_Quests_Final.xlsx`; converted and structurally checked into `English_Mint_100_RWQ_Integration_Package.zip`. The source package is a conversation artifact and **has not been committed** to this repository.

## Target
Next.js App Router under `apps/web/app/`. Add Games → Real-World Quests navigation using the existing shared Nav component. Keep unrelated routes unchanged.

## Data contract
JSON `schema_version: "1.0"` with `categories[]` and `quests[]`. Quest identifiers `RWQ-0001`…`RWQ-0100`. Options `option_a`, `option_b`, `option_c`; `correct_answer` is A/B/C. Show `correct_explanation` for the correct response and the matching `wrong_a_explanation` / `wrong_b_explanation` / `wrong_c_explanation` for incorrect responses. Keep `content_status=draft` hidden from production users until approved.

## UX acceptance criteria
- Show categories, difficulty, quest progress, estimated time, and clear first-step guidance.
- Display context, character dialogue, question, and three tappable answers.
- Explain correct AND incorrect responses; allow retry without XP farming.
- Provide speaking prompt with accessible text-entry fallback.
- Display correct/wrong count and an accessible celebratory completion state after each completed quest.
- Persist attempts and awarded XP safely; avoid double awards.
- Provide keyboard focus, screen-reader status, reduced-motion support, and mobile layout.

## Validation
100 unique quest IDs and scenarios; 10 categories; 40 Easy / 35 Medium / 25 Hard; answer balance A=34, B=33, C=33. The workbook remains editorially draft.

## Required before merge
Import all 100 JSON records from the user-supplied package, add feature UI and tests, run `pnpm verify:release`, review content editorially, and verify staging. No production deployment or merge is approved by this change.
