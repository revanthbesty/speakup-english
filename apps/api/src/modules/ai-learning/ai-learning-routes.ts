import type { IncomingMessage, ServerResponse } from "node:http";
import { runLearningAI } from "./ai-learning-service.js";
function send(res:ServerResponse,status:number,data:unknown){res.statusCode=status;res.setHeader("content-type","application/json");res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));}
async function body(req:IncomingMessage){let raw="";for await(const c of req)raw+=c;return raw?JSON.parse(raw):{};}
export async function handleLearningAIRoute(req:IncomingMessage,res:ServerResponse,path:string){
 if(req.method!=="POST"||path!=="/api/v1/ai/learning") return false;
 const input=await body(req);
 const result=await runLearningAI(input);
 return send(res,result.status==="SAFETY_BLOCK"?422:200,result);
}
