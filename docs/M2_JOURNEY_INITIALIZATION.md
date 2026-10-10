# M2 — Journey Initialization

## Purpose

Turn confirmed placement into an executable learning journey without replacing the curated curriculum with AI-generated curriculum.

## Inputs

- confirmed learner proficiency
- learner audience
- learning goals
- diagnostic skill profile

These are snapshotted into the learning path so later changes remain auditable.

## MVP path

```text
Everyday English
  └── Food & Meals
       └── Breakfast
            ├── Verb Introduction — eat
            ├── Sentence Builder — I eat rice for breakfast.
            └── Speaking — What do you eat for breakfast?
```

## Rules

- Server is authoritative.
- Published content is read-only.
- Learning path state is separate from content.
- Journey initialization is idempotent.
- Existing active journey is returned instead of creating duplicates.
- Activity ordering is data/content driven with a deterministic MVP fallback.
- Placement is required before journey initialization.

## API

```text
POST /api/v1/journey/initialize
GET  /api/v1/journey
POST /api/v1/journey/lessons/{lessonId}/start
```

## Next

The project now crosses into **M3 — Core Learning Loop**:
Activity Runtime → Verb → Sentence Builder → Evaluation → Feedback → Retry → Speaking → Evidence → Mastery → XP.
