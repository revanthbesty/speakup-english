import type {IncomingMessage,ServerResponse} from "node:http";
import {getAuthenticatedUser} from "../../security/session.js";
import {getMvpActivityDefinition,evaluateSentence,recordDeterministicEvaluation} from "./mvp-activities.js";
import {getAttempt} from "./activity-runtime.js";

async function body(req:IncomingMessage){
  let raw=""; for await(const c of req) raw+=c;
  return raw?JSON.parse(raw):{};
}
function auth(req:IncomingMessage){
  const v=req.headers.authorization;
  return v?.startsWith("Bearer ")?v.slice(7):null;
}
function send(res:ServerResponse,status:number,data:unknown){
  res.statusCode=status;res.setHeader("content-type","application/json");
  res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));
}
function fail(res:ServerResponse,status:number,code:string,message:string){
  res.statusCode=status;res.setHeader("content-type","application/json");
  res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));
}

export async function handleMvpActivityRoutes(
  req:IncomingMessage,res:ServerResponse,pathname:string
){
  const match=pathname.match(/^\/api\/v1\/mvp\/activities\/(VERB_INTRODUCTION|SENTENCE_BUILDER)$/);
  if(!match) return false;

  const token=auth(req);
  if(!token){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(token);
  if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}

  try{
    if(req.method==="GET"){
      return send(res,200,getMvpActivityDefinition(match[1]!));
    }

    if(req.method==="POST" && match[1]!==="SENTENCE_BUILDER"){
      const input=await body(req);
      const evaluation=evaluateSentence(input.tokens ?? input.sentence);
      if(input.attemptId){
        return send(res,200,await recordDeterministicEvaluation(
          user.learner_id,input.attemptId,evaluation
        ));
      }
      return send(res,200,evaluation);
    }

    return fail(res,405,"METHOD_NOT_ALLOWED","Method is not supported.");
  }catch(e){
    const code=e instanceof Error?e.message:"INTERNAL_ERROR";
    const known:Record<string,[number,string]>={
      ATTEMPT_NOT_FOUND:[404,"Attempt not found."]
    };
    const [status,message]=known[code]??[500,"Unable to evaluate the activity."];
    fail(res,status,code,message);return true;
  }
}
