-- M3 Gamification: meaningful learning -> XP -> level -> reward.
-- Extends the foundation xp_events table without replacing prior data.
alter table xp_events
  add column if not exists event_key text,
  add column if not exists reason text,
  add column if not exists metadata jsonb not null default '{}'::jsonb;

update xp_events
   set event_key=coalesce(event_key, source_type || ':' || source_id::text),
       reason=coalesce(reason,'legacy')
 where event_key is null or reason is null;

alter table xp_events
  alter column event_key set not null,
  alter column reason set not null;

create unique index if not exists uq_xp_events_learner_event_key
  on xp_events(learner_id,event_key);
create index if not exists idx_xp_events_learner_time
  on xp_events(learner_id,created_at desc);

create table if not exists learner_gamification (
  learner_id uuid primary key references learner_profiles(id) on delete cascade,
  total_xp bigint not null default 0 check (total_xp >= 0),
  level integer not null default 1 check (level >= 1),
  updated_at timestamptz not null default now()
);

create table if not exists reward_definitions (
  id uuid primary key default gen_random_uuid(),
  code text not null unique,
  name text not null,
  description text not null,
  unlock_level integer not null default 1 check (unlock_level >= 1),
  xp_threshold bigint not null default 0 check (xp_threshold >= 0),
  status text not null default 'ACTIVE'
    check (status in ('ACTIVE','INACTIVE')),
  created_at timestamptz not null default now()
);

create table if not exists learner_rewards (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  reward_id uuid not null references reward_definitions(id) on delete restrict,
  unlocked_at timestamptz not null default now(),
  unique(learner_id,reward_id)
);

insert into reward_definitions(code,name,description,unlock_level,xp_threshold)
values
 ('FIRST_STEP','First Step','Complete your first meaningful learning action.',1,1),
 ('SPEAKING_START','Speaking Start','Build confidence by earning XP from meaningful speaking practice.',2,50),
 ('CONSISTENT_LEARNER','Consistent Learner','Keep progressing through meaningful learning evidence.',3,120)
on conflict(code) do nothing;

create index if not exists idx_learner_rewards_learner
  on learner_rewards(learner_id,unlocked_at desc);
