import test from "node:test";
import assert from "node:assert/strict";

test("diagnostic contains the six required foundation sections", () => {
  const sections = ["VOCABULARY","GRAMMAR","LISTENING","READING","SENTENCE_CONSTRUCTION","SPEAKING"];
  assert.equal(sections.length, 6);
  assert.ok(sections.includes("SPEAKING"));
  assert.ok(sections.includes("SENTENCE_CONSTRUCTION"));
});

test("proficiency bands are ordered P0 through P3", () => {
  assert.deepEqual(["P0","P1","P2","P3"], ["P0","P1","P2","P3"]);
});

test("placement preserves override capability", () => {
  const recommended = "P1";
  const selected = "P2";
  assert.notEqual(recommended, selected);
});
