import {pool} from "../../db.js";
import {
  evaluateSpeakingTranscript,
  lowConfidenceSpeakingResult
} from "./speaking-evaluator.js";
import { getSpeechIntelligence } from "./speech-intelligence-service.js";

export async function evaluateSpeakingAttempt(
  learnerId:string,
  speakingAttemptId:string
){
  const result=await pool.query(
    `select sa.id,sa.transcript,sa.transcript_confidence,sa.duration_ms,aa.id activity_attempt_id
       from speaking_attempts sa
       join activity_attempts aa on aa.id=sa.activity_attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where sa.id=$1 and ls.learner_id=$2`,
    [speakingAttemptId,learnerId]
  );

  if(!result.rowCount) throw new Error("SPEAKING_ATTEMPT_NOT_FOUND");

  const row=result.rows[0];

  if(row.transcript_confidence !== null && Number(row.transcript_confidence)<0.55){
    return persistEvaluation(learnerId,speakingAttemptId,lowConfidenceSpeakingResult(),Number(row.duration_ms??0));
  }

  if(!row.transcript){
    throw new Error("TRANSCRIPT_NOT_AVAILABLE");
  }

  const evaluation=evaluateSpeakingTranscript(row.transcript);
  const intelligence=await getSpeechIntelligence(learnerId,speakingAttemptId).catch(()=>null);
  if(intelligence){
    const p=intelligence.pronunciation_score===null?null:Number(intelligence.pronunciation_score);
    const f=intelligence.fluency_score===null?null:Number(intelligence.fluency_score);
    if(p!==null){
      evaluation.dimensions.sentenceConstruction=Number((evaluation.dimensions.sentenceConstruction*.75+p*.25).toFixed(4));
    }
    if(f!==null){
      evaluation.dimensions.completeness=Number((evaluation.dimensions.completeness*.8+f*.2).toFixed(4));
    }
    if(intelligence.interpretation_status!=="COMPLETED") evaluation.status="LOW_CONFIDENCE";
    if(evaluation.status!=="LOW_CONFIDENCE" && evaluation.outcome!=="NEUTRAL") evaluation.feedback.explanation += p!==null ? ` Pronunciation signal: ${Math.round(p*100)}%.` : "";
  }
  return persistEvaluation(learnerId,speakingAttemptId,evaluation,Number(row.duration_ms??0));
}

async function persistEvaluation(
  learnerId:string,
  speakingAttemptId:string,
  evaluation:ReturnType<typeof evaluateSpeakingTranscript>,
  durationMs:number
){
  const ownership=await pool.query(
    `select aa.id
       from speaking_attempts sa
       join activity_attempts aa on aa.id=sa.activity_attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where sa.id=$1 and ls.learner_id=$2`,
    [speakingAttemptId,learnerId]
  );

  if(!ownership.rowCount) throw new Error("SPEAKING_ATTEMPT_NOT_FOUND");

  const client=await pool.connect();
  try{
    await client.query("begin");

    const inserted=await client.query(
      `insert into evaluations
       (attempt_id,evaluation_mode,status,outcome,score,confidence,result,evaluator_version)
       values($1,'SPEECH','COMPLETED',$2,$3,$4,$5,'mvp-speaking-evaluator-v1')
       returning id`,
      [
        ownership.rows[0].id,
        evaluation.outcome,
        score(evaluation),
        evaluation.confidence,
        JSON.stringify({
          dimensions:evaluation.dimensions,
          feedback:evaluation.feedback,
          evidence:evaluation.evidence
        })
      ]
    );

    await client.query(
      `update activity_attempts
          set status='EVALUATED'
        where id=$1`,
      [ownership.rows[0].id]
    );

    await client.query(
      `update speaking_attempts
          set speech_result=$2,updated_at=now()
        where id=$1`,
      [speakingAttemptId,JSON.stringify(evaluation)]
    );

    await client.query(
      `insert into speaking_learning_metrics(learner_id,speaking_attempt_id,speaking_seconds,sentence_improved,meaningful)
       values($1,$2,$3,$4,$5)
       on conflict(speaking_attempt_id) do update set speaking_seconds=excluded.speaking_seconds,sentence_improved=excluded.sentence_improved,meaningful=excluded.meaningful`,
      [learnerId,speakingAttemptId,Math.max(0,Math.round(durationMs/1000)),evaluation.outcome==="CORRECT",evaluation.outcome!=="NEUTRAL"]
    );

    await client.query("commit");

    return {
      evaluationId:inserted.rows[0].id,
      ...evaluation
    };
  }catch(error){
    await client.query("rollback");
    throw error;
  }finally{
    client.release();
  }
}

function score(evaluation:ReturnType<typeof evaluateSpeakingTranscript>){
  const d=evaluation.dimensions;
  return Number((
    d.sentenceConstruction*.30+
    d.grammar*.20+
    d.vocabulary*.15+
    d.completeness*.15+
    d.contextualRelevance*.20
  ).toFixed(4));
}
