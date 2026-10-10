import type { SpeechCapability, STTResult, TTSResult, PronunciationResult, FluencyResult } from "@speakup/ai-contracts";
import { config } from "../../config.js";

type Provider={key:string;model:string;endpoint?:string;capabilities:SpeechCapability[];priority:number};
const providers:Provider[]=[
 {key:"speakup-mock-speech",model:"speech-foundation-v1",capabilities:["STT","TTS","PRONUNCIATION","FLUENCY"],priority:100},
 ...(config.aiProviderEndpoint?[{key:config.aiProviderKey,model:config.aiModel,endpoint:config.aiProviderEndpoint,capabilities:["STT","TTS","PRONUNCIATION","FLUENCY"] as SpeechCapability[],priority:10}]:[])
];
const failures=new Map<string,{count:number;openedAt:number}>();
const available=(p:Provider)=>{const f=failures.get(p.key);return !f||Date.now()-f.openedAt>config.aiCircuitCooldownMs};
const fail=(p:Provider)=>{const f=failures.get(p.key)||{count:0,openedAt:0};f.count++;if(f.count>=config.aiFailureThreshold)f.openedAt=Date.now();failures.set(p.key,f)};
const ok=(p:Provider)=>failures.delete(p.key);
async function call(p:Provider,capability:SpeechCapability,input:unknown){
 if(!p.endpoint){
  if(capability==="STT")return {transcript:"I eat rice for breakfast.",confidence:.94,language:"en-IN",durationMs:2600};
  if(capability==="TTS")return {audioUrl:null,audioMimeType:"audio/mpeg",durationMs:null};
  if(capability==="PRONUNCIATION")return {score:.86,confidence:.82,phonemeErrors:[],wordErrors:[]};
  return {score:.81,confidence:.84,wordsPerMinute:92,pauseRatio:.18,fillerCount:1};
 }
 const c=new AbortController();const t=setTimeout(()=>c.abort(),config.aiTimeoutMs);
 try{const r=await fetch(p.endpoint,{method:"POST",headers:{"content-type":"application/json",...(config.aiProviderApiKey?{authorization:`Bearer ${config.aiProviderApiKey}`}:{})},body:JSON.stringify({model:p.model,capability,input}),signal:c.signal});if(!r.ok)throw new Error(`HTTP_${r.status}`);return await r.json();}finally{clearTimeout(t)}
}
async function run<T>(capability:SpeechCapability,input:unknown):Promise<T&{provider:string;model:string;fallbackUsed:boolean}>{let fallback=false;for(const p of providers.filter(x=>x.capabilities.includes(capability)).sort((a,b)=>a.priority-b.priority)){if(!available(p)){fallback=true;continue}try{const out=await call(p,capability,input);if(!out||typeof out!=="object")throw new Error("INVALID_RESPONSE");ok(p);return {...out,provider:p.key,model:p.model,fallbackUsed:fallback} as T&{provider:string;model:string;fallbackUsed:boolean}}catch(e){fail(p);fallback=true}}throw new Error("SPEECH_PROVIDER_UNAVAILABLE")}
export const transcribe=(input:unknown)=>run<STTResult>("STT",input);
export const synthesize=(input:unknown)=>run<TTSResult>("TTS",input);
export const pronunciation=(input:unknown)=>run<PronunciationResult>("PRONUNCIATION",input);
export const fluency=(input:unknown)=>run<FluencyResult>("FLUENCY",input);
export function speechProviderHealth(){return providers.map(p=>({provider:p.key,model:p.model,capabilities:p.capabilities,available:available(p),failureCount:failures.get(p.key)?.count??0}))}
