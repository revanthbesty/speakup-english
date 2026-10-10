import test from "node:test";
import assert from "node:assert/strict";
import { buildRetryPlan } from "../src/modules/activities/feedback-retry.js";

const base = {
  id:"e1",
  attempt_id:"a1",
  score:0.5,
  confidence:0.95,
  result:{headline:"Good attempt.",explanation:"Check the word order.",retryHint:"Try again."}
};

test("correct result continues without retry", () => {
  const plan=buildRetryPlan({...base,outcome:"CORRECT"});
  assert.equal(plan.action,"CONTINUE");
  assert.equal(plan.retryAllowed,false);
  assert.equal(plan.supportLevel,"NONE");
});

test("partial result receives medium support", () => {
  const plan=buildRetryPlan({...base,outcome:"PARTIAL"});
  assert.equal(plan.action,"RETRY");
  assert.equal(plan.retryAllowed,true);
  assert.equal(plan.supportLevel,"MEDIUM");
});

test("incorrect result receives high support", () => {
  const plan=buildRetryPlan({...base,outcome:"INCORRECT"});
  assert.equal(plan.action,"RETRY");
  assert.equal(plan.retryAllowed,true);
  assert.equal(plan.supportLevel,"HIGH");
});
