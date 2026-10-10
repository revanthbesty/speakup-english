# M2 — Diagnostic & Placement

## Diagnostic lifecycle

`CREATED → IN_PROGRESS → COMPLETED`

Onboarding progresses through:

`DIAGNOSTIC_READY → DIAGNOSTIC_IN_PROGRESS → PLACEMENT_READY → PLACEMENT_CONFIRMED`

## Foundation diagnostic sections

1. Vocabulary
2. Grammar
3. Listening
4. Reading
5. Sentence Construction
6. Speaking

Each section is versioned and owns its attempts.

## Attempt model

Every attempt records:

- section
- attempt number
- learner input
- outcome
- score
- confidence
- evaluator metadata

The foundation evaluator is deterministic. Speaking/AI evaluation remains behind the AI Gateway and can be added without changing the diagnostic API contract.

## Result

Completion produces:

- section results
- skill profile
- recommended proficiency
- confidence
- focus skills
- support level
- placement decision

Proficiency remains independent of audience/age.

## Placement

The system produces a **recommendation**, not a permanent label.

The learner can:

- accept the recommendation
- override the recommended proficiency
- retain the original diagnostic result for auditability

## API

```text
POST /api/v1/diagnostics
GET  /api/v1/diagnostics/{diagnosticId}
POST /api/v1/diagnostics/{diagnosticId}/start
POST /api/v1/diagnostics/{diagnosticId}/attempts
POST /api/v1/diagnostics/{diagnosticId}/complete
GET  /api/v1/diagnostics/{diagnosticId}/result
POST /api/v1/diagnostics/{diagnosticId}/placement
```

## Important production boundary

The equal-weight deterministic evaluator is deliberately a foundation implementation, not the final placement algorithm. The final production diagnostic must use the planned deterministic/AI/hybrid evaluation strategy, confidence handling, golden datasets, speaking evaluation and placement quality gates.

## Next

Journey initialization will consume the confirmed placement, learner audience, goals and diagnostic skill profile.
