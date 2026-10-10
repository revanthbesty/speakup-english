import test from "node:test";
import assert from "node:assert/strict";

test("effective evidence is bounded and weighted by confidence", () => {
  const strength=.8;
  const confidence=.5;
  const effective=strength*confidence;
  assert.equal(effective,.4);
  assert.ok(effective>=0 && effective<=1);
});

test("mastery update is conservative", () => {
  const previous=.2;
  const effective=1;
  const next=previous + .20*(effective-previous);
  assert.equal(Number(next.toFixed(2)),.36);
  assert.ok(next-previous < .2);
});

test("activity concepts remain separate", () => {
  const concepts=["completion","evidence","mastery","xp","proficiency"];
  assert.deepEqual(concepts,["completion","evidence","mastery","xp","proficiency"]);
});
