create extension if not exists pgcrypto;

create table if not exists users (
  id uuid primary key default gen_random_uuid(),
  email text not null unique,
  password_hash text,
  status text not null default 'PENDING_VERIFICATION'
    check (status in ('ACTIVE','PENDING_VERIFICATION','SUSPENDED','DEACTIVATED')),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists learner_profiles (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null unique references users(id) on delete cascade,
  display_name text,
  audience text,
  proficiency text check (proficiency in ('P0','P1','P2','P3')),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists learning_goals (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  goal_code text not null,
  priority integer not null default 1,
  created_at timestamptz not null default now()
);

create table if not exists worlds (
  id uuid primary key default gen_random_uuid(),
  code text not null unique,
  name text not null,
  status text not null default 'PUBLISHED'
);

create table if not exists units (
  id uuid primary key default gen_random_uuid(),
  world_id uuid not null references worlds(id),
  code text not null unique,
  name text not null
);

create table if not exists lessons (
  id uuid primary key default gen_random_uuid(),
  unit_id uuid not null references units(id),
  code text not null unique,
  name text not null,
  objective text not null,
  version integer not null default 1
);

create table if not exists activities (
  id uuid primary key default gen_random_uuid(),
  lesson_id uuid not null references lessons(id),
  code text not null unique,
  type text not null,
  objective text not null,
  status text not null default 'PUBLISHED'
);

create table if not exists activity_versions (
  id uuid primary key default gen_random_uuid(),
  activity_id uuid not null references activities(id),
  version integer not null,
  payload jsonb not null,
  published_at timestamptz,
  unique(activity_id, version)
);

create table if not exists learning_sessions (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id),
  lesson_id uuid references lessons(id),
  status text not null default 'ACTIVE',
  started_at timestamptz not null default now(),
  completed_at timestamptz
);

create table if not exists activity_attempts (
  id uuid primary key default gen_random_uuid(),
  session_id uuid not null references learning_sessions(id),
  activity_id uuid not null references activities(id),
  status text not null default 'CREATED',
  input jsonb not null default '{}'::jsonb,
  started_at timestamptz,
  submitted_at timestamptz,
  created_at timestamptz not null default now()
);

create table if not exists evaluations (
  id uuid primary key default gen_random_uuid(),
  attempt_id uuid not null unique references activity_attempts(id),
  evaluation_mode text not null,
  status text not null,
  outcome text,
  score numeric(5,4),
  confidence numeric(5,4),
  result jsonb not null default '{}'::jsonb,
  evaluator_version text,
  created_at timestamptz not null default now()
);

create table if not exists learning_evidence (
  id uuid primary key default gen_random_uuid(),
  attempt_id uuid not null references activity_attempts(id),
  skill text not null,
  provenance text not null,
  confidence numeric(5,4) not null,
  strength numeric(5,4) not null,
  context jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now()
);

create table if not exists skill_mastery (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id),
  skill text not null,
  mastery numeric(5,4) not null default 0,
  confidence numeric(5,4) not null default 0,
  evidence_count integer not null default 0,
  trend text not null default 'STABLE',
  updated_at timestamptz not null default now(),
  unique(learner_id, skill)
);

create table if not exists xp_events (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id),
  source_type text not null,
  source_id uuid not null,
  xp integer not null,
  policy_version text not null,
  created_at timestamptz not null default now(),
  unique(source_type, source_id)
);

create table if not exists analytics_events (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid references learner_profiles(id),
  event_name text not null,
  schema_version integer not null default 1,
  payload jsonb not null default '{}'::jsonb,
  occurred_at timestamptz not null
);

create index if not exists idx_activity_attempts_session on activity_attempts(session_id);
create index if not exists idx_evidence_learner_skill on learning_evidence(skill);
create index if not exists idx_analytics_event_name_time on analytics_events(event_name, occurred_at);
