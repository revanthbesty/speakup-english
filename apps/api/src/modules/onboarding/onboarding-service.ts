import { pool } from "../../db.js";

const audiences = new Set(["KIDS","TEENS","STUDENTS","COLLEGE","JOB_SEEKERS","PROFESSIONALS","ADULTS","SENIORS"]);
const goals = new Set(["GENERAL_ENGLISH","CONVERSATION","WORKPLACE","INTERVIEW","ACADEMIC","TRAVEL","CONFIDENCE"]);

export async function getOnboarding(learnerId: string) {
  const result = await pool.query(
    `select lp.audience, lp.proficiency, lp.display_name,
            os.state, os.self_assessment, os.daily_goal_minutes, os.updated_at
       from learner_profiles lp
       join onboarding_states os on os.learner_id=lp.id
      where lp.id=$1`,
    [learnerId]
  );
  if (!result.rowCount) throw new Error("LEARNER_NOT_FOUND");
  const goalsResult = await pool.query(
    `select goal_code, priority from learning_goals where learner_id=$1 order by priority`,
    [learnerId]
  );
  return { ...result.rows[0], goals: goalsResult.rows };
}

export async function selectAudience(learnerId: string, audience: string) {
  if (!audiences.has(audience)) throw new Error("INVALID_AUDIENCE");
  await pool.query(
    `update learner_profiles set audience=$2, updated_at=now() where id=$1`,
    [learnerId, audience]
  );
  await pool.query(
    `update onboarding_states set state='AUDIENCE_SELECTED', updated_at=now() where learner_id=$1`,
    [learnerId]
  );
}

export async function setGoals(learnerId: string, selectedGoals: string[]) {
  if (!selectedGoals.length || selectedGoals.some(g => !goals.has(g))) throw new Error("INVALID_GOAL");
  const client = await pool.connect();
  try {
    await client.query("begin");
    await client.query("delete from learning_goals where learner_id=$1", [learnerId]);
    for (let i=0; i<selectedGoals.length; i++) {
      await client.query(
        `insert into learning_goals(learner_id,goal_code,priority) values($1,$2,$3)`,
        [learnerId, selectedGoals[i], i + 1]
      );
    }
    await client.query(
      `update onboarding_states set state='GOALS_SELECTED', updated_at=now() where learner_id=$1`,
      [learnerId]
    );
    await client.query("commit");
  } catch (e) {
    await client.query("rollback");
    throw e;
  } finally {
    client.release();
  }
}

export async function setSelfAssessment(learnerId: string, assessment: Record<string, string>) {
  await pool.query(
    `update onboarding_states
        set self_assessment=$2, state='SELF_ASSESSMENT_COMPLETED', updated_at=now()
      where learner_id=$1`,
    [learnerId, assessment]
  );
}

export async function setDailyGoal(learnerId: string, minutes: number) {
  if (![5,10,15,20,30,45,60].includes(minutes)) throw new Error("INVALID_DAILY_GOAL");
  await pool.query(
    `update onboarding_states
        set daily_goal_minutes=$2, state='DAILY_GOAL_SELECTED', updated_at=now()
      where learner_id=$1`,
    [learnerId, minutes]
  );
}
