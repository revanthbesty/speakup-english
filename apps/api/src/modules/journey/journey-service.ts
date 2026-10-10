import { pool } from "../../db.js";

type PathInput = {
  proficiency: "P0"|"P1"|"P2"|"P3";
  audience: string | null;
  goals: Array<{goal_code:string; priority:number}>;
  skillProfile: Record<string, unknown>;
};

async function getPlacementState(learnerId: string): Promise<PathInput> {
  const learner = await pool.query(
    `select audience, proficiency from learner_profiles where id=$1`,
    [learnerId]
  );
  if (!learner.rowCount) throw new Error("LEARNER_NOT_FOUND");
  if (!learner.rows[0].proficiency) throw new Error("PLACEMENT_NOT_CONFIRMED");

  const goals = await pool.query(
    `select goal_code,priority from learning_goals
      where learner_id=$1 order by priority`,
    [learnerId]
  );

  const diagnostic = await pool.query(
    `select dr.skill_profile
       from diagnostics d
       join diagnostic_results dr on dr.diagnostic_id=d.id
      where d.learner_id=$1 and d.status='COMPLETED'
      order by d.completed_at desc limit 1`,
    [learnerId]
  );

  return {
    proficiency: learner.rows[0].proficiency,
    audience: learner.rows[0].audience,
    goals: goals.rows,
    skillProfile: diagnostic.rows[0]?.skill_profile ?? {}
  };
}

export async function initializeJourney(learnerId: string) {
  const state = await getPlacementState(learnerId);

  const existing = await pool.query(
    `select id from learning_paths
      where learner_id=$1 and status='ACTIVE'
      order by version desc limit 1`,
    [learnerId]
  );
  if (existing.rowCount) return getJourney(learnerId);

  const lesson = await pool.query(
    `select l.id lesson_id, l.name lesson_name, u.id unit_id, u.name unit_name,
            w.id world_id, w.name world_name
       from lessons l
       join units u on u.id=l.unit_id
       join worlds w on w.id=u.world_id
      where w.status='PUBLISHED'
        and l.code='BREAKFAST'
      limit 1`
  );
  if (!lesson.rowCount) throw new Error("MVP_CONTENT_NOT_FOUND");

  const client = await pool.connect();
  try {
    await client.query("begin");

    const pathResult = await client.query(
      `insert into learning_paths
       (learner_id,placement_proficiency,audience,goal_snapshot,skill_profile_snapshot)
       values($1,$2,$3,$4,$5) returning id`,
      [learnerId,state.proficiency,state.audience,state.goals,state.skillProfile]
    );
    const pathId = pathResult.rows[0].id as string;

    await client.query(
      `insert into lesson_progress
       (learner_id,lesson_id,learning_path_id,status)
       values($1,$2,$3,'NOT_STARTED')`,
      [learnerId,lesson.rows[0].lesson_id,pathId]
    );

    await client.query(
      `update onboarding_states set state='ONBOARDING_COMPLETED',updated_at=now()
        where learner_id=$1`,
      [learnerId]
    );

    await client.query("commit");
  } catch(e) {
    await client.query("rollback");
    throw e;
  } finally {
    client.release();
  }

  return getJourney(learnerId);
}

export async function getJourney(learnerId: string) {
  const path = await pool.query(
    `select id,placement_proficiency,audience,goal_snapshot,skill_profile_snapshot,
            status,version,created_at,updated_at
       from learning_paths
      where learner_id=$1 and status='ACTIVE'
      order by version desc limit 1`,
    [learnerId]
  );
  if (!path.rowCount) throw new Error("JOURNEY_NOT_INITIALIZED");

  const lessons = await pool.query(
    `select lp.id progress_id,lp.status progress_status,lp.current_activity_id,
            lp.completed_activities,
            l.id lesson_id,l.code lesson_code,l.name lesson_name,l.objective,
            u.id unit_id,u.code unit_code,u.name unit_name,
            w.id world_id,w.code world_code,w.name world_name
       from lesson_progress lp
       join lessons l on l.id=lp.lesson_id
       join units u on u.id=l.unit_id
       join worlds w on w.id=u.world_id
      where lp.learning_path_id=$1
      order by l.code`,
    [path.rows[0].id]
  );

  const first = lessons.rows[0];
  let activities: unknown[] = [];
  if (first) {
    const activityRows = await pool.query(
      `select a.id,a.code,a.type,a.objective,av.version,av.payload
         from activities a
         join activity_versions av on av.activity_id=a.id
        where a.lesson_id=$1 and a.status='PUBLISHED'
          and av.published_at is not null
        order by
          case a.type
            when 'VERB_INTRODUCTION' then 1
            when 'SENTENCE_BUILDER' then 2
            when 'SPEAKING' then 3
            else 99
          end,
          av.version desc`,
      [first.lesson_id]
    );
    activities = activityRows.rows;
  }

  return {
    path: path.rows[0],
    lessons: lessons.rows,
    nextLesson: first ?? null,
    nextActivities: activities
  };
}

export async function startLesson(learnerId: string, lessonId: string) {
  const result = await pool.query(
    `update lesson_progress
        set status='IN_PROGRESS',started_at=coalesce(started_at,now()),updated_at=now()
      where learner_id=$1 and lesson_id=$2 and status='NOT_STARTED'
      returning id,lesson_id,status`,
    [learnerId,lessonId]
  );
  if (!result.rowCount) {
    const existing = await pool.query(
      `select id,lesson_id,status from lesson_progress
        where learner_id=$1 and lesson_id=$2`,
      [learnerId,lessonId]
    );
    if (!existing.rowCount) throw new Error("LESSON_NOT_FOUND");
    return existing.rows[0];
  }
  return result.rows[0];
}
