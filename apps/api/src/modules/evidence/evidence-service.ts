import {pool} from "../../db.js";

type EvidenceInput = {
  attemptId:string;
  evaluationId:string;
  skill:string;
  provenance:"DETERMINISTIC"|"AI"|"SPEECH"|"HUMAN"|"SYSTEM_DERIVED";
  strength:number;
  confidence:number;
  independenceFactor?:number;
  difficultyFactor?:number;
  contextFactor?:number;
  details?:Record<string,unknown>;
};

export async function createEvidence(
  learnerId:string,
  input:EvidenceInput
){
  const ownership=await pool.query(
    `select aa.id
       from activity_attempts aa
       join learning_sessions ls on ls.id=aa.session_id
      where aa.id=$1 and ls.learner_id=$2`,
    [input.attemptId,learnerId]
  );
  if(!ownership.rowCount) throw new Error("ATTEMPT_NOT_FOUND");

  const evaluation=await pool.query(
    `select e.id,e.status
       from evaluations e
       join activity_attempts aa on aa.id=e.attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where e.id=$1 and e.attempt_id=$2 and ls.learner_id=$3`,
    [input.evaluationId,input.attemptId,learnerId]
  );
  if(!evaluation.rowCount) throw new Error("EVALUATION_NOT_FOUND");
  if(evaluation.rows[0].status!=="COMPLETED") throw new Error("EVALUATION_NOT_COMPLETE");

  const existing=await pool.query(
    `select id from learning_evidence
      where evaluation_id=$1 and skill=$2`,
    [input.evaluationId,input.skill]
  );
  if(existing.rowCount) return existing.rows[0];

  const result=await pool.query(
    `insert into learning_evidence
      (attempt_id,evaluation_id,skill,provenance,evidence_strength,confidence,
       independence_factor,difficulty_factor,context_factor,evidence)
     values($1,$2,$3,$4,$5,$6,$7,$8,$9,$10)
     returning id,skill,evidence_strength,confidence,created_at`,
    [
      input.attemptId,input.evaluationId,input.skill,input.provenance,
      clamp(input.strength),clamp(input.confidence),
      clamp(input.independenceFactor??1),
      clamp(input.difficultyFactor??1),
      clamp(input.contextFactor??1),
      JSON.stringify(input.details??{})
    ]
  );
  return result.rows[0];
}

export async function createEvidenceFromEvaluation(
  learnerId:string,
  attemptId:string,
  evaluationId:string
){
  const evaluation=await pool.query(
    `select e.result,e.outcome,e.confidence,e.evaluation_mode
       from evaluations e
       join activity_attempts aa on aa.id=e.attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where e.id=$1 and e.attempt_id=$2 and ls.learner_id=$3`,
    [evaluationId,attemptId,learnerId]
  );
  if(!evaluation.rowCount) throw new Error("EVALUATION_NOT_FOUND");

  const row=evaluation.rows[0];
  const result=row.result ?? {};
  const mapped=Array.isArray(result.evidence)?result.evidence:[];

  const evidence=[];
  for(const item of mapped){
    const skill=String(item.skill??"");
    if(!skill) continue;
    const strength=Number(item.weight??0.5);
    const created=await createEvidence(learnerId,{
      attemptId,
      evaluationId,
      skill,
      provenance:row.evaluation_mode==="SPEECH"?"SPEECH":
        row.evaluation_mode==="AI"?"AI":"DETERMINISTIC",
      strength,
      confidence:Number(row.confidence??0),
      details:{
        outcome:row.outcome,
        source:"evaluation"
      }
    });
    evidence.push({...created,outcome:row.outcome});
  }

  return evidence;
}

function clamp(value:number){
  return Math.max(0,Math.min(1,value));
}
