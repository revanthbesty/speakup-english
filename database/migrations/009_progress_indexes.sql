-- M3 Progress/Home: derived learner state and faster evidence/progress reads.
create index if not exists idx_learning_evidence_attempt on learning_evidence(attempt_id);
create index if not exists idx_activity_attempts_activity_session on activity_attempts(activity_id,session_id,created_at desc);
create index if not exists idx_learning_sessions_learner_created on learning_sessions(learner_id,created_at desc);
create index if not exists idx_skill_mastery_learner_score on skill_mastery(learner_id,mastery_score asc);
