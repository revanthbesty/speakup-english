create table if not exists auth_sessions (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references users(id) on delete cascade,
  device_id uuid,
  token_hash text not null unique,
  expires_at timestamptz not null,
  revoked_at timestamptz,
  created_at timestamptz not null default now()
);

create index if not exists idx_auth_sessions_user on auth_sessions(user_id);
create index if not exists idx_auth_sessions_active on auth_sessions(token_hash) where revoked_at is null;

create table if not exists learner_preferences (
  learner_id uuid primary key references learner_profiles(id) on delete cascade,
  language text not null default 'en',
  timezone text not null default 'UTC',
  reminder_enabled boolean not null default false,
  updated_at timestamptz not null default now()
);

create table if not exists onboarding_states (
  learner_id uuid primary key references learner_profiles(id) on delete cascade,
  state text not null default 'NOT_STARTED',
  self_assessment jsonb not null default '{}'::jsonb,
  daily_goal_minutes integer,
  updated_at timestamptz not null default now()
);

create table if not exists consent_records (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  consent_type text not null,
  version text not null,
  granted boolean not null,
  recorded_at timestamptz not null default now()
);

alter table learner_profiles
  add column if not exists audience text,
  add column if not exists proficiency text;

create index if not exists idx_learner_profiles_audience on learner_profiles(audience);
