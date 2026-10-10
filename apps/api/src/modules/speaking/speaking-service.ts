import {pool} from "../../db.js";
import type {SpeechProvider,SttResult} from "./speaking-types.js";
import { processSpeechIntelligence } from "./speech-intelligence-service.js";

export async function createSpeakingAttempt(
  learnerId:string,
  activityAttemptId:string
){
  const ownership=await pool.query(
    `select aa.id,aa.activity_id
       from activity_attempts aa
       join learning_sessions ls on ls.id=aa.session_id
      where aa.id=$1 and ls.learner_id=$2`,
    [activityAttemptId,learnerId]
  );

  if(!ownership.rowCount) throw new Error("ATTEMPT_NOT_FOUND");

  const existing=await pool.query(
    `select id,processing_status,duration_ms,transcript,transcript_confidence,
            audio_retained,failure_code,failure_retryable
       from speaking_attempts
      where activity_attempt_id=$1
      order by created_at desc limit 1`,
    [activityAttemptId]
  );

  if(existing.rowCount) return existing.rows[0];

  const created=await pool.query(
    `insert into speaking_attempts(activity_attempt_id,processing_status,audio_retained)
     values($1,'READY',false)
     returning id,processing_status,audio_retained`,
    [activityAttemptId]
  );

  return created.rows[0];
}

export async function markRecordingStarted(
  learnerId:string,
  speakingAttemptId:string
){
  await assertSpeakingOwnership(learnerId,speakingAttemptId);

  const result=await pool.query(
    `update speaking_attempts
        set processing_status='CAPTURING',updated_at=now()
      where id=$1 and processing_status='READY'
      returning id,processing_status`,
    [speakingAttemptId]
  );

  if(!result.rowCount) throw new Error("SPEAKING_STATE_INVALID");
  return result.rows[0];
}

export async function submitAudioForProcessing(
  learnerId:string, speakingAttemptId:string, temporaryObjectKey:string, durationMs:number, provider?:SpeechProvider
){
  await assertSpeakingOwnership(learnerId,speakingAttemptId);
  if(durationMs<=0) throw new Error("EMPTY_AUDIO");
  if(durationMs>120000) throw new Error("AUDIO_TOO_LONG");
  await pool.query(`update speaking_attempts set processing_status='PROCESSING',audio_storage_key=$2,duration_ms=$3,audio_retained=false,updated_at=now() where id=$1`,[speakingAttemptId,temporaryObjectKey,durationMs]);
  try{
    const speech=await processSpeechIntelligence({learnerId,speakingAttemptId,temporaryObjectKey,durationMs});
    if(speech.status==="TECHNICAL_FAILURE") return {status:"FAILED",failureCode:"SPEECH_ANALYSIS_UNAVAILABLE",retryable:true,learnerMessage:"Your voice was recorded, but we could not analyze it this time. Please try again."};
    return {status:"COMPLETED",transcript:speech.stt.transcript,confidence:speech.stt.confidence,durationMs:speech.stt.durationMs,rawAudioRetained:false,speechIntelligence:speech};
  }catch(error){
    const code=error instanceof Error?error.message:"STT_FAILED";
    const retryable=["STT_PROVIDER_UNAVAILABLE","SPEECH_PROVIDER_UNAVAILABLE","STT_TIMEOUT"].includes(code);
    await pool.query(`update speaking_attempts set processing_status='FAILED',failure_code=$2,failure_retryable=$3,audio_storage_key=null,audio_retained=false,updated_at=now() where id=$1`,[speakingAttemptId,code,retryable]);
    return {status:"FAILED",failureCode:code,retryable,learnerMessage:"Your voice could not be processed fairly this time. Please try again."};
  }
}

async function persistStt(
  learnerId:string,
  speakingAttemptId:string,
  stt:SttResult
){
  await assertSpeakingOwnership(learnerId,speakingAttemptId);

  await pool.query(
    `update speaking_attempts
        set processing_status='COMPLETED',
            transcript=$2,
            transcript_confidence=$3,
            duration_ms=coalesce($4,duration_ms),
            audio_storage_key=null,
            audio_retained=false,
            updated_at=now()
      where id=$1`,
    [speakingAttemptId,stt.transcript,stt.confidence,stt.durationMs ?? null]
  );
}

async function assertSpeakingOwnership(
  learnerId:string,
  speakingAttemptId:string
){
  const result=await pool.query(
    `select 1
       from speaking_attempts sa
       join activity_attempts aa on aa.id=sa.activity_attempt_id
       join learning_sessions ls on ls.id=aa.session_id
      where sa.id=$1 and ls.learner_id=$2`,
    [speakingAttemptId,learnerId]
  );

  if(!result.rowCount) throw new Error("SPEAKING_ATTEMPT_NOT_FOUND");
}
