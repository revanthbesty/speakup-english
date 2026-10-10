import test from "node:test";
import assert from "node:assert/strict";

test("MVP journey starts with the canonical Breakfast lesson", () => {
  const journey = {
    world: "EVERYDAY_ENGLISH",
    unit: "FOOD_MEALS",
    lesson: "BREAKFAST"
  };
  assert.equal(journey.world, "EVERYDAY_ENGLISH");
  assert.equal(journey.unit, "FOOD_MEALS");
  assert.equal(journey.lesson, "BREAKFAST");
});

test("MVP activity order is verb, sentence, speaking", () => {
  const types = ["VERB_INTRODUCTION","SENTENCE_BUILDER","SPEAKING"];
  assert.deepEqual(types, ["VERB_INTRODUCTION","SENTENCE_BUILDER","SPEAKING"]);
});
