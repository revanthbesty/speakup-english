# M3 — Learning Evidence & Mastery

## Purpose

Turn evaluated learner behavior into durable learning intelligence without allowing a single activity to arbitrarily change proficiency.

## Evidence pipeline

```text
Attempt
  ↓
Evaluation
  ↓
Validated result
  ↓
Learning Evidence
  ↓
Skill attribution
  ↓
Confidence + evidence factors
  ↓
Skill Mastery
```

## Evidence

Each evidence record stores:

- attempt
- evaluation
- skill
- provenance
- evidence strength
- confidence
- independence factor
- difficulty factor
- context factor
- evidence details

Evidence is immutable after creation.

## Provenance

MVP supports:

```text
DETERMINISTIC
AI
SPEECH
HUMAN
SYSTEM_DERIVED
```

The speaking evaluator therefore produces `SPEECH` evidence.

## Mastery

Mastery is:

- skill-specific
- continuous
- confidence-aware
- conservative
- auditable
- separate from proficiency

The MVP applies a deliberately small incremental update so one attempt cannot radically change a learner's mastery.

## Important separation

These are different concepts:

```text
Activity Completion
        ≠
Learning Evidence
        ≠
Mastery
        ≠
XP
        ≠
Proficiency
```

A completed activity does not automatically mean mastery.

## Auditability

Every mastery update creates a `mastery_events` record containing previous and new values plus the algorithm version.

## API

```text
POST /api/v1/evidence
GET  /api/v1/mastery
```

The evidence endpoint creates evidence and immediately applies the resulting mastery update for the MVP.

## Next

Connect meaningful learning evidence to the Gamification Engine so successful learning can produce XP without allowing retry farming or technical failures to generate rewards.
