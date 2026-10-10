create table if not exists speaking_attempts (
  id uuid primary key default gen_random_uuid(),
  activity_attempt_id uuid not null references activity_attempts(id) on delete cascade,
  processing_status text not null default 'READY'
    check (processing_status in ('READY','CAPTURING','UPLOADING','PROCESSING','COMPLETED','FAILED','CANCELLED')),
  audio_storage_key text,
  audio_retained boolean not null default false,
  duration_ms integer,
  transcript text,
  transcript_confidence numeric(5,4),
  speech_result jsonb,
  failure_code text,
  failure_retryable boolean,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_speaking_attempts_activity_attempt
  on speaking_attempts(activity_attempt_id);

create index if not exists idx_speaking_attempts_status
  on speaking_attempts(processing_status);
