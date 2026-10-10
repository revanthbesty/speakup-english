-- Keeps the MVP evaluator provenance explicit without changing the generic evaluation contract.
create index if not exists idx_evaluations_attempt_mode
  on evaluations(attempt_id,evaluation_mode);
