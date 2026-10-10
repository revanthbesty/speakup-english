import { generateStructured } from "../ai/ai-gateway.js";
import type { AICapability } from "@speakup/contracts";

export type LearningAIUseCase = "BUDDY"|"SENTENCE_EVALUATION"|"ROLEPLAY"|"STORY"|"GRAMMAR"|"VOCABULARY"|"SPEAKING_FEEDBACK";

const capability: Record<LearningAIUseCase,AICapability> = {
  BUDDY:"CONVERSATION", SENTENCE_EVALUATION:"TEXT_EVALUATION", ROLEPLAY:"ROLEPLAY", STORY:"STORY",
  GRAMMAR:"TEXT_EVALUATION", VOCABULARY:"TEXT_EVALUATION", SPEAKING_FEEDBACK:"FEEDBACK"
};

const schemaVersion = "learning-ai.v1";

export async function runLearningAI(input:{useCase:LearningAIUseCase; learnerInput:string; context?:Record<string,unknown>; systemContext?:string; maxTokens?:number}) {
  const result = await generateStructured({
    capability: capability[input.useCase],
    purpose: input.useCase,
    learnerInput: input.learnerInput,
    systemContext: input.systemContext ?? "You are a supportive English learning coach. Be concise, age-appropriate, specific, encouraging, and never shame the learner.",
    outputSchema: JSON.stringify({ type:"object", schemaVersion, fields:["outcome","message","correction","followUp","priority","retryHint"] }),
    maxTokens: input.maxTokens ?? 450,
    temperature: 0.2
  });
  return { ...result, useCase: input.useCase, schemaVersion };
}
