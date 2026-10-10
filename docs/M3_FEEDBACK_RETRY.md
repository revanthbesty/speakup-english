# M3 — Feedback & Retry Loop

## Purpose

Evaluation is not the end of an activity. SpeakUp converts evaluation into short, actionable guidance and an appropriate retry path.

## Feedback contract

Feedback contains:

- learner-friendly headline
- concise explanation
- score/confidence for system use
- retry guidance when appropriate
- retry support level

The learner should not be presented with a bare `Wrong` state.

## Retry policy

| Outcome | Support | Next action |
|---|---|---|
| CORRECT | NONE | Continue |
| PARTIAL | MEDIUM | Retry |
| INCORRECT | HIGH | Retry |

The retry strategy changes support, not the underlying learning objective.

## Attempt history

A retry creates a distinct `activity_attempts` record. Previous attempts and evaluations remain available for later evidence/mastery processing.

If an active retry already exists, it is reused rather than creating duplicate active attempts.

## API

```text
GET  /api/v1/attempts/{attemptId}/feedback
POST /api/v1/attempts/{attemptId}/retry
```

## Important boundary

Feedback and retry do not directly mutate mastery, proficiency, XP, or entitlements.

Those outcomes belong to the Evidence, Mastery, Gamification, and Entitlement domains.

## Current MVP

The sentence evaluator remains deterministic. AI/open-ended evaluation will enter through the AI Gateway and formal Evaluation Engine later.

## Next

Implement the speaking activity foundation and connect the AI Gateway mock to speaking evaluation.
