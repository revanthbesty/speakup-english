# M3 — Gamification: XP, Level & Reward

## Goal
Convert meaningful learning evidence into bounded XP, deterministic levels, and unlockable rewards without conflating completion, evidence, mastery, XP, or proficiency.

## XP policy
- Correct meaningful evidence: up to 20 XP.
- Partial meaningful evidence: up to 12 XP.
- Incorrect attempts can receive a small learning signal (4 XP) only when they produce valid evidence; this is not a completion reward.
- Retry evidence receives a 50% XP multiplier to prevent farming.
- Neutral/low-confidence/technical outcomes: 0 XP.
- Confidence and evidence strength reduce XP when signal quality is weaker.
- XP is awarded from immutable `learning_evidence`, not from button clicks or technical completion.

## Anti-farming
- XP event key is `EVIDENCE:{evidenceId}`.
- Unique `(learner_id,event_key)` prevents duplicate awards.
- Retry attempts are reduced rather than treated as fresh full-value awards.
- Technical failures never create learning evidence and therefore cannot create XP.
- Gamification does not directly mutate mastery or proficiency.

## Levels
MVP deterministic thresholds:
- Level 1: 0–49 XP
- Level 2: 50–119 XP
- Level 3: 120–249 XP
- Level 4: 250–449 XP
- Level 5: 450–699 XP
- Level 6+: 700+ with 300 XP per subsequent level.

## Rewards
Seeded rewards:
- First Step — level 1 / 1 XP
- Speaking Start — level 2 / 50 XP
- Consistent Learner — level 3 / 120 XP

Rewards are idempotently unlocked from the learner's resulting XP/level.

## APIs
- `GET /api/v1/gamification`
- `GET /api/v1/gamification/xp`
- `POST /api/v1/evidence` now returns evidence, mastery, and gamification results.

## Important integration fix
Deterministic sentence evaluations now persist their evidence array in the evaluation result so the normal sentence-builder flow can produce learning evidence and XP.

## Production follow-up
This is an MVP policy. Production should add configurable XP policy versions, daily/lesson anti-abuse limits, streak rules, mission rewards, achievement definitions, entitlement-aware reward catalogs, and analytics/event quality gates.
