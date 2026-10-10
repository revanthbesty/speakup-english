CREATE TABLE IF NOT EXISTS ai_providers (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  provider_key TEXT UNIQUE NOT NULL,
  display_name TEXT NOT NULL,
  endpoint TEXT,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  timeout_ms INTEGER NOT NULL DEFAULT 8000,
  failure_threshold INTEGER NOT NULL DEFAULT 3,
  cooldown_ms INTEGER NOT NULL DEFAULT 30000,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ai_models (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  provider_id UUID NOT NULL REFERENCES ai_providers(id) ON DELETE CASCADE,
  model_key TEXT NOT NULL,
  display_name TEXT NOT NULL,
  capabilities JSONB NOT NULL DEFAULT '{}'::jsonb,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  priority INTEGER NOT NULL DEFAULT 100,
  cost_input_per_1k NUMERIC(12,6) NOT NULL DEFAULT 0,
  cost_output_per_1k NUMERIC(12,6) NOT NULL DEFAULT 0,
  config JSONB NOT NULL DEFAULT '{}'::jsonb,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE(provider_id, model_key)
);

CREATE TABLE IF NOT EXISTS ai_interactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  learner_id UUID,
  purpose TEXT NOT NULL,
  provider_key TEXT NOT NULL,
  model_key TEXT NOT NULL,
  status TEXT NOT NULL,
  latency_ms INTEGER,
  input_tokens INTEGER,
  output_tokens INTEGER,
  cost_estimate NUMERIC(14,6),
  safety_status TEXT NOT NULL DEFAULT 'NOT_EVALUATED',
  fallback_used BOOLEAN NOT NULL DEFAULT FALSE,
  schema_version TEXT NOT NULL,
  error_code TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_ai_interactions_created ON ai_interactions(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_ai_interactions_learner ON ai_interactions(learner_id, created_at DESC);
