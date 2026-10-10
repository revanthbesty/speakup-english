export type SpeechCapability = "STT" | "TTS" | "PRONUNCIATION" | "FLUENCY";
export interface STTResult { transcript:string; confidence:number; language:string; durationMs:number; provider:string; model:string; }
export interface TTSResult { audioUrl:string|null; audioMimeType:string; durationMs:number|null; provider:string; model:string; }
export interface PronunciationResult { score:number; confidence:number; phonemeErrors:string[]; wordErrors:string[]; provider:string; model:string; }
export interface FluencyResult { score:number; confidence:number; wordsPerMinute:number|null; pauseRatio:number|null; fillerCount:number; provider:string; model:string; }
export interface SpeechProviderHealth { provider:string; capabilities:SpeechCapability[]; available:boolean; failureCount:number; }
