# Grammar Arena — offline content validation (draft)

This directory is deliberately **not wired to the runtime question bank**. The current main branch is a pnpm/TypeScript web/API monorepo; do not assume Android asset paths. This is authoring infrastructure only.

## Content contract

Plan: 25 domains × 400 questions (120 Easy, 140 Medium, 140 Hard) = 10,000. Each item needs exactly three choices, one defensible answer and useful explanations. Generated content begins as `draft`; passing a script is not editorial approval.

## Validate a local pilot

Obtain the independently supplied `English_Mint_Grammar_Arena_100_Draft_Pilot.zip` and inspect its records before import. Do not assume the package is checked into Git.

```sh
python3 content/grammar-arena/tools/validate_questions.py /path/to/pilot_100_draft.csv --report grammar-validation.json
```

The script checks required fields, status, choice counts, duplicate IDs/hashes/normalized prompts and basic review metadata. Flagged semantic near-duplicates and answer ambiguity require human review; a hash is not proof of uniqueness. Cross-batch checks require combining all candidate and existing-bank records into a single audit input or an enhanced registry.

## Release gate

Draft → structural checks → independent grammar/answer review → accessibility and rights review → approved → separately authorized publishing.

Do not import unpublished questions into playable sessions. Do not alter learner progress keys, live data, reward issuance, authentication or deployment. No paid AI is needed.

## Follow-up work

1. Locate and inventory the actual existing question bank and its persistence keys.
2. Map the app's real runtime schema to a versioned content contract and develop a non-destructive importer.
3. Add fixture tests, semantic-review queue, cross-batch registry and 25-domain manifest.
4. Have humans review all 100 pilot questions before eligibility to publish.
5. Validate UI navigation, offline behavior and earned-reward eligibility only after the actual runtime path is understood.
