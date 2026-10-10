# M3 — Universal Activity Runtime

## Purpose

The Activity Runtime is the common execution layer for Web and Android. Individual activity types provide their own renderer/evaluator configuration; the client does not create a separate workflow for every activity.

## Universal contract

Each activity carries:

- identity/version
- activity type
- objective
- skills
- multidimensional difficulty
- instructions
- input contract
- interaction contract
- evaluation mode/configuration
- completion rules
- evidence mapping
- retry rules
- accessibility metadata
- offline capability
- analytics event names

## Runtime state machine

```text
CREATED
  ↓
READY
  ↓
STARTED
  ↓
IN_PROGRESS
  ↓
SUBMITTED
  ↓
EVALUATING
  ↓
EVALUATED
  ↓
FEEDBACK_SHOWN
  ↓
COMPLETED
```

Retry creates a new attempt while preserving the activity objective and learning history.

## MVP activity types

1. `VERB_INTRODUCTION`
2. `SENTENCE_BUILDER`
3. `SPEAKING`

## API

```text
GET   /api/v1/activities/{activityId}
POST  /api/v1/activities/{activityId}/start

PATCH /api/v1/attempts/{attemptId}
GET   /api/v1/attempts/{attemptId}
POST  /api/v1/attempts/{attemptId}/submit
POST  /api/v1/attempts/{attemptId}/retry
```

## Reliability rules

- Activity ownership is server checked.
- Attempt ownership is server checked through the learner's session.
- Existing active attempts are reused when starting an activity.
- Attempt input can be autosaved.
- Submission is separated from evaluation.
- Technical evaluation failure must not become learner failure.
- Retry creates a distinct attempt for auditability.
- Published activity versions are immutable.

## Next

Implement the first concrete activity renderers/evaluators:

**Verb Introduction → Sentence Builder → Evaluation → Feedback → Retry.**
