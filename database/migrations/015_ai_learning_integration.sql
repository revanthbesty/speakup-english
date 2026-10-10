CREATE TABLE IF NOT EXISTS ai_learning_interactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  learner_id UUID NULL,
  use_case TEXT NOT NULL,
  capability TEXT NOT NULL,
  schema_version TEXT NOT NULL,
  provider TEXT NOT NULL,
  model TEXT NOT NULL,
  status TEXT NOT NULL,
  safety_status TEXT NOT NULL,
  fallback_used BOOLEAN NOT NULL DEFAULT FALSE,
  latency_ms INTEGER NOT NULL DEFAULT 0,
  input_tokens INTEGER NOT NULL DEFAULT 0,
  output_tokens INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_ai_learning_use_case_created ON ai_learning_interactions(use_case,created_at DESC);
