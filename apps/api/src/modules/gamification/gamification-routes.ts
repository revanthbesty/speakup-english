import type {IncomingMessage,ServerResponse} from "node:http";
import {getAuthenticatedUser} from "../../security/session.js";
import {getGamification,getXpHistory} from "./gamification-service.js";

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

export async function handleGamificationRoutes(req:IncomingMessage,res:ServerResponse,pathname:string){
  if(!pathname.startsWith("/api/v1/gamification")) return false;
  const value=token(req);
  if(!value){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(value);
  if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}

  try{
    if(req.method==="GET" && pathname==="/api/v1/gamification")
      return send(res,200,await getGamification(user.learner_id));
    if(req.method==="GET" && pathname==="/api/v1/gamification/xp")
      return send(res,200,await getXpHistory(user.learner_id));
    return fail(res,404,"NOT_FOUND","Gamification route not found.");
  }catch{
    return fail(res,500,"GAMIFICATION_UNAVAILABLE","Unable to load gamification data.");
  }
}
