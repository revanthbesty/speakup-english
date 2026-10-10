# M3 — Daily Goals + Notifications

Meaningful learning is the only source of daily progress and streak eligibility.

## Rules
- App opens/clicks do not count.
- Technical failures do not count.
- Only attempts with learning evidence count.
- Daily minutes use learner timezone.
- Speaking uses recorded speaking duration; other evidence-backed activities use their active attempt interval.
- Goal completion is `meaningful_minutes >= target_minutes`.
- Streaks require consecutive local days whose goals were met.
- Notification eligibility is preference-aware and goal-aware.
- Notification events are idempotent per learner/type/local date.

## APIs
- `GET /api/v1/daily`
- `PATCH /api/v1/daily/goal`
- `GET /api/v1/daily/streak`
- `GET /api/v1/notifications/preferences`
- `PATCH /api/v1/notifications/preferences`
- `GET /api/v1/notifications/daily-goal/eligibility`

This slice establishes eligibility/state; provider-specific push delivery is intentionally separate.
