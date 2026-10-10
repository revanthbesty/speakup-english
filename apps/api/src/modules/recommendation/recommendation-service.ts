import {pool} from "../../db.js";

type Candidate={id:string;code:string;type:string;objective:string;lesson_id:string;lesson_code:string;skill:string;priority:number;difficulty:number;reason:string};

export async function getRecommendation(learnerId:string){
  const path=await pool.query(`select id from learning_paths where learner_id=$1 and status='ACTIVE' order by version desc limit 1`,[learnerId]);
  if(!path.rowCount) return {recommendation:null,adaptiveDifficulty:[]};
  const mastery=await pool.query(`select skill,mastery_score,confidence,evidence_count,trend from skill_mastery where learner_id=$1 order by mastery_score asc,confidence asc,skill`,[learnerId]);
  const weak=mastery.rows[0]??null;
  const candidates=await pool.query(`
    select a.id,a.code,a.type,a.objective,a.lesson_id,l.code lesson_code,
      coalesce((select skill from activity_skills s where s.activity_id=a.id order by skill limit 1),'SENTENCE_CONSTRUCTION') skill
    from activities a join lessons l on l.id=a.lesson_id
    join lesson_progress lp on lp.lesson_id=l.id and lp.learning_path_id=$2 and lp.learner_id=$1
    where a.status='PUBLISHED' and lp.status<>'COMPLETED'
    order by case a.type when 'VERB_INTRODUCTION' then 1 when 'SENTENCE_BUILDER' then 2 when 'SPEAKING' then 3 else 99 end,a.code
    limit 20`,[learnerId,path.rows[0].id]);
  const states=await pool.query(`select skill,difficulty,confidence,consecutive_successes,consecutive_failures,last_outcome from difficulty_states where learner_id=$1`,[learnerId]);
  const stateMap=new Map(states.rows.map((r:any)=>[r.skill,r]));
  const scored:Candidate[]=candidates.rows.map((r:any)=>{
    const skill=r.skill;
    const m=mastery.rows.find((x:any)=>x.skill===skill);
    const d=stateMap.get(skill);
    const masteryGap=m?1-Number(m.mastery_score):0.7;
    const difficulty=Number(d?.difficulty??0.5);
    const typeBoost=r.type==='SPEAKING'?0.12:r.type==='SENTENCE_BUILDER'?0.10:0.04;
    const weakBoost=weak&&weak.skill===skill?0.25:0;
    return {...r,skill,priority:masteryGap+weakBoost+typeBoost,difficulty,reason:weak&&weak.skill===skill?`Practice ${skill} because it is currently your weakest demonstrated skill.`:`Continue the next activity to build consistent evidence.`};
  });
  scored.sort((a,b)=>b.priority-a.priority);
  const selected=scored[0]??null;
  const recommendation=selected?{type:'PRACTICE',activityId:selected.id,activityCode:selected.code,activityType:selected.type,skill:selected.skill,difficulty:selected.difficulty,reason:selected.reason,priority:Number(selected.priority.toFixed(3)),source:'RULE_ENGINE'}:{type:'START_LEARNING',reason:'Your next learning activity is ready.',source:'RULE_ENGINE'};
  return {recommendation,adaptiveDifficulty:states.rows};
}

export async function recordLearningOutcome(learnerId:string,skill:string,outcome:'CORRECT'|'PARTIAL'|'INCORRECT'|'NEUTRAL'){
  const current=await pool.query(`select difficulty,confidence,consecutive_successes,consecutive_failures from difficulty_states where learner_id=$1 and skill=$2`,[learnerId,skill]);
  const row=current.rows[0]??{difficulty:0.5,confidence:0.2,consecutive_successes:0,consecutive_failures:0};
  let difficulty=Number(row.difficulty), confidence=Number(row.confidence);
  let successes=Number(row.consecutive_successes), failures=Number(row.consecutive_failures);
  if(outcome==='CORRECT'){successes++;failures=0;if(successes>=2)difficulty=Math.min(1,difficulty+0.05);}
  else if(outcome==='PARTIAL'){successes=0;failures++;difficulty=Math.max(0,difficulty-0.02);}
  else if(outcome==='INCORRECT'){successes=0;failures++;difficulty=Math.max(0,difficulty-0.08);}
  confidence=Math.min(1,confidence+(outcome==='NEUTRAL'?0.01:0.05));
  await pool.query(`insert into difficulty_states(learner_id,skill,difficulty,confidence,consecutive_successes,consecutive_failures,last_outcome,algorithm_version) values($1,$2,$3,$4,$5,$6,$7,'mvp-adaptive-v1') on conflict(learner_id,skill) do update set difficulty=excluded.difficulty,confidence=excluded.confidence,consecutive_successes=excluded.consecutive_successes,consecutive_failures=excluded.consecutive_failures,last_outcome=excluded.last_outcome,updated_at=now()`,[learnerId,skill,difficulty,confidence,successes,failures,outcome]);
  return {skill,difficulty:Number(difficulty.toFixed(2)),confidence:Number(confidence.toFixed(2)),consecutiveSuccesses:successes,consecutiveFailures:failures,lastOutcome:outcome};
}

export async function getAdaptiveDifficulty(learnerId:string){
  const r=await pool.query(`select skill,difficulty,confidence,consecutive_successes,consecutive_failures,last_outcome,algorithm_version,updated_at from difficulty_states where learner_id=$1 order by skill`,[learnerId]);
  return r.rows;
}
