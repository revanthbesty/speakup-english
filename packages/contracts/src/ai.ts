export type AICapability = "TEXT_EVALUATION" | "CONVERSATION" | "FEEDBACK" | "ROLEPLAY" | "STORY" | "TRANSLATION";
export type AIStatus = "SUCCESS" | "FALLBACK" | "TIMEOUT" | "PROVIDER_ERROR" | "INVALID_RESPONSE" | "SAFETY_BLOCK";

export interface AIRequest {
  purpose: string;
  capability: AICapability;
  learnerId?: string;
  systemContext: string;
  learnerInput: string;
  outputSchema: string;
  temperature?: number;
  maxTokens?: number;
}

export interface StructuredAIResult<T = unknown> {
  status: AIStatus;
  output: T | null;
  provider: string;
  model: string;
  latencyMs: number;
  fallbackUsed: boolean;
  safetyStatus: "ALLOWED" | "BLOCKED";
  usage: { inputTokens: number; outputTokens: number };
  errorCode?: string;
}

export interface EvaluationRequest {
  activityId: string;
  attemptId: string;
  learnerInput: string;
  expectedPattern?: string;
  scenario?: MockAIScenario;
}

export interface EvaluationResult {
  outcome: "CORRECT" | "INCORRECT" | "RETRYABLE" | "SAFETY_BLOCK";
  score: number | null;
  confidence: number;
  feedback: string;
  provenance: { mode: "AI" | "MOCK" | "DETERMINISTIC"; provider: string; model: string };
}

export type MockAIScenario = "CORRECT" | "INCORRECT" | "PROVIDER_ERROR" | "TIMEOUT" | "SAFETY_BLOCK" | "INVALID_RESPONSE" | "LOW_CONFIDENCE";
