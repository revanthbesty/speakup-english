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

## Cross-batch audit

Compare all candidate batches and any exported existing-bank data in a single read-only pass:

```sh
python3 content/grammar-arena/tools/audit_batches.py drafts/*.csv --report duplicate-review.json
python3 -m unittest discover -s content/grammar-arena/tests -v
```

The audit rejects exact duplicate IDs/hashes/prompts and flags similar prompts for independent review. It has quadratic runtime; for the full 10,000-question bank replace the all-pairs comparison with a scalable indexing strategy before running in CI. This cannot establish semantic uniqueness or review approval.

The original 100-item pilot was scanned locally in isolation: 100 records and zero duplicate/near-duplicate flags at the default 0.90 threshold. The existing repository question bank has **not** been scanned. Do not report this pilot-only result as whole-bank clearance.

## Staging-only export adapter (no database writes)

The codebase currently models lesson content in `activities` and `activity_versions.payload` and routes through `apps/api/src/app.ts`. No dedicated Grammar Arena question importer has been verified. Until integration contracts and learner-progress behavior are reviewed, export JSON offline only:

```sh
python3 content/grammar-arena/tools/export_approved.py reviewed-questions.csv --output reviewed-staging.json
```

All rows must be approved with reviewer/approval/rights evidence, or export is blocked. This approval evidence is checked for presence only: independent verification is still required. Export JSON is **not** imported, deployed or published by this script. Draft pilot content intentionally fails this gate. Run tests with `python3 -m unittest discover -s content/grammar-arena/tests -v`.

Before wiring the export to the API, review canonical schema migrations and the learner activity, progress and rewards routes. Require non-destructive migration plans, rollback tests, learner data preservation, and separately approved deployment.
