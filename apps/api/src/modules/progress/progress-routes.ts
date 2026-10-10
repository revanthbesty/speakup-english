import type {IncomingMessage,ServerResponse} from "node:http";
import {getAuthenticatedUser} from "../../security/session.js";
import {getProgressSnapshot,getJourneyProgress} from "./progress-service.js";

function token(req:IncomingMessage){const value=req.headers.authorization;return value?.startsWith("Bearer ")?value.slice(7):null;}
function send(res:ServerResponse,status:number,data:unknown){res.statusCode=status;res.setHeader("content-type","application/json");res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));}
function fail(res:ServerResponse,status:number,code:string,message:string){res.statusCode=status;res.setHeader("content-type","application/json");res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));}

export async function handleProgressRoutes(req:IncomingMessage,res:ServerResponse,pathname:string){
  if(!pathname.startsWith("/api/v1/progress")&&!pathname.startsWith("/api/v1/home")) return false;
  const value=token(req); if(!value){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(value); if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  try{
    if(req.method!=="GET") return fail(res,405,"METHOD_NOT_ALLOWED","Only GET is supported for learner state.");
    if(pathname==="/api/v1/home") return send(res,200,await getProgressSnapshot(user.learner_id));
    if(pathname==="/api/v1/progress") return send(res,200,await getProgressSnapshot(user.learner_id));
    if(pathname==="/api/v1/progress/journey") return send(res,200,await getJourneyProgress(user.learner_id));
    return fail(res,404,"NOT_FOUND","Progress route not found.");
  }catch(error){
    const code=error instanceof Error?error.message:"INTERNAL_ERROR";
    const status=code==="LEARNER_NOT_FOUND"?404:500;
    return fail(res,status,code,"Unable to load learner progress.");
  }
}
