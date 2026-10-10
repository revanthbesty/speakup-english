import type {IncomingMessage,ServerResponse} from "node:http";
import {getAuthenticatedUser} from "../../security/session.js";
import {MockSttProvider} from "./mock-stt-provider.js";
import {evaluateSpeakingAttempt} from "./speaking-evaluation-service.js";
import {
  createSpeakingAttempt,
  markRecordingStarted,
  submitAudioForProcessing
} from "./speaking-service.js";

async function body(req:IncomingMessage){
  let raw="";for await(const c of req)raw+=c;
  return raw?JSON.parse(raw):{};
}
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

export async function handleSpeakingRoutes(
  req:IncomingMessage,res:ServerResponse,pathname:string
){
  if(!pathname.startsWith("/api/v1/speaking"))return false;

  const value=token(req);
  if(!value){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}
  const user=await getAuthenticatedUser(value);
  if(!user){fail(res,401,"UNAUTHENTICATED","Authentication is required.");return true;}

  try{
    const create=pathname.match(/^\/api\/v1\/speaking\/attempts$/);
    if(req.method==="POST" && create){
      const input=await body(req);
      return send(res,201,await createSpeakingAttempt(
        user.learner_id,input.activityAttemptId
      ));
    }

    const start=pathname.match(/^\/api\/v1\/speaking\/attempts\/([^/]+)\/start$/);
    if(req.method==="POST" && start){
      return send(res,200,await markRecordingStarted(
        user.learner_id,start[1]!
      ));
    }

    const submit=pathname.match(/^\/api\/v1\/speaking\/attempts\/([^/]+)\/submit$/);
    if(req.method==="POST" && submit){
      const input=await body(req);
      return send(res,202,await submitAudioForProcessing(
        user.learner_id,
        submit[1]!,
        input.temporaryObjectKey,
        Number(input.durationMs),
        new MockSttProvider()
      ));
    }

    const evaluate=pathname.match(/^\/api\/v1\/speaking\/attempts\/([^/]+)\/evaluate$/);
    if(req.method==="POST" && evaluate){
      return send(res,202,await evaluateSpeakingAttempt(
        user.learner_id,evaluate[1]!
      ));
    }

    return fail(res,404,"NOT_FOUND","Speaking route not found.");
  }catch(error){
    const code=error instanceof Error?error.message:"INTERNAL_ERROR";
    const known:Record<string,[number,string]>={
      ATTEMPT_NOT_FOUND:[404,"Activity attempt not found."],
      SPEAKING_ATTEMPT_NOT_FOUND:[404,"Speaking attempt not found."],
      SPEAKING_STATE_INVALID:[409,"The speaking attempt is not ready for that action."],
      EMPTY_AUDIO:[422,"No usable speech was detected in the recording."],
      AUDIO_TOO_LONG:[422,"The recording is longer than the allowed limit."],
      TRANSCRIPT_NOT_AVAILABLE:[409,"Speech recognition has not produced a transcript yet."]
    };
    const [status,message]=known[code]??[500,"Unable to process the speaking attempt."];
    fail(res,status,code,message);return true;
  }
}
