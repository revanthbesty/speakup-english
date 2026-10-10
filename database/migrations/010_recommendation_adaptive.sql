create table if not exists difficulty_states (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  skill text not null,
  difficulty numeric(4,2) not null default 0.50 check (difficulty >= 0 and difficulty <= 1),
  confidence numeric(5,4) not null default 0.20 check (confidence >= 0 and confidence <= 1),
  consecutive_successes integer not null default 0,
  consecutive_failures integer not null default 0,
  last_outcome text,
  algorithm_version text not null default 'mvp-adaptive-v1',
  updated_at timestamptz not null default now(),
  unique(learner_id, skill)
);
create index if not exists idx_difficulty_states_learner on difficulty_states(learner_id,skill);

create table if not exists recommendations (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  type text not null,
  activity_id uuid references activities(id),
  skill text,
  reason text not null,
  priority numeric(6,3) not null default 0,
  difficulty numeric(4,2),
  source text not null default 'RULE_ENGINE',
  expires_at timestamptz,
  created_at timestamptz not null default now()
);
create index if not exists idx_recommendations_learner_created on recommendations(learner_id,created_at desc);
