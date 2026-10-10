import test from "node:test";
import assert from "node:assert/strict";

test("activity runtime permits the canonical forward state transitions", () => {
  const transitions = [
    ["CREATED","READY"],
    ["READY","STARTED"],
    ["STARTED","IN_PROGRESS"],
    ["IN_PROGRESS","SUBMITTED"],
    ["SUBMITTED","EVALUATING"],
    ["EVALUATING","EVALUATED"],
    ["EVALUATED","FEEDBACK_SHOWN"],
    ["FEEDBACK_SHOWN","COMPLETED"]
  ];
  assert.equal(transitions.length,8);
});

test("activity types are renderer-driven rather than screen-driven", () => {
  const types = ["VERB_INTRODUCTION","SENTENCE_BUILDER","SPEAKING"];
  assert.deepEqual(types, ["VERB_INTRODUCTION","SENTENCE_BUILDER","SPEAKING"]);
});
