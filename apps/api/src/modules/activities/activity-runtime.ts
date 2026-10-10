import { pool } from "../../db.js";

const transitions: Record<string, string[]> = {
  CREATED: ["READY"],
  READY: ["STARTED"],
  STARTED: ["IN_PROGRESS","SUBMITTED"],
  IN_PROGRESS: ["IN_PROGRESS","SUBMITTED"],
  SUBMITTED: ["EVALUATING"],
  EVALUATING: ["EVALUATED"],
  EVALUATED: ["FEEDBACK_SHOWN","COMPLETED"],
  FEEDBACK_SHOWN: ["COMPLETED","IN_PROGRESS"],
  COMPLETED: []
};

export function canTransition(from: string, to: string) {
  return transitions[from]?.includes(to) ?? false;
}

async function ownsActivity(learnerId: string, activityId: string) {
  const result = await pool.query(
    `select 1
       from lesson_progress lp
       join learning_paths p on p.id=lp.learning_path_id
       join activities a on a.lesson_id=lp.lesson_id
      where p.learner_id=$1 and a.id=$2 and p.status='ACTIVE'`,
    [learnerId, activityId]
  );
  if (!result.rowCount) throw new Error("ACTIVITY_NOT_AVAILABLE");
}

export async function getActivity(learnerId: string, activityId: string) {
  await ownsActivity(learnerId, activityId);
  const result = await pool.query(
    `select a.id,a.code,a.type,a.objective,av.version,av.payload
       from activities a
       join lateral (
         select * from activity_versions x
          where x.activity_id=a.id and x.published_at is not null
          order by x.version desc limit 1
       ) av on true
      where a.id=$1 and a.status='PUBLISHED'`,
    [activityId]
  );
  if (!result.rowCount) throw new Error("ACTIVITY_NOT_FOUND");
  return result.rows[0];
}

export async function startActivity(learnerId: string, activityId: string, sessionId: string) {
  await ownsActivity(learnerId, activityId);
  const activity = await getActivity(learnerId, activityId);

  const session = await pool.query(
    `select 1 from learning_sessions where id=$1 and learner_id=$2`,
    [sessionId, learnerId]
  );
  if (!session.rowCount) throw new Error("SESSION_NOT_FOUND");

  const existing = await pool.query(
    `select id,status,input,started_at
       from activity_attempts
      where session_id=$1 and activity_id=$2
        and status in ('CREATED','STARTED','IN_PROGRESS','SUBMITTED','EVALUATING','EVALUATED','FEEDBACK_SHOWN')
      order by created_at desc limit 1`,
    [sessionId,activityId]
  );
  if (existing.rowCount) return { activity, attempt: existing.rows[0] };

  const result = await pool.query(
    `insert into activity_attempts(session_id,activity_id,status,started_at)
     values($1,$2,'STARTED',now())
     returning id,status,input,started_at`,
    [sessionId,activityId]
  );
  return { activity, attempt: result.rows[0] };
}

export async function updateAttempt(
  learnerId: string,
  attemptId: string,
  input: Record<string, unknown>,
  progress = 0
) {
  const attempt = await getAttempt(learnerId, attemptId);
  if (!["STARTED","IN_PROGRESS"].includes(attempt.status)) throw new Error("ATTEMPT_NOT_EDITABLE");

  const nextState = progress >= 1 ? "SUBMITTED" : "IN_PROGRESS";
  await pool.query(
    `update activity_attempts
        set input=$2,status=$3,submitted_at=case when $3='SUBMITTED' then now() else submitted_at end
      where id=$1`,
    [attemptId,input,nextState]
  );
  return getAttempt(learnerId,attemptId);
}

export async function submitAttempt(learnerId: string, attemptId: string) {
  const attempt = await getAttempt(learnerId,attemptId);
  if (!["STARTED","IN_PROGRESS","SUBMITTED"].includes(attempt.status))
    throw new Error("ATTEMPT_NOT_SUBMITTABLE");

  if (attempt.status !== "SUBMITTED") {
    await pool.query(
      `update activity_attempts set status='SUBMITTED',submitted_at=coalesce(submitted_at,now()) where id=$1`,
      [attemptId]
    );
  }

  return getAttempt(learnerId,attemptId);
}

export async function retryAttempt(learnerId: string, attemptId: string) {
  const attempt = await getAttempt(learnerId,attemptId);
  if (!["EVALUATED","FEEDBACK_SHOWN"].includes(attempt.status))
    throw new Error("ATTEMPT_NOT_RETRYABLE");

  const result = await pool.query(
    `insert into activity_attempts(session_id,activity_id,status,created_at)
     values($1,$2,'STARTED',now())
     returning id,status,input,started_at`,
    [attempt.session_id,attempt.activity_id]
  );
  return result.rows[0];
}

export async function getAttempt(learnerId: string, attemptId: string) {
  const result = await pool.query(
    `select aa.id,aa.session_id,aa.activity_id,aa.status,aa.input,aa.started_at,aa.submitted_at
       from activity_attempts aa
       join learning_sessions ls on ls.id=aa.session_id
      where aa.id=$1 and ls.learner_id=$2`,
    [attemptId,learnerId]
  );
  if (!result.rowCount) throw new Error("ATTEMPT_NOT_FOUND");
  return result.rows[0];
}
