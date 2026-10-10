import {pool} from "../../db.js";
import {getRecommendation} from "../recommendation/recommendation-service.js";

export async function getProgressSnapshot(learnerId:string){
  const profile=await pool.query(`select lp.id,lp.display_name,lp.audience,lp.proficiency,lp.updated_at profile_updated_at from learner_profiles lp where lp.id=$1`,[learnerId]);
  if(!profile.rowCount) throw new Error("LEARNER_NOT_FOUND");
  const journey=await getJourneyProgress(learnerId);
  const mastery=await pool.query(`select skill,mastery_score,confidence,evidence_count,trend,last_evidence_at from skill_mastery where learner_id=$1 order by mastery_score asc,skill`,[learnerId]);
  const xp=await pool.query(`select coalesce(lg.total_xp,0) total_xp,coalesce(lg.level,1) level,coalesce((select count(*) from learner_rewards lr where lr.learner_id=$1),0) reward_count from (select $1::uuid learner_id) seed left join learner_gamification lg on lg.learner_id=seed.learner_id`,[learnerId]);
  const activityStats=await pool.query(`select count(distinct e.attempt_id)::int evidence_attempts,count(*) filter(where aa.status in ('EVALUATED','FEEDBACK_SHOWN'))::int evaluated_attempts,count(*) filter(where aa.status='COMPLETED')::int completed_attempts,count(distinct date_trunc('day',aa.created_at))::int active_learning_days from activity_attempts aa join learning_sessions ls on ls.id=aa.session_id and ls.learner_id=$1 left join learning_evidence e on e.attempt_id=aa.id`,[learnerId]);
  const recommendationState=await getRecommendation(learnerId);
  return {profile:profile.rows[0],journey,mastery:mastery.rows,gamification:xp.rows[0],activityStats:activityStats.rows[0],recommendation:recommendationState.recommendation,adaptiveDifficulty:recommendationState.adaptiveDifficulty};
}

export async function getJourneyProgress(learnerId:string){
  const path=await pool.query(`select id,version,placement_proficiency,audience,status,created_at,updated_at from learning_paths where learner_id=$1 and status='ACTIVE' order by version desc limit 1`,[learnerId]);
  if(!path.rowCount) return {path:null,lessons:[],nextActivity:null,percentComplete:0};
  const lessons=await pool.query(`select lp.id progress_id,lp.lesson_id,lp.status,lp.completed_activities,lp.current_activity_id,lp.started_at,lp.completed_at,l.code lesson_code,l.name lesson_name,l.objective,u.code unit_code,u.name unit_name,w.code world_code,w.name world_name,(select count(*)::int from activities a where a.lesson_id=l.id and a.status='PUBLISHED') activity_count,(select count(distinct aa.activity_id)::int from activity_attempts aa join learning_sessions ls on ls.id=aa.session_id and ls.learner_id=$1 join activities ax on ax.id=aa.activity_id where ax.lesson_id=l.id and exists(select 1 from learning_evidence le where le.attempt_id=aa.id)) evidence_activity_count from lesson_progress lp join lessons l on l.id=lp.lesson_id join units u on u.id=l.unit_id join worlds w on w.id=u.world_id where lp.learner_id=$1 and lp.learning_path_id=$2 order by l.code`,[learnerId,path.rows[0].id]);
  const totalActivities=lessons.rows.reduce((n:number,r:any)=>n+Number(r.activity_count),0);
  const completedActivities=lessons.rows.reduce((n:number,r:any)=>n+Math.min(Number(r.activity_count),Number(r.evidence_activity_count)),0);
  const percentComplete=totalActivities?Math.round((completedActivities/totalActivities)*100):0;
  const nextLesson=lessons.rows.find((r:any)=>Number(r.evidence_activity_count)<Number(r.activity_count)) ?? null;
  let nextActivity=null;
  if(nextLesson){
    const activities=await pool.query(`select a.id,a.code,a.type,a.objective from activities a where a.lesson_id=$1 and a.status='PUBLISHED' order by case a.type when 'VERB_INTRODUCTION' then 1 when 'SENTENCE_BUILDER' then 2 when 'SPEAKING' then 3 else 99 end,a.code`,[nextLesson.lesson_id]);
    for(const activity of activities.rows){
      const done=await pool.query(`select 1 from activity_attempts aa join learning_sessions ls on ls.id=aa.session_id and ls.learner_id=$1 where aa.activity_id=$2 and exists(select 1 from learning_evidence le where le.attempt_id=aa.id) limit 1`,[learnerId,activity.id]);
      if(!done.rowCount){nextActivity=activity;break;}
    }
  }
  return {path:path.rows[0],lessons:lessons.rows,percentComplete,nextActivity};
}

export async function syncLessonProgressFromEvidence(learnerId:string,attemptId:string){
  const result=await pool.query(`select aa.activity_id,a.lesson_id,lp.id progress_id from activity_attempts aa join learning_sessions ls on ls.id=aa.session_id and ls.learner_id=$1 join activities a on a.id=aa.activity_id join lesson_progress lp on lp.learner_id=$1 and lp.lesson_id=a.lesson_id where aa.id=$2`,[learnerId,attemptId]);
  if(!result.rowCount) throw new Error("ATTEMPT_NOT_FOUND");
  const row=result.rows[0];
  const activityCount=await pool.query(`select count(*)::int count from activities where lesson_id=$1 and status='PUBLISHED'`,[row.lesson_id]);
  const completed=await pool.query(`select count(distinct a.id)::int count from activities a where a.lesson_id=$1 and a.status='PUBLISHED' and exists(select 1 from activity_attempts aa where aa.activity_id=a.id and exists(select 1 from learning_evidence le where le.attempt_id=aa.id) and exists(select 1 from learning_sessions ls where ls.id=aa.session_id and ls.learner_id=$2))`,[row.lesson_id,learnerId]);
  const count=Math.min(Number(activityCount.rows[0].count),Number(completed.rows[0].count));
  const status=count>=Number(activityCount.rows[0].count)&&count>0?'COMPLETED':'IN_PROGRESS';
  await pool.query(`update lesson_progress set status=$2,completed_activities=$3,current_activity_id=case when $2='COMPLETED' then null else current_activity_id end,started_at=coalesce(started_at,now()),completed_at=case when $2='COMPLETED' then coalesce(completed_at,now()) else null end,updated_at=now() where id=$1`,[row.progress_id,status,count]);
  return {progressId:row.progress_id,status,completedActivities:count};
}
