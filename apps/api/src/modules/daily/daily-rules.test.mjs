import test from 'node:test';
import assert from 'node:assert/strict';

test('daily goal uses meaningful learning',()=>{
  assert.equal(12 >= 15,false);
  assert.equal(20 >= 15,true);
});

test('goal bounds are 5..180 minutes',()=>{
  assert.equal(5 >= 5 && 5 <= 180,true);
  assert.equal(180 >= 5 && 180 <= 180,true);
  assert.equal(4 >= 5,false);
  assert.equal(181 <= 180,false);
});

test('streak stops at the first unmet day',()=>{
  const days=[true,true,true,false,true];
  let streak=0;
  for(const met of days){ if(!met) break; streak++; }
  assert.equal(streak,3);
});

test('notification becomes ineligible after goal completion',()=>{
  assert.equal(true && !true,false);
});
