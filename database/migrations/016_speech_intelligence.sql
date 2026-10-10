CREATE TABLE IF NOT EXISTS speech_intelligence_results (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  speaking_attempt_id UUID NOT NULL REFERENCES speaking_attempts(id) ON DELETE CASCADE,
  stt_confidence NUMERIC(6,4) NOT NULL,
  pronunciation_score NUMERIC(6,4),
  pronunciation_confidence NUMERIC(6,4),
  fluency_score NUMERIC(6,4),
  fluency_confidence NUMERIC(6,4),
  words_per_minute NUMERIC(8,2),
  pause_ratio NUMERIC(6,4),
  filler_count INTEGER NOT NULL DEFAULT 0,
  interpretation_status TEXT NOT NULL,
  provider_summary JSONB NOT NULL DEFAULT '{}'::jsonb,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE(speaking_attempt_id)
);
CREATE INDEX IF NOT EXISTS idx_speech_intelligence_attempt ON speech_intelligence_results(speaking_attempt_id);

CREATE TABLE IF NOT EXISTS speaking_learning_metrics (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  learner_id UUID NOT NULL,
  speaking_attempt_id UUID NOT NULL REFERENCES speaking_attempts(id) ON DELETE CASCADE,
  speaking_seconds INTEGER NOT NULL DEFAULT 0,
  sentence_attempted BOOLEAN NOT NULL DEFAULT TRUE,
  sentence_improved BOOLEAN NOT NULL DEFAULT FALSE,
  meaningful BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE(speaking_attempt_id)
);
CREATE INDEX IF NOT EXISTS idx_speaking_metrics_learner ON speaking_learning_metrics(learner_id, created_at DESC);
