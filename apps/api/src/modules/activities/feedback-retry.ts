import { pool } from "../../db.js";

type EvaluationRow = {
  id: string;
  attempt_id: string;
  outcome: "CORRECT" | "PARTIAL" | "INCORRECT";
  score: number;
  confidence: number;
  result: {
    headline?: string;
    explanation?: string;
    retryHint?: string;
  };
};

export function buildRetryPlan(evaluation: EvaluationRow) {
  if (evaluation.outcome === "CORRECT") {
    return {
      action: "CONTINUE",
      supportLevel: "NONE",
      message: "Nice work. You're ready for the next challenge.",
      retryAllowed: false
    };
  }

  if (evaluation.outcome === "PARTIAL") {
    return {
      action: "RETRY",
      supportLevel: "MEDIUM",
      message: evaluation.result.retryHint ?? "Try the sentence again with the hint.",
      retryAllowed: true
    };
  }

  return {
    action: "RETRY",
    supportLevel: "HIGH",
    message: evaluation.result.retryHint ?? "Let's try it again step by step.",
    retryAllowed: true
  };
}

export async function getFeedback(learnerId: string, attemptId: string) {
  const result = await pool.query(
    `select e.id,e.attempt_id,e.outcome,e.score,e.confidence,e.result
       from evaluations e
       join activity_attempts aa on aa.id=e.attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where e.attempt_id=$1 and ls.learner_id=$2
      order by e.created_at desc limit 1`,
    [attemptId, learnerId]
  );

  if (!result.rowCount) throw new Error("EVALUATION_NOT_FOUND");

  const evaluation = result.rows[0] as EvaluationRow;
  const plan = buildRetryPlan(evaluation);

  await pool.query(
    `update activity_attempts
        set status=case when $2=true then 'FEEDBACK_SHOWN' else 'COMPLETED' end
      where id=$1`,
    [attemptId, plan.retryAllowed]
  );

  return {
    evaluation,
    feedback: {
      headline: evaluation.result?.headline ?? "Good effort.",
      explanation: evaluation.result?.explanation ?? "Keep practicing.",
      score: evaluation.score,
      confidence: evaluation.confidence
    },
    retry: plan
  };
}

export async function createRetry(learnerId: string, attemptId: string) {
  const feedback = await getFeedback(learnerId, attemptId);

  if (!feedback.retry.retryAllowed) {
    throw new Error("RETRY_NOT_ALLOWED");
  }

  const source = await pool.query(
    `select aa.session_id,aa.activity_id
       from activity_attempts aa
       join learning_sessions ls on ls.id=aa.session_id
      where aa.id=$1 and ls.learner_id=$2`,
    [attemptId, learnerId]
  );

  if (!source.rowCount) throw new Error("ATTEMPT_NOT_FOUND");

  const existing = await pool.query(
    `select id,status
       from activity_attempts
      where session_id=$1 and activity_id=$2
        and status in ('STARTED','IN_PROGRESS')
      order by created_at desc limit 1`,
    [source.rows[0].session_id, source.rows[0].activity_id]
  );

  if (existing.rowCount) {
    return {
      attempt: existing.rows[0],
      retry: feedback.retry,
      reused: true
    };
  }

  const created = await pool.query(
    `insert into activity_attempts
      (session_id,activity_id,status,created_at)
     values($1,$2,'STARTED',now())
     returning id,status,created_at`,
    [source.rows[0].session_id, source.rows[0].activity_id]
  );

  return {
    attempt: created.rows[0],
    retry: feedback.retry,
    reused: false
  };
}
