import {pool} from "../../db.js";

type DailyGoalInput = {minutes:number};
type NotificationPrefsInput = {
  enabled?:boolean;
  reminderEnabled?:boolean;
  reminderTimeLocal?:string|null;
  timezone?:string;
  quietHoursEnabled?:boolean;
};

const MIN_GOAL=5;
const MAX_GOAL=180;

function clampGoal(value:number){
  const n=Math.floor(Number(value));
  if(!Number.isFinite(n)||n<MIN_GOAL||n>MAX_GOAL) throw new Error("INVALID_DAILY_GOAL");
  return n;
}

export async function getDailyGoal(learnerId:string){
  const result=await pool.query(
    `select coalesce(os.daily_goal_minutes,15) target_minutes,
            coalesce(lp.timezone,'UTC') timezone,
            coalesce(lp.reminder_enabled,false) reminder_enabled,
            coalesce(lp.notifications_enabled,true) notifications_enabled,
            lp.reminder_time_local,lp.quiet_hours_enabled
       from learner_profiles p
       left join onboarding_states os on os.learner_id=p.id
       left join learner_preferences lp on lp.learner_id=p.id
      where p.id=$1`,[learnerId]);
  if(!result.rowCount) throw new Error("LEARNER_NOT_FOUND");
  return result.rows[0];
}

export async function setDailyGoal(learnerId:string, minutes:number){
  const target=clampGoal(minutes);
  await pool.query(
    `insert into onboarding_states(learner_id,state,daily_goal_minutes)
     values($1,'DAILY_GOAL_SELECTED',$2)
     on conflict(learner_id) do update set daily_goal_minutes=excluded.daily_goal_minutes,updated_at=now()`,
    [learnerId,target]);
  return refreshToday(learnerId);
}

export async function setNotificationPreferences(learnerId:string,input:NotificationPrefsInput){
  const existing=await getDailyGoal(learnerId);
  const enabled=input.enabled ?? input.reminderEnabled ?? existing.reminder_enabled;
  const timezone=input.timezone ?? existing.timezone;
  try{ new Intl.DateTimeFormat("en-US",{timeZone:timezone}).format(); }catch{ throw new Error("INVALID_TIMEZONE"); }
  const time=input.reminderTimeLocal===undefined ? existing.reminder_time_local : input.reminderTimeLocal;
  if(time!==null && time!==undefined && !/^([01]\\d|2[0-3]):[0-5]\\d$/.test(String(time))) throw new Error("INVALID_REMINDER_TIME");
  const quiet=input.quietHoursEnabled ?? existing.quiet_hours_enabled ?? false;
  await pool.query(
    `insert into learner_preferences(learner_id,timezone,reminder_enabled,notifications_enabled,reminder_time_local,quiet_hours_enabled)
     values($1,$2,$3,$3,$4,$5)
     on conflict(learner_id) do update set timezone=excluded.timezone,reminder_enabled=excluded.reminder_enabled,
       notifications_enabled=excluded.notifications_enabled,reminder_time_local=excluded.reminder_time_local,
       quiet_hours_enabled=excluded.quiet_hours_enabled,updated_at=now()`,
    [learnerId,timezone,Boolean(enabled),time||null,Boolean(quiet)]);
  return getDailyGoal(learnerId);
}

export async function refreshToday(learnerId:string){
  const prefs=await getDailyGoal(learnerId);
  const dateResult=await pool.query(`select ((now() at time zone $2)::date) local_date`,[learnerId,prefs.timezone]);
  const localDate=dateResult.rows[0].local_date;
  return refreshDate(learnerId,String(localDate),Number(prefs.target_minutes),prefs.timezone);
}

export async function refreshDate(learnerId:string,localDate:string,targetMinutes:number,timezone:string){
  const totals=await pool.query(
    `select
       coalesce(sum(greatest(0,extract(epoch from (coalesce(aa.submitted_at,aa.created_at)-coalesce(aa.started_at,aa.created_at))))/60.0),0) elapsed_minutes,
       count(distinct aa.id)::int meaningful_sessions
       from activity_attempts aa
       join learning_sessions ls on ls.id=aa.session_id and ls.learner_id=$1
       join learning_evidence le on le.attempt_id=aa.id
      where ((aa.created_at at time zone $2)::date)=$3`,
    [learnerId,timezone,localDate]);

  const speaking=await pool.query(
    `select coalesce(sum(sa.duration_ms),0)::bigint duration_ms
       from speaking_attempts sa
       join activity_attempts aa on aa.id=sa.activity_attempt_id
       join learning_sessions ls on ls.id=aa.session_id and ls.learner_id=$1
       join learning_evidence le on le.attempt_id=aa.id
      where sa.duration_ms is not null and ((aa.created_at at time zone $2)::date)=$3`,
    [learnerId,timezone,localDate]);

  // For speaking, use recorded speech duration instead of the broader attempt interval.
  // Non-speaking evidence-backed attempts use their bounded active interval.
  const speechMinutes=Number(speaking.rows[0].duration_ms||0)/60000;
  const totalMinutes=Math.max(0,Number(totals.rows[0].elapsed_minutes)-speechMinutes)+speechMinutes;
  const meaningfulMinutes=Math.round(totalMinutes*100)/100;
  const sessions=Number(totals.rows[0].meaningful_sessions||0);
  const goalMet=meaningfulMinutes>=targetMinutes;

  const saved=await pool.query(
    `insert into daily_learning_progress(learner_id,local_date,target_minutes,meaningful_minutes,meaningful_sessions,goal_met,calculated_at)
     values($1,$2,$3,$4,$5,$6,now())
     on conflict(learner_id,local_date) do update set target_minutes=excluded.target_minutes,
       meaningful_minutes=excluded.meaningful_minutes,meaningful_sessions=excluded.meaningful_sessions,
       goal_met=excluded.goal_met,calculated_at=now()
     returning id,learner_id,local_date,target_minutes,meaningful_minutes,meaningful_sessions,goal_met,calculated_at`,
    [learnerId,localDate,targetMinutes,meaningfulMinutes,sessions,goalMet]);
  return saved.rows[0];
}

export async function getDailyProgress(learnerId:string,days=7){
  const prefs=await getDailyGoal(learnerId);
  await refreshToday(learnerId);
  const safe=Math.max(1,Math.min(31,Math.floor(days)));
  const result=await pool.query(
    `select local_date,target_minutes,meaningful_minutes,meaningful_sessions,goal_met
       from daily_learning_progress
      where learner_id=$1
      order by local_date desc limit $2`,[learnerId,safe]);
  return {today:result.rows[0]??null,days:result.rows,goal:prefs};
}

export async function getStreak(learnerId:string){
  const prefs=await getDailyGoal(learnerId);
  await refreshToday(learnerId);
  const rows=await pool.query(
    `select local_date,goal_met from daily_learning_progress
      where learner_id=$1 and local_date <= ((now() at time zone $2)::date)
      order by local_date desc limit 400`,[learnerId,prefs.timezone]);
  let streak=0;
  let expected=rows.rows.length?new Date(`${rows.rows[0].local_date}T00:00:00Z`):null;
  for(const row of rows.rows){
    const date=new Date(`${row.local_date}T00:00:00Z`);
    if(!expected || date.getTime()!==expected.getTime()) break;
    if(!row.goal_met) break;
    streak++;
    expected=new Date(expected.getTime()-86400000);
  }
  return {currentStreak:streak,goalMinutes:Number(prefs.target_minutes),timezone:prefs.timezone};
}

export async function getNotificationEligibility(learnerId:string){
  const daily=await getDailyProgress(learnerId,1);
  const prefs=daily.goal;
  const today=daily.today;
  const eligible=Boolean(prefs.notifications_enabled && prefs.reminder_enabled && !today?.goal_met && !prefs.quiet_hours_enabled);
  if(!eligible) return {eligible:false,reason:!prefs.notifications_enabled?"NOTIFICATIONS_DISABLED":!prefs.reminder_enabled?"REMINDERS_DISABLED":prefs.quiet_hours_enabled?"QUIET_HOURS":"GOAL_ALREADY_MET"};
  const localDate=today?.local_date;
  const event=await pool.query(
    `insert into notification_events(learner_id,notification_type,local_date,status,payload)
     values($1,'DAILY_GOAL_REMINDER',$2,'ELIGIBLE',$3)
     on conflict(learner_id,notification_type,local_date) do update set payload=excluded.payload
     returning id,status,local_date`,
    [learnerId,localDate,JSON.stringify({targetMinutes:prefs.target_minutes,meaningfulMinutes:today?.meaningful_minutes??0})]);
  return {eligible:true,notification:event.rows[0],targetMinutes:Number(prefs.target_minutes),meaningfulMinutes:Number(today?.meaningful_minutes??0)};
}
