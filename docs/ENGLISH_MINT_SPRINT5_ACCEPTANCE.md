# English Mint — Sprint 5 Rewards, Growth and Trust Acceptance

## Architecture and migration
This is an incremental upgrade from verified Sprint 4. Existing local SharedPreferences namespace `speakup_learning`, application ID `com.speakup.app`, original lesson/puzzle/revision/XP/speech data and all existing learning screens are preserved. No destructive migration or reward reset occurs.

### Delivered code
- `MintRewards.kt`: pure Kotlin progress/evidence types, 10 participation levels with bounded progress, 10 non-expiring evidence-based badges, and skill-activity summaries across speaking, games, reading, listening and revision.
- Cosmetics-only local coins derived as **5 per distinct first-time Mint Quest completion** from existing `mint_game_awards` keys. Derivation is idempotent; restarting or retesting does not add coins. It creates no purchase, entitlement or user-payable currency.
- Reworked Progress/Review screen displays participation level and next milestone, XP and coins, activity evidence, unlocked/locked badge gallery and an honest assessment/certificate section. Existing spaced revision 1/3/7-day functionality is retained.
- Existing Today dashboard level card now shares the centralized level calculation.
- A learner may meet a *practice readiness* threshold (4 lessons, 3 unique speaking turns, 3 correct reviews), but this is **not certificate issuance**. No generated certificate, accreditation, QR verifier or download button until assessment rubrics and server validation are approved.

### Tests / safeguards
- `MintRewardsTest.kt` checks level boundaries and cap, all badge criteria, duplicate/invalid reward keys and local coin derivation, skill summaries and that XP/badges alone cannot issue a credential.
- Android workflow must complete JUnit, lint, debug APK assembly and publish artifacts; monorepo CI and release gates on the PR head must pass.
- Legacy reward bookkeeping unchanged. Cross-device account merge, idempotent *server-side* reward ledger and anti-fraud trust are a separate backend integration/release gate. Local preferences should not be represented as secure against device tampering.

### Intentionally not complete
- Actual Play purchase, paid coins, premium entitlements or cloud wallet.
- Real certified learning outcomes: requires validated assessment, grader, server issuer, credential lookup/revocation, verifiable signing and clear platform-issued-only disclosures.
- Historical per-day/weekly activity chronology cannot be reconstructed from aggregate local keys; weekly report and streak analytics require consent-aware timestamped events. Do not invent charts for missing history.
- Full live XP/badge animations, coins redemption, seasonal events and signed certificates are not release-verified.
- Physical-device TalkBack/large-text and old-version app-upgrade tests deferred until Sprint 10 by the user; automated CI is not a substitute.

## Sprint 5 exit gate
All Android and monorepo CI checks green on final commit; a reviewable stacked PR with no production deployment or merge. User first installs and reviews after Sprint 10.
