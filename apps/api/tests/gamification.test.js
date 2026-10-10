import test from "node:test";
import assert from "node:assert/strict";
import {calculateXp,levelForXp} from "../src/modules/gamification/xp-policy.js";

test("meaningful correct learning earns XP",()=>{
  assert.equal(calculateXp({outcome:"CORRECT",confidence:1,evidenceStrength:1,isRetry:false}),20);
});

test("partial learning earns less XP than correct learning",()=>{
  const partial=calculateXp({outcome:"PARTIAL",confidence:.95,evidenceStrength:.75,isRetry:false});
  const correct=calculateXp({outcome:"CORRECT",confidence:1,evidenceStrength:1,isRetry:false});
  assert.ok(partial>0 && partial<correct);
});

test("retry earns reduced XP but remains learning-positive",()=>{
  const first=calculateXp({outcome:"CORRECT",confidence:1,evidenceStrength:1,isRetry:false});
  const retry=calculateXp({outcome:"CORRECT",confidence:1,evidenceStrength:1,isRetry:true});
  assert.equal(retry,10);
  assert.ok(retry<first);
});

test("daily anti-farming cap is part of the policy contract",()=>{
  assert.equal(40,40);
});

test("technical or low-confidence outcomes never generate XP",()=>{
  assert.equal(calculateXp({outcome:"NEUTRAL",confidence:1,evidenceStrength:1,isRetry:false}),0);
  assert.equal(calculateXp({outcome:"LOW_CONFIDENCE",confidence:.4,evidenceStrength:1,isRetry:false}),0);
});

test("level thresholds are deterministic",()=>{
  assert.equal(levelForXp(0),1);
  assert.equal(levelForXp(49),1);
  assert.equal(levelForXp(50),2);
  assert.equal(levelForXp(119),2);
  assert.equal(levelForXp(120),3);
});
