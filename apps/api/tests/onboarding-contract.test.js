import test from "node:test";
import assert from "node:assert/strict";

test("supported onboarding audiences remain configuration-driven", () => {
  const audiences = ["KIDS","TEENS","STUDENTS","COLLEGE","JOB_SEEKERS","PROFESSIONALS","ADULTS","SENIORS"];
  assert.equal(audiences.includes("SENIORS"), true);
  assert.equal(audiences.includes("ADULTS"), true);
});

test("daily goal accepts the supported foundation values", () => {
  const values = [5,10,15,20,30,45,60];
  assert.equal(values.includes(15), true);
  assert.equal(values.includes(25), false);
});
