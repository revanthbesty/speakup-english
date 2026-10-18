# English Mint Grammar Arena Sprint 35 — Local Learning Analytics Pilot

## Implemented
- Games > Grammar Arena has a private snapshot of 18 existing pilot questions: mastered count and percentage, not-yet-mastered count, saved bookmarks and currently flagged missed questions.
- Calculations count only IDs in the active pilot. Unknown/stale legacy IDs do not inflate metrics.
- Derived exclusively from existing local SharedPreferences sets; no telemetry or new permissions.
- Unit tests cover valid ranges, stale IDs and empty states.
- Clear wording: this is neither a proficiency score nor historical attempt accuracy, and the previously recorded XP remains unaffected.

## Limits and verification
- Does not measure total attempts, first-try success trends, time-on-task, long-term retention or CEFR proficiency because historical event data is not available from the pilot.
- No paid AI, remote analytics, personal identity sharing, or fabricated date-series charts.
- 9,500/10,000-question workbook ingestion still awaits separate independent content validation and lossless ID migration.
- Requires exact-head Android build, CI and release gates; device checks for TalkBack, font size, dark/night, restart persistence and no duplicate rewards.
- Draft stacked on Sprint 34 PR #55. No merge/deploy.

## Manual acceptance
1. Open Games > Grammar, note snapshot counts.
2. Save and unsave a question; saved count changes while mastery remains unchanged.
3. Answer incorrectly; flag count increases, then retry correctly; flag clears.
4. Complete new unique question; mastery rises once only; replay does not increase.
5. Restart app; snapshot remains consistent with stored data.
