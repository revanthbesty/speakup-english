create table if not exists learning_paths (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  placement_proficiency text not null,
  audience text,
  goal_snapshot jsonb not null default '[]'::jsonb,
  skill_profile_snapshot jsonb not null default '{}'::jsonb,
  status text not null default 'ACTIVE'
    check (status in ('DRAFT','ACTIVE','PAUSED','COMPLETED','ARCHIVED')),
  version integer not null default 1,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists lesson_progress (
  id uuid primary key default gen_random_uuid(),
  learner_id uuid not null references learner_profiles(id) on delete cascade,
  lesson_id uuid not null references lessons(id),
  learning_path_id uuid not null references learning_paths(id),
  status text not null default 'NOT_STARTED'
    check (status in ('NOT_STARTED','IN_PROGRESS','COMPLETED')),
  current_activity_id uuid references activities(id),
  completed_activities integer not null default 0,
  started_at timestamptz,
  completed_at timestamptz,
  updated_at timestamptz not null default now(),
  unique(learner_id, lesson_id, learning_path_id)
);

create index if not exists idx_learning_paths_learner_status
  on learning_paths(learner_id,status);

create index if not exists idx_lesson_progress_learner
  on lesson_progress(learner_id,status);
