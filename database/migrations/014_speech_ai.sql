CREATE TABLE IF NOT EXISTS speech_interactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(), learner_id UUID, capability TEXT NOT NULL,
  provider_key TEXT NOT NULL, model_key TEXT NOT NULL, status TEXT NOT NULL,
  latency_ms INTEGER, confidence NUMERIC(6,4), duration_ms INTEGER,
  raw_audio_retained BOOLEAN NOT NULL DEFAULT FALSE, error_code TEXT, created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_speech_interactions_learner ON speech_interactions(learner_id, created_at DESC);
