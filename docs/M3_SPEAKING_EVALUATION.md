# M3 — Speaking Evaluation

## Evaluation dimensions

The MVP speaking evaluator considers:

1. Sentence construction
2. Grammar
3. Vocabulary
4. Completeness
5. Contextual relevance

The dimensions remain separate so later providers can improve individual capabilities without changing the learning contract.

## Score

The MVP aggregate score is weighted:

```text
Sentence construction  30%
Grammar                20%
Vocabulary             15%
Completeness           15%
Context relevance      20%
```

The score is an internal evaluation value. It is not proficiency and does not directly change mastery or XP.

## Feedback

The evaluator returns:

- outcome
- confidence
- dimension scores
- concise feedback
- retry priority
- retry hint
- evidence mapping

## Low-confidence rule

If speech recognition confidence is below the configured MVP threshold, the learner receives a **neutral technical/retry outcome** rather than being marked incorrect.

Example:

> We weren't confident enough in the speech recognition to judge your English fairly.

This follows the product rule:

**Technical failure ≠ learner failure.**

## Provenance

The evaluation stores:

```text
evaluation_mode = SPEECH
evaluator_version = mvp-speaking-evaluator-v1
```

This makes later evaluator/model changes traceable.

## API

```text
POST /api/v1/speaking/attempts/{id}/evaluate
```

## Current boundary

The evaluator is still a deterministic MVP evaluator operating on the transcript. It is not yet a production LLM evaluator, pronunciation model, or full fluency engine.

Those capabilities will enter through the AI Gateway and Speech Engine.

## Next

Connect this result to the formal Evidence Engine, then implement XP/reward and progress updates.
