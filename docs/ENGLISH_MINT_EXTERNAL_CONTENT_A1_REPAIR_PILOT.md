# English Mint uploaded JSON audit and A1 repair pilot

Source files: owner-provided `English_Mint_4000_Master_Content.xlsx` and `English_Mint_4000_JSON_Export.zip`. The JSON export was read-only audited, not modified or published.

## JSON audit result
- 4,000 records; 4,000 distinct activity IDs.
- Exactly 500 records/category.
- CEFR distribution per category: A1=100, A2=125, B1=125, B2=100, C1=50.
- Correct-option distribution overall: A=999, B=1,020, C=984, D=997.
- Distinct central material per category (JSON structural equality): Word Builder 481, Riddles 25, Vocabulary 25, Situations 99, Listening transcripts 25, Reading passages 25, Dialogues turns 25, Stories texts 25.
- These figures show drastic material reuse; correct counts and answer distribution alone do not imply release readiness.

## Repair pilot
`content/english_mint_4000/external_workbook_repair_pilot_24.json` contains 24 manually rewritten A1 drafts: 3 per category. The replacement samples keep the original IDs of the first three A1 items in each category. Do not concatenate them with the original export: **replace matching IDs only** after review. All new records retain `needs_human_review`.
- Listening includes TTS-ready transcripts but has **not** been device audio-tested.
- Reading includes independent brief passages; dialogues include structured turns; stories include beginning, development and ending.
- These are examples toward substantive rewriting, **not** a declaration that 24 items are approved or deployed.

## Blocking actions
1. Qualified linguistic review and distractor correction of the pilot.
2. Replace repetitive originals in every category with distinct, level-appropriate material.
3. Audit Excel versus JSON row parity separately before any bulk import.
4. Only import records with verified category-specific schema, answers and rights/provenance.
5. Test Android screens, device TTS and saved learner progress on real devices.
6. Never automatically merge, deploy or reset learner data.
