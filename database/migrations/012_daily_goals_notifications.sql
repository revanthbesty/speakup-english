-- M3 Daily Goals + Notifications: meaningful learning drives daily progress and streak eligibility.
alter table learner_preferences
  add column if not exists reminder_time_local time,
  add column if not exists quiet_hours_enabled boolean not null default false,
  add column if not exists notifications_enabled boolean not null default true;

create table if not exists daily_learning_progress (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  local_date date not null,
  target_minutes integer not null check (target_minutes >= 0),
  meaningful_minutes numeric(8,2) not null default 0 check (meaningful_minutes >= 0),
  meaningful_sessions integer not null default 0 check (meaningful_sessions >= 0),
  goal_met boolean not null default false,
  calculated_at timestamptz not null default now(),
  unique(learner_id, local_date)
);

create index if not exists idx_daily_learning_progress_learner_date
  on daily_learning_progress(learner_id, local_date desc);

create table if not exists notification_events (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  notification_type text not null,
  local_date date not null,
  status text not null default 'ELIGIBLE'
    check (status in ('ELIGIBLE','SENT','DISMISSED','FAILED')),
  payload jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now(),
  sent_at timestamptz,
  unique(learner_id, notification_type, local_date)
);

create index if not exists idx_notification_events_learner_date
  on notification_events(learner_id, local_date desc);
