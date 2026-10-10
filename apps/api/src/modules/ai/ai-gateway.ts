import type { AICapability, AIRequest, StructuredAIResult } from "@speakup/contracts";
import { config } from "../../config.js";

interface Provider { key:string; model:string; endpoint?:string; timeoutMs:number; capabilities:AICapability[]; priority:number; }
const providers: Provider[] = [
  { key:"speakup-mock", model:"foundation-v1", timeoutMs:config.aiTimeoutMs, capabilities:["TEXT_EVALUATION","CONVERSATION","FEEDBACK","ROLEPLAY","STORY","TRANSLATION"], priority:100 },
  ...(config.aiProviderEndpoint ? [{ key:config.aiProviderKey, model:config.aiModel, endpoint:config.aiProviderEndpoint, timeoutMs:config.aiTimeoutMs, capabilities:["TEXT_EVALUATION","CONVERSATION","FEEDBACK","ROLEPLAY","STORY","TRANSLATION"] as AICapability[], priority:10 }] : [])
];

const failures = new Map<string,{count:number; openedAt:number}>();
const blocked = /(self[- ]?harm|suicide|sexual exploitation|child sexual|terrorist recruitment)/i;

function mock(req:AIRequest): unknown {
  if(req.capability === "TEXT_EVALUATION") return { outcome:"PARTIAL", score:0.72, confidence:0.86, feedback:"Good attempt. Add a little more detail and try the sentence again.", priority:"DETAIL", retryHint:"Add what or where." };
  return { message:"Nice work. Keep speaking in complete sentences.", correction:null, followUp:"Can you tell me a little more?" };
}

function available(p:Provider){ const f=failures.get(p.key); return !f || Date.now()-f.openedAt>config.aiCircuitCooldownMs; }
function recordFailure(p:Provider){ const f=failures.get(p.key) ?? {count:0,openedAt:0}; f.count++; if(f.count>=config.aiFailureThreshold) f.openedAt=Date.now(); failures.set(p.key,f); }
function recordSuccess(p:Provider){ failures.delete(p.key); }

async function callHttp(p:Provider, req:AIRequest):Promise<unknown>{
  if(!p.endpoint) return mock(req);
  const controller=new AbortController(); const timer=setTimeout(()=>controller.abort(),p.timeoutMs);
  try {
    const response=await fetch(p.endpoint,{method:"POST",headers:{"content-type":"application/json", ...(config.aiProviderApiKey?{"authorization":`Bearer ${config.aiProviderApiKey}`}:{})},body:JSON.stringify({model:p.model,purpose:req.purpose,capability:req.capability,system:req.systemContext,input:req.learnerInput,outputSchema:req.outputSchema,temperature:req.temperature ?? 0.2,max_tokens:req.maxTokens ?? 500}),signal:controller.signal});
    if(!response.ok) throw new Error(`HTTP_${response.status}`);
    return await response.json();
  } finally { clearTimeout(timer); }
}

export async function generateStructured<T>(req:AIRequest):Promise<StructuredAIResult<T>>{
  if(blocked.test(req.learnerInput)) return {status:"SAFETY_BLOCK",output:null,provider:"safety",model:"policy",latencyMs:0,fallbackUsed:false,safetyStatus:"BLOCKED",usage:{inputTokens:0,outputTokens:0},errorCode:"SAFETY_BLOCK"};
  const start=Date.now(); const candidates=providers.filter(p=>p.capabilities.includes(req.capability)).sort((a,b)=>a.priority-b.priority);
  let lastError="AI_PROVIDER_ERROR"; let fallback=false;
  for(const p of candidates){
    if(!available(p)) { fallback=true; continue; }
    try {
      const output=await callHttp(p,req); recordSuccess(p);
      if(!output || typeof output!=="object") throw new Error("INVALID_RESPONSE");
      return {status:fallback?"FALLBACK":"SUCCESS",output:output as T,provider:p.key,model:p.model,latencyMs:Date.now()-start,fallbackUsed:fallback,safetyStatus:"ALLOWED",usage:{inputTokens:0,outputTokens:0}};
    } catch(e){ lastError=e instanceof Error?e.message:"AI_PROVIDER_ERROR"; recordFailure(p); fallback=true; }
  }
  return {status:"PROVIDER_ERROR",output:null,provider:"none",model:"none",latencyMs:Date.now()-start,fallbackUsed:fallback,safetyStatus:"ALLOWED",usage:{inputTokens:0,outputTokens:0},errorCode:lastError};
}

export function aiProviderHealth(){ return providers.map(p=>({provider:p.key,model:p.model,enabled:true,available:available(p),failureCount:failures.get(p.key)?.count ?? 0})); }
