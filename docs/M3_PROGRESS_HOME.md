# M3 Progress + Home

## Purpose
Turn authoritative learning signals into a learner-facing state snapshot without creating a second source of truth.

## Endpoints
- `GET /api/v1/home` — Home-ready learner snapshot.
- `GET /api/v1/progress` — full learner progress snapshot.
- `GET /api/v1/progress/journey` — journey progress and next activity.

## Snapshot
The response combines:
- learner profile and placement
- active journey and lesson progress
- derived journey completion percentage
- next unfinished activity
- skill mastery and evidence counts
- XP, level and reward count
- activity/learning-day metrics
- a minimal explainable next-action recommendation

## Progress semantics
Progress is derived from learning evidence, not from clicks or technical completion alone.

`Activity completion != Learning evidence != Mastery != XP != Proficiency`

An activity contributes to lesson progress only after at least one learning-evidence record exists for an attempt owned by the learner.

## Home recommendation
The MVP recommends continuing the current lesson. If the lowest-mastery skill is below `0.60`, the explanation calls out that skill as the current strengthening focus.

This is intentionally a foundation. The full Recommendation Engine will later incorporate goals, recency, difficulty, review scheduling, repetition control, context variety and confidence.

## Progress synchronization
When evidence is created, the evidence route synchronizes the related lesson progress. This keeps the explicit lesson-progress record aligned with the authoritative evidence stream while preserving evidence as the learning signal.

## Testing / environment note
Static TypeScript validation is limited by the build environment when workspace dependencies and Node type packages are not installed. Existing project-level dependency installation remains a prerequisite for full package tests and runtime integration tests.
