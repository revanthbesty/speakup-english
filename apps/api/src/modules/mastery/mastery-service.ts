import {pool} from "../../db.js";

export async function applyEvidenceToMastery(
  learnerId:string,
  evidenceId:string
){
  const evidence=await pool.query(
    `select le.id,le.skill,le.evidence_strength,le.confidence,
            le.independence_factor,le.difficulty_factor,le.context_factor,
            le.created_at
       from learning_evidence le
       join activity_attempts aa on aa.id=le.attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where le.id=$1 and ls.learner_id=$2`,
    [evidenceId,learnerId]
  );
  if(!evidence.rowCount) throw new Error("EVIDENCE_NOT_FOUND");

  const e=evidence.rows[0];
  const effective=clamp(
    Number(e.evidence_strength)*
    Number(e.confidence)*
    Number(e.independence_factor)*
    Number(e.difficulty_factor)*
    Number(e.context_factor)
  );

  const current=await pool.query(
    `select mastery_score,confidence,evidence_count,trend
       from skill_mastery
      where learner_id=$1 and skill=$2`,
    [learnerId,e.skill]
  );

  const previous=current.rowCount ? Number(current.rows[0].mastery_score) : 0;
  const previousConfidence=current.rowCount ? Number(current.rows[0].confidence) : 0;
  const count=current.rowCount ? Number(current.rows[0].evidence_count) : 0;

  // MVP: conservative incremental update; no single attempt can swing mastery dramatically.
  const learningRate=0.20;
  const next=clamp(previous + learningRate*(effective-previous));
  const nextConfidence=clamp(
    Math.min(1, previousConfidence + (0.12 * Number(e.confidence)))
  );

  const trend=next>previous+0.01?"IMPROVING":
    next<previous-0.01?"DECLINING":"STABLE";

  const client=await pool.connect();
  try{
    await client.query("begin");

    await client.query(
      `insert into skill_mastery
       (learner_id,skill,mastery_score,confidence,evidence_count,trend,
        last_evidence_at,algorithm_version)
       values($1,$2,$3,$4,1,$5,$6,'mvp-mastery-v1')
       on conflict(learner_id,skill) do update set
         mastery_score=excluded.mastery_score,
         confidence=excluded.confidence,
         evidence_count=skill_mastery.evidence_count+1,
         trend=excluded.trend,
         last_evidence_at=excluded.last_evidence_at,
         algorithm_version=excluded.algorithm_version,
         updated_at=now()`,
      [learnerId,e.skill,next,nextConfidence,trend,e.created_at]
    );

    await client.query(
      `insert into mastery_events
       (learner_id,skill,evidence_id,previous_score,new_score,
        previous_confidence,new_confidence,algorithm_version)
       values($1,$2,$3,$4,$5,$6,$7,'mvp-mastery-v1')`,
      [learnerId,e.skill,e.id,previous,next,previousConfidence,nextConfidence]
    );

    await client.query("commit");

    return {
      skill:e.skill,
      previousScore:previous,
      newScore:Number(next.toFixed(4)),
      previousConfidence,
      newConfidence:Number(nextConfidence.toFixed(4)),
      evidenceCount:count+1,
      trend
    };
  }catch(error){
    await client.query("rollback");
    throw error;
  }finally{
    client.release();
  }
}

export async function getMastery(learnerId:string){
  const result=await pool.query(
    `select skill,mastery_score,confidence,evidence_count,trend,
            last_evidence_at,algorithm_version
       from skill_mastery
      where learner_id=$1
      order by skill`,
    [learnerId]
  );
  return result.rows;
}

function clamp(value:number){
  return Math.max(0,Math.min(1,value));
}
