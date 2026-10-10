# M3 — MVP Activities

## Verb Introduction

The first verb is `eat`.

The activity teaches meaning through active interaction rather than a passive dictionary card.

Example language:

- I eat rice.
- I eat breakfast.
- I eat rice for breakfast.

## Sentence Builder

Canonical target:

> I eat rice for breakfast.

Sentence construction follows the product's core progression:

```text
WHO → ACTION → WHAT → WHEN
```

The evaluator is deterministic for the MVP.

## Evaluation outcomes

### CORRECT
The learner matches the canonical target.

### PARTIAL
The learner demonstrates meaningful progress but has an ordering/selection issue.

### INCORRECT
The response does not yet demonstrate the target construction.

Every non-correct result gives a short, actionable retry hint. The system avoids presenting the learner as a failure.

## API

```text
GET  /api/v1/mvp/activities/VERB_INTRODUCTION
GET  /api/v1/mvp/activities/SENTENCE_BUILDER

POST /api/v1/mvp/activities/SENTENCE_BUILDER
```

When an `attemptId` is supplied, the deterministic result is persisted as an evaluation and the attempt becomes `EVALUATED`.

## Design boundary

This evaluator is intentionally deterministic for the first MVP slice. It does not claim to replace the later AI/hybrid evaluation engine for open-ended language.

## Next

Connect evaluation to the formal Feedback/Retry pipeline, then implement the speaking activity and AI Gateway path.
