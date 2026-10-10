import type {IncomingMessage,ServerResponse} from "node:http";
import {getAuthenticatedUser} from "../../security/session.js";
import {getRecommendation,getAdaptiveDifficulty} from "./recommendation-service.js";
function token(req:IncomingMessage){const v=req.headers.authorization;return v?.startsWith('Bearer ')?v.slice(7):null;}
function send(res:ServerResponse,status:number,data:unknown){res.statusCode=status;res.setHeader('content-type','application/json');res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));}
function fail(res:ServerResponse,status:number,code:string,message:string){res.statusCode=status;res.setHeader('content-type','application/json');res.end(JSON.stringify({error:{code,message,requestId:crypto.randomUUID()}}));}
export async function handleRecommendationRoutes(req:IncomingMessage,res:ServerResponse,pathname:string){
  if(!pathname.startsWith('/api/v1/recommendations')&&!pathname.startsWith('/api/v1/adaptive-difficulty'))return false;
  const t=token(req);if(!t){fail(res,401,'UNAUTHENTICATED','Authentication is required.');return true;}
  const user=await getAuthenticatedUser(t);if(!user){fail(res,401,'UNAUTHENTICATED','Authentication is required.');return true;}
  if(req.method!=='GET'){fail(res,405,'METHOD_NOT_ALLOWED','Only GET is supported.');return true;}
  try{
    if(pathname==='/api/v1/recommendations/next')return send(res,200,await getRecommendation(user.learner_id));
    if(pathname==='/api/v1/adaptive-difficulty')return send(res,200,await getAdaptiveDifficulty(user.learner_id));
    return fail(res,404,'NOT_FOUND','Recommendation route not found.');
  }catch(error){const code=error instanceof Error?error.message:'INTERNAL_ERROR';return fail(res,500,code,'Unable to load recommendation.');}
}
