create table if not exists diagnostics (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  version integer not null default 1,
  status text not null default 'CREATED'
    check (status in ('CREATED','IN_PROGRESS','COMPLETED','CANCELLED')),
  recommended_proficiency text,
  recommendation_confidence numeric(5,4),
  started_at timestamptz,
  completed_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists diagnostic_sections (
  id uuid primary key default gen_random_uuid(),
  diagnostic_id uuid not null references diagnostics(id) on delete cascade,
  section_code text not null,
  sequence_no integer not null,
  version integer not null default 1,
  status text not null default 'NOT_STARTED',
  unique(diagnostic_id, section_code)
);

create table if not exists diagnostic_attempts (
  id uuid primary key default gen_random_uuid(),
  diagnostic_section_id uuid not null references diagnostic_sections(id) on delete cascade,
  attempt_no integer not null default 1,
  input jsonb not null default '{}'::jsonb,
  outcome text,
  score numeric(5,4),
  confidence numeric(5,4),
  evaluation jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now(),
  unique(diagnostic_section_id, attempt_no)
);

create table if not exists diagnostic_results (
  id uuid primary key default gen_random_uuid(),
  diagnostic_id uuid not null unique references diagnostics(id) on delete cascade,
  section_results jsonb not null default '[]'::jsonb,
  skill_profile jsonb not null default '{}'::jsonb,
  recommended_proficiency text,
  confidence numeric(5,4),
  rationale jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now()
);

create table if not exists placement_decisions (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  diagnostic_id uuid not null references diagnostics(id),
  recommended_proficiency text not null,
  recommended_starting_point jsonb not null,
  learner_selected_proficiency text,
  learner_selected_starting_point jsonb,
  decision_type text not null default 'PENDING'
    check (decision_type in ('PENDING','ACCEPTED','OVERRIDDEN')),
  decided_at timestamptz,
  created_at timestamptz not null default now()
);

create index if not exists idx_diagnostics_learner_status on diagnostics(learner_id,status);
create index if not exists idx_diagnostic_attempts_section on diagnostic_attempts(diagnostic_section_id);
