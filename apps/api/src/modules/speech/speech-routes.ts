import type {IncomingMessage,ServerResponse} from "node:http";
import {transcribe,synthesize,pronunciation,fluency,speechProviderHealth} from "./speech-gateway.js";
function send(res:ServerResponse,status:number,data:unknown){res.statusCode=status;res.setHeader("content-type","application/json");res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}))}
async function body(req:IncomingMessage){let raw="";for await(const c of req)raw+=c;return raw?JSON.parse(raw):{}}
export async function handleSpeechRoute(req:IncomingMessage,res:ServerResponse,path:string){
 try{
  if(req.method==="GET"&&path==="/api/v1/admin/ai/speech/providers")return send(res,200,speechProviderHealth());
  if(req.method!=="POST")return false;const input=await body(req);
  if(path==="/api/v1/speech/transcribe")return send(res,200,await transcribe(input));
  if(path==="/api/v1/speech/synthesize")return send(res,200,await synthesize(input));
  if(path==="/api/v1/speech/pronunciation")return send(res,200,await pronunciation(input));
  if(path==="/api/v1/speech/fluency")return send(res,200,await fluency(input));
  return false;
 }catch(e){return send(res,503,{status:"TECHNICAL_FAILURE",errorCode:e instanceof Error?e.message:"SPEECH_PROVIDER_ERROR",learnerImpact:"NONE"})}
}
