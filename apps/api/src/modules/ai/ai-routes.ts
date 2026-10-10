import type { IncomingMessage, ServerResponse } from "node:http";
import { aiProviderHealth, generateStructured } from "./ai-gateway.js";

function send(res:ServerResponse,status:number,data:unknown){res.statusCode=status;res.setHeader("content-type","application/json");res.end(JSON.stringify({data,meta:{requestId:crypto.randomUUID()}}));}
async function body(req:IncomingMessage){let raw="";for await(const c of req)raw+=c;return raw?JSON.parse(raw):{};}

export async function handleAIRoute(req:IncomingMessage,res:ServerResponse,path:string){
  if(req.method==="GET" && path==="/api/v1/admin/ai/providers") return send(res,200,aiProviderHealth());
  if(req.method==="POST" && path==="/api/v1/ai/generate"){
    const input=await body(req);
    const result=await generateStructured(input);
    return send(res,result.status==="SAFETY_BLOCK"?422:200,result);
  }
  return false;
}
