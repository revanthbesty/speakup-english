create table if not exists learning_evidence (
  id uuid primary key default gen_random_uuid(),
  attempt_id uuid not null references activity_attempts(id) on delete restrict,
  evaluation_id uuid not null references evaluations(id) on delete restrict,
  skill text not null,
  provenance text not null
    check (provenance in ('DETERMINISTIC','AI','SPEECH','HUMAN','SYSTEM_DERIVED')),
  evidence_strength numeric(5,4) not null check (evidence_strength >= 0 and evidence_strength <= 1),
  confidence numeric(5,4) not null check (confidence >= 0 and confidence <= 1),
  independence_factor numeric(5,4) not null default 1,
  difficulty_factor numeric(5,4) not null default 1,
  context_factor numeric(5,4) not null default 1,
  evidence jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now(),
  unique(evaluation_id,skill)
);

create table if not exists skill_mastery (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  skill text not null,
  mastery_score numeric(6,4) not null default 0
    check (mastery_score >= 0 and mastery_score <= 1),
  confidence numeric(5,4) not null default 0
    check (confidence >= 0 and confidence <= 1),
  evidence_count integer not null default 0,
  trend text not null default 'STABLE'
    check (trend in ('IMPROVING','STABLE','DECLINING','INSUFFICIENT_DATA')),
  last_evidence_at timestamptz,
  algorithm_version text not null default 'mvp-mastery-v1',
  updated_at timestamptz not null default now(),
  unique(learner_id,skill)
);

create table if not exists mastery_events (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  skill text not null,
  evidence_id uuid references learning_evidence(id) on delete restrict,
  previous_score numeric(6,4),
  new_score numeric(6,4),
  previous_confidence numeric(5,4),
  new_confidence numeric(5,4),
  algorithm_version text not null,
  created_at timestamptz not null default now()
);

create index if not exists idx_learning_evidence_attempt
  on learning_evidence(attempt_id);

create index if not exists idx_learning_evidence_learner_skill
  on learning_evidence(skill,created_at);

create index if not exists idx_skill_mastery_learner
  on skill_mastery(learner_id,skill);
