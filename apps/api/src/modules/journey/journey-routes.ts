import type { IncomingMessage, ServerResponse } from "node:http";
import { getAuthenticatedUser } from "../../security/session.js";
import { initializeJourney, getJourney, startLesson } from "./journey-service.js";

function token(req: IncomingMessage) {
  const v=req.headers.authorization;
  return v?.startsWith("Bearer ") ? v.slice(7) : null;
}
function send(res: ServerResponse,status:number,data:unknown) {
  res.statusCode=status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));
}
function fail(res:ServerResponse,status:number,code:string,message:string) {
  res.statusCode=status;
  res.setHeader("content-type","application/json");
  res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));
}
export async function handleJourneyRoutes(req:IncomingMessage,res:ServerResponse,pathname:string) {
  if (!pathname.startsWith("/api/v1/journey")) return false;
  const t=token(req);
  if(!t){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(t);
  if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}

  try {
    if(req.method==="POST" && pathname==="/api/v1/journey/initialize")
      return send(res,201,await initializeJourney(user.learner_id));

    if(req.method==="GET" && pathname==="/api/v1/journey")
      return send(res,200,await getJourney(user.learner_id));

    const match=pathname.match(/^\/api\/v1\/journey\/lessons\/([^/]+)\/start$/);
    if(req.method==="POST" && match)
      return send(res,200,await startLesson(user.learner_id,match[1]!));

    return fail(res,404,"NOT_FOUND","Journey route not found.");
  } catch(e) {
    const code=e instanceof Error?e.message:"INTERNAL_ERROR";
    const known:Record<string,[number,string]>={
      PLACEMENT_NOT_CONFIRMED:[409,"Confirm placement before starting the learning journey."],
      MVP_CONTENT_NOT_FOUND:[503,"The initial learning content is not available."],
      JOURNEY_NOT_INITIALIZED:[404,"Learning journey has not been initialized."],
      LESSON_NOT_FOUND:[404,"Lesson not found."]
    };
    const [status,message]=known[code]??[500,"Unable to process the journey request."];
    fail(res,status,code,message);
    return true;
  }
}
