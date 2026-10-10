import type {IncomingMessage,ServerResponse} from "node:http";
import {getAuthenticatedUser} from "../../security/session.js";
import {createEvidenceFromEvaluation} from "./evidence-service.js";
import {applyEvidenceToMastery,getMastery} from "../mastery/mastery-service.js";
import {awardXpFromEvidence} from "../gamification/gamification-service.js";
import {syncLessonProgressFromEvidence} from "../progress/progress-service.js";
import {recordLearningOutcome} from "../recommendation/recommendation-service.js";
import {refreshToday} from "../daily/daily-service.js";

function token(req:IncomingMessage){
  const value=req.headers.authorization;
  return value?.startsWith("Bearer ")?value.slice(7):null;
}
function send(res:ServerResponse,status:number,data:unknown){
  res.statusCode=status;res.setHeader("content-type","application/json");
  res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));
}
function fail(res:ServerResponse,status:number,code:string,message:string){
  res.statusCode=status;res.setHeader("content-type","application/json");
  res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));
}

export async function handleEvidenceRoutes(
  req:IncomingMessage,res:ServerResponse,pathname:string
){
  if(!pathname.startsWith("/api/v1/evidence") && !pathname.startsWith("/api/v1/mastery"))
    return false;

  const value=token(req);
  if(!value){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(value);
  if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}

  try{
    const create=pathname.match(/^\/api\/v1\/evidence$/);
    if(req.method==="POST" && create){
      let raw="";for await(const c of req)raw+=c;
      const input=raw?JSON.parse(raw):{};
      const evidence=await createEvidenceFromEvaluation(
        user.learner_id,input.attemptId,input.evaluationId
      );

      const mastery=[];
      const gamification=[];
      const adaptive=[];
      for(const item of evidence){
        mastery.push(await applyEvidenceToMastery(user.learner_id,item.id));
        gamification.push(await awardXpFromEvidence(user.learner_id,item.id));
        const outcome = String((item as any).outcome ?? "NEUTRAL") as "CORRECT"|"PARTIAL"|"INCORRECT"|"NEUTRAL";
        adaptive.push(await recordLearningOutcome(user.learner_id,item.skill,outcome));
      }
      const progress=evidence.length ? await syncLessonProgressFromEvidence(user.learner_id,input.attemptId) : null;
      const daily=evidence.length ? await refreshToday(user.learner_id) : null;

      return send(res,201,{evidence,mastery,gamification,adaptive,progress,daily});
    }

    if(req.method==="GET" && pathname==="/api/v1/mastery"){
      return send(res,200,await getMastery(user.learner_id));
    }

    return fail(res,404,"NOT_FOUND","Evidence or mastery route not found.");
  }catch(error){
    const code=error instanceof Error?error.message:"INTERNAL_ERROR";
    const known:Record<string,[number,string]>={
      ATTEMPT_NOT_FOUND:[404,"Activity attempt not found."],
      EVALUATION_NOT_FOUND:[404,"Evaluation not found."],
      EVALUATION_NOT_COMPLETE:[409,"Evaluation is not complete."],
      EVIDENCE_NOT_FOUND:[404,"Learning evidence not found."]
    };
    const [status,message]=known[code]??[500,"Unable to process learning evidence."];
    fail(res,status,code,message);return true;
  }
}
