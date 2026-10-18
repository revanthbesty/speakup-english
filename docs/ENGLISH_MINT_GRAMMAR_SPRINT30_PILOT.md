# Grammar Arena Sprint 30 — Offline Pilot Engine

Parent: draft PR #37; no merges or deployment.

## Delivered
- Pure Kotlin `MintGrammarPilotEngine` that checks exactly three unique choices, valid answer and explanation; calculates finite packs of six; validates that first-try-correct plus required-retry count equals number of answered questions. JUnit tests cover all three rules, invalid input and partial final packs.
- Existing Grammar Arena shows **offline pilot practice, 18 authored questions**, not a false 10,000-question claim.
- Learner can choose Pack 1, 2 or 3, resetting transient score/feedback when switching, without deleting any mastered question IDs or XP-backed history.
- Existing six-question result popup, Mint Bloom award indicator and learning garden preserved from PR #37.
- No external API, paid AI, new permissions or content publications.

## Not completed
The SRS Sprint 30 exit criterion asks for a 100-question *approved* pilot. The supplied 25 example questions have `sample_not_approved` status; human-independent review has not occurred. So **0 newly approved items** and **0 newly published items** in this PR. Three difficulty tiers and all 25 topic filters also require vetted content. No production-scale quiz engine or 10k bank is claimed.

## Device QA
1. Games > Grammar, choose Pack 1, complete six, verify popup and counts.
2. Choose Pack 2 and see different questions without losing Pack 1 mastery.
3. Choose Pack 3; finishing shows Review from Set 1, not an imaginary fourth pack.
4. Switch packs during an incomplete round; counters reset while existing rewards remain once-only.
5. Restart app and verify saved XP ledger/collectible counts, dark/night theme, 320dp and large-font UI.
6. Require exact SHA CI, Android unit tests, lint and debug APK plus owner acceptance before merge.

**Release status: draft/no-go for production.**
