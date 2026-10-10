import type { EvaluationRequest, EvaluationResult, MockAIScenario } from "@speakup/contracts/ai";

export class MockAIGateway {
  async evaluate(request: EvaluationRequest): Promise<EvaluationResult> {
    const scenario: MockAIScenario = request.scenario ?? "CORRECT";
    if (scenario === "PROVIDER_ERROR" || scenario === "TIMEOUT") {
      return {
        outcome: "RETRYABLE", score: null, confidence: 0,
        feedback: "The AI evaluator is temporarily unavailable. Please try again.",
        provenance: { mode: "MOCK", provider: "speakup-mock", model: "foundation-v1" }
      };
    }
    if (scenario === "SAFETY_BLOCK") {
      return {
        outcome: "SAFETY_BLOCK", score: null, confidence: 1,
        feedback: "This response cannot be evaluated in the current learning context.",
        provenance: { mode: "MOCK", provider: "speakup-mock", model: "foundation-v1" }
      };
    }
    if (scenario === "INVALID_RESPONSE") {
      return {
        outcome: "RETRYABLE", score: null, confidence: 0,
        feedback: "The evaluator returned an invalid result. Please retry.",
        provenance: { mode: "MOCK", provider: "speakup-mock", model: "foundation-v1" }
      };
    }
    if (scenario === "LOW_CONFIDENCE") {
      return {
        outcome: "RETRYABLE", score: null, confidence: 0.42,
        feedback: "I’m not fully confident about this answer yet. Try once more.",
        provenance: { mode: "MOCK", provider: "speakup-mock", model: "foundation-v1" }
      };
    }
    if (scenario === "INCORRECT") {
      return {
        outcome: "INCORRECT", score: 0.35, confidence: 0.98,
        feedback: "Try building the sentence with the target action and a clear object.",
        provenance: { mode: "MOCK", provider: "speakup-mock", model: "foundation-v1" }
      };
    }
    return {
      outcome: "CORRECT", score: 1, confidence: 0.99,
      feedback: "Great! Your sentence communicates the idea clearly.",
      provenance: { mode: "MOCK", provider: "speakup-mock", model: "foundation-v1" }
    };
  }
}
