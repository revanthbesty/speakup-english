import type { IncomingMessage,ServerResponse } from "node:http";
import { getAuthenticatedUser } from "../../security/session.js";
import { getActivity,startActivity,updateAttempt,submitAttempt,retryAttempt } from "./activity-runtime.js";

async function readBody(req:IncomingMessage) {
  let raw=""; for await(const chunk of req) raw+=chunk;
  return raw?JSON.parse(raw):{};
}
function token(req:IncomingMessage) {
  const v=req.headers.authorization; return v?.startsWith("Bearer ")?v.slice(7):null;
}
function send(res:ServerResponse,status:number,data:unknown) {
  res.statusCode=status; res.setHeader("content-type","application/json");
  res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));
}
function fail(res:ServerResponse,status:number,code:string,message:string) {
  res.statusCode=status; res.setHeader("content-type","application/json");
  res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));
}
export async function handleActivityRoutes(req:IncomingMessage,res:ServerResponse,pathname:string) {
  if(!pathname.startsWith("/api/v1/activities") && !pathname.startsWith("/api/v1/attempts")) return false;
  const t=token(req); if(!t){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(t); if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}

  try {
    const activityMatch=pathname.match(/^\/api\/v1\/activities\/([^/]+)(?:\/(.+))?$/);
    if(activityMatch) {
      const id=activityMatch[1]!, action=activityMatch[2]!;
      if(req.method==="GET" && !action) return send(res,200,await getActivity(user.learner_id,id));
      if(req.method==="POST" && action==="start") {
        const input=await readBody(req);
        return send(res,201,await startActivity(user.learner_id,id,input.sessionId));
      }
    }

    const attemptMatch=pathname.match(/^\/api\/v1\/attempts\/([^/]+)(?:\/(.+))?$/);
    if(attemptMatch) {
      const id=attemptMatch[1]!,action=attemptMatch[2]!;
      if(req.method==="PATCH" && !action) {
        const input=await readBody(req);
        return send(res,200,await updateAttempt(user.learner_id,id,input.input ?? {},input.progress ?? 0));
      }
      if(req.method==="POST" && action==="submit") return send(res,202,await submitAttempt(user.learner_id,id));
      if(req.method==="GET" && !action) {
        const runtime=await import("./activity-runtime.js");
        return send(res,200,await runtime.getAttempt(user.learner_id,id));
      }
      if(req.method==="POST" && action==="retry") return send(res,201,await retryAttempt(user.learner_id,id));
    }

    return fail(res,404,"NOT_FOUND","Activity route not found.");
  } catch(e) {
    const code=e instanceof Error?e.message:"INTERNAL_ERROR";
    const known:Record<string,[number,string]>={
      ACTIVITY_NOT_AVAILABLE:[403,"This activity is not available in your current learning journey."],
      ACTIVITY_NOT_FOUND:[404,"Activity not found."],
      SESSION_NOT_FOUND:[404,"Learning session not found."],
      ATTEMPT_NOT_FOUND:[404,"Attempt not found."],
      ATTEMPT_NOT_EDITABLE:[409,"This attempt can no longer be edited."],
      ATTEMPT_NOT_SUBMITTABLE:[409,"This attempt cannot be submitted in its current state."],
      ATTEMPT_NOT_RETRYABLE:[409,"This attempt is not ready for retry."]
    };
    const [status,message]=known[code]??[500,"Unable to process the activity."];
    fail(res,status,code,message); return true;
  }
}
