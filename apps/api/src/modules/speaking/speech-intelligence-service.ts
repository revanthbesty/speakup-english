import { pool } from "../../db.js";
import { fluency, pronunciation, transcribe } from "../speech/speech-gateway.js";

export type SpeechIntelligence = {
  stt: { transcript:string; confidence:number; language:string; durationMs:number };
  pronunciation: { score:number; confidence:number; phonemeErrors?:unknown[]; wordErrors?:unknown[] } | null;
  fluency: { score:number; confidence:number; wordsPerMinute?:number; pauseRatio?:number; fillerCount?:number } | null;
  status:"COMPLETED"|"LOW_CONFIDENCE"|"TECHNICAL_FAILURE";
};

const clamp=(n:number)=>Math.max(0,Math.min(1,Number.isFinite(n)?n:0));

export async function processSpeechIntelligence(input:{learnerId:string; speakingAttemptId:string; temporaryObjectKey:string; durationMs:number}):Promise<SpeechIntelligence>{
  let stt:Awaited<ReturnType<typeof transcribe>>;
  try { stt=await transcribe({temporaryObjectKey:input.temporaryObjectKey,language:"en"}); }
  catch { throw new Error("STT_PROVIDER_UNAVAILABLE"); }
  const normalized={transcript:String(stt.transcript??""),confidence:clamp(Number(stt.confidence)),language:String(stt.language??"en"),durationMs:Number(stt.durationMs??input.durationMs)};
  if(normalized.confidence<0.55){
    await persist(input.learnerId,input.speakingAttemptId,normalized,null,null,"LOW_CONFIDENCE");
    return {stt:normalized,pronunciation:null,fluency:null,status:"LOW_CONFIDENCE"};
  }
  let p:null|Awaited<ReturnType<typeof pronunciation>>=null;
  let f:null|Awaited<ReturnType<typeof fluency>>=null;
  try { p=await pronunciation({temporaryObjectKey:input.temporaryObjectKey,transcript:normalized.transcript,language:normalized.language}); } catch {}
  try { f=await fluency({temporaryObjectKey:input.temporaryObjectKey,transcript:normalized.transcript,durationMs:normalized.durationMs}); } catch {}
  if(!p && !f){
    await persist(input.learnerId,input.speakingAttemptId,normalized,null,null,"TECHNICAL_FAILURE");
    return {stt:normalized,pronunciation:null,fluency:null,status:"TECHNICAL_FAILURE"};
  }
  await persist(input.learnerId,input.speakingAttemptId,normalized,p,f,"COMPLETED");
  return {stt:normalized,pronunciation:p,fluency:f,status:"COMPLETED"};
}

async function persist(learnerId:string,attemptId:string,stt:SpeechIntelligence["stt"],p:SpeechIntelligence["pronunciation"],f:SpeechIntelligence["fluency"],status:SpeechIntelligence["status"]){
  await pool.query(`UPDATE speaking_attempts SET processing_status=$2, transcript=$3, transcript_confidence=$4, duration_ms=$5, audio_storage_key=NULL, audio_retained=false, speech_result=$6, updated_at=NOW() WHERE id=$1`,[attemptId,status==="COMPLETED"?"COMPLETED":status==="LOW_CONFIDENCE"?"COMPLETED":"FAILED",stt.transcript,stt.confidence,stt.durationMs,JSON.stringify({speechIntelligence:{status,pronunciation:p,fluency:f}})]);
  await pool.query(`INSERT INTO speech_intelligence_results(speaking_attempt_id,stt_confidence,pronunciation_score,pronunciation_confidence,fluency_score,fluency_confidence,words_per_minute,pause_ratio,filler_count,interpretation_status,provider_summary) VALUES($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11) ON CONFLICT(speaking_attempt_id) DO UPDATE SET stt_confidence=EXCLUDED.stt_confidence,pronunciation_score=EXCLUDED.pronunciation_score,pronunciation_confidence=EXCLUDED.pronunciation_confidence,fluency_score=EXCLUDED.fluency_score,fluency_confidence=EXCLUDED.fluency_confidence,words_per_minute=EXCLUDED.words_per_minute,pause_ratio=EXCLUDED.pause_ratio,filler_count=EXCLUDED.filler_count,interpretation_status=EXCLUDED.interpretation_status,provider_summary=EXCLUDED.provider_summary`,[attemptId,stt.confidence,p?.score??null,p?.confidence??null,f?.score??null,f?.confidence??null,f?.wordsPerMinute??null,f?.pauseRatio??null,f?.fillerCount??0,status,JSON.stringify({pronunciationProvider:p?{provider:p.provider,model:p.model}:null,fluencyProvider:f?{provider:f.provider,model:f.model}:null})]);
}

export async function getSpeechIntelligence(learnerId:string,attemptId:string){
  const r=await pool.query(`SELECT sir.* FROM speech_intelligence_results sir JOIN speaking_attempts sa ON sa.id=sir.speaking_attempt_id JOIN activity_attempts aa ON aa.id=sa.activity_attempt_id JOIN learning_sessions ls ON ls.id=aa.session_id WHERE sir.speaking_attempt_id=$1 AND ls.learner_id=$2`,[attemptId,learnerId]);
  if(!r.rowCount) throw new Error("SPEECH_INTELLIGENCE_NOT_FOUND"); return r.rows[0];
}

export async function recordSpeakingMetric(learnerId:string,attemptId:string,meaningful:boolean,improved:boolean,durationMs:number){
  await pool.query(`INSERT INTO speaking_learning_metrics(learner_id,speaking_attempt_id,speaking_seconds,sentence_improved,meaningful) VALUES($1,$2,$3,$4,$5) ON CONFLICT(speaking_attempt_id) DO UPDATE SET speaking_seconds=EXCLUDED.speaking_seconds,sentence_improved=EXCLUDED.sentence_improved,meaningful=EXCLUDED.meaningful`,[learnerId,attemptId,Math.max(0,Math.round(durationMs/1000)),improved,meaningful]);
}
