import type {IncomingMessage,ServerResponse} from "node:http";
import {getAuthenticatedUser} from "../../security/session.js";
import {getDailyProgress,getStreak,setDailyGoal,setNotificationPreferences,getNotificationEligibility} from "./daily-service.js";
function token(req:IncomingMessage){const v=req.headers.authorization;return v?.startsWith("Bearer ")?v.slice(7):null;}
function send(res:ServerResponse,status:number,data:unknown){res.statusCode=status;res.setHeader("content-type","application/json");res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));}
function fail(res:ServerResponse,status:number,code:string,message:string){res.statusCode=status;res.setHeader("content-type","application/json");res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));}
async function read(req:IncomingMessage){let raw="";for await(const c of req)raw+=c;return raw?JSON.parse(raw):{};}
export async function handleDailyRoutes(req:IncomingMessage,res:ServerResponse,pathname:string){
  if(!pathname.startsWith("/api/v1/daily")&&!pathname.startsWith("/api/v1/notifications")) return false;
  const t=token(req);if(!t){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(t);if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  try{
    if(req.method==="GET"&&pathname==="/api/v1/daily") return send(res,200,await getDailyProgress(user.learner_id));
    if(req.method==="GET"&&pathname==="/api/v1/daily/streak") return send(res,200,await getStreak(user.learner_id));
    if(req.method==="PATCH"&&pathname==="/api/v1/daily/goal"){const b=await read(req);return send(res,200,await setDailyGoal(user.learner_id,Number(b.minutes)));}
    if(req.method==="GET"&&pathname==="/api/v1/notifications/preferences") return send(res,200,(await getDailyProgress(user.learner_id,1)).goal);
    if(req.method==="PATCH"&&pathname==="/api/v1/notifications/preferences") return send(res,200,await setNotificationPreferences(user.learner_id,await read(req)));
    if(req.method==="GET"&&pathname==="/api/v1/notifications/daily-goal/eligibility") return send(res,200,await getNotificationEligibility(user.learner_id));
    return fail(res,404,"NOT_FOUND","Daily or notification route not found.");
  }catch(e){const code=e instanceof Error?e.message:"INTERNAL_ERROR";const map:Record<string,[number,string]>={LEARNER_NOT_FOUND:[404,"Learner not found."],INVALID_DAILY_GOAL:[422,"Daily goal must be between 5 and 180 minutes."],INVALID_TIMEZONE:[422,"Enter a valid IANA timezone."],INVALID_REMINDER_TIME:[422,"Reminder time must use HH:MM format."]};const [s,m]=map[code]??[500,"Unable to process daily learning data."];fail(res,s,code,m);return true;}
}
