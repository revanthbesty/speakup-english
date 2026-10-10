import test from "node:test";
import assert from "node:assert/strict";
import {
  evaluateSpeakingTranscript,
  lowConfidenceSpeakingResult
} from "../src/modules/speaking/speaking-evaluator.js";

test("clear canonical speech is evaluated positively", () => {
  const result=evaluateSpeakingTranscript("I eat rice for breakfast.");
  assert.equal(result.outcome,"CORRECT");
  assert.equal(result.status,"COMPLETED");
  assert.equal(result.dimensions.sentenceConstruction,1);
  assert.ok(result.dimensions.contextualRelevance > .9);
  assert.ok(result.evidence.length>0);
});

test("partial speech produces actionable guidance", () => {
  const result=evaluateSpeakingTranscript("I eat breakfast.");
  assert.equal(result.outcome,"PARTIAL");
  assert.equal(result.feedback.priority,"MEDIUM");
  assert.ok(result.feedback.retryHint);
});

test("low STT confidence becomes neutral rather than learner failure", () => {
  const result=lowConfidenceSpeakingResult();
  assert.equal(result.outcome,"NEUTRAL");
  assert.equal(result.status,"LOW_CONFIDENCE");
  assert.equal(result.evidence.length,0);
});
