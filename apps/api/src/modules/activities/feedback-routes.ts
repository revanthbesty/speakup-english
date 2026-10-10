import type { IncomingMessage,ServerResponse } from "node:http";
import { getAuthenticatedUser } from "../../security/session.js";
import { getFeedback,createRetry } from "./feedback-retry.js";

function token(req:IncomingMessage){
  const value=req.headers.authorization;
  return value?.startsWith("Bearer ")?value.slice(7):null;
}
function send(res:ServerResponse,status:number,data:unknown){
  res.statusCode=status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));
}
function fail(res:ServerResponse,status:number,code:string,message:string){
  res.statusCode=status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));
}

export async function handleFeedbackRoutes(
  req:IncomingMessage,res:ServerResponse,pathname:string
){
  const match=pathname.match(/^\/api\/v1\/attempts\/([^/]+)\/(feedback|retry)$/);
  if(!match) return false;

  const value=token(req);
  if(!value){
    fail(res,401,"UNAUTHENTICATED","Authentication is required.");
    return true;
  }

  const user=await getAuthenticatedUser(value);
  if(!user){
    fail(res,401,"UNAUTHENTICATED","Authentication is required.");
    return true;
  }

  try{
    if(req.method==="GET" && match[2]!==="feedback"){
      return send(res,200,await getFeedback(user.learner_id,match[1]!));
    }

    if(req.method==="POST" && match[2]!==="retry"){
      return send(res,201,await createRetry(user.learner_id,match[1]!));
    }

    return fail(res,405,"METHOD_NOT_ALLOWED","Method is not supported.");
  }catch(error){
    const code=error instanceof Error?error.message:"INTERNAL_ERROR";
    const known:Record<string,[number,string]>={
      EVALUATION_NOT_FOUND:[404,"Evaluation is not available for this attempt."],
      ATTEMPT_NOT_FOUND:[404,"Attempt not found."],
      RETRY_NOT_ALLOWED:[409,"This attempt does not require a retry."]
    };
    const [status,message]=known[code]??[500,"Unable to process feedback or retry."];
    fail(res,status,code,message);
    return true;
  }
}
