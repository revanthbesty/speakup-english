import test from "node:test";
import assert from "node:assert/strict";
import { evaluateSentence } from "../src/modules/activities/mvp-activities.js";

test("canonical sentence is correct", () => {
  const result=evaluateSentence(["I","eat","rice","for","breakfast"]);
  assert.equal(result.outcome,"CORRECT");
  assert.equal(result.score,1);
});

test("wrong order receives actionable retry feedback", () => {
  const result=evaluateSentence(["I","rice","eat","for","breakfast"]);
  assert.notEqual(result.outcome,"CORRECT");
  assert.match(result.feedback.headline,/Good attempt|Good try/);
  assert.ok(result.feedback.retryHint);
});

test("empty input gets a supportive starting hint", () => {
  const result=evaluateSentence([]);
  assert.equal(result.outcome,"INCORRECT");
  assert.match(result.feedback.retryHint,"I");
});
