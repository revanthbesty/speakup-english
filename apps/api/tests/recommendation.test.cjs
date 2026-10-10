const assert=require('node:assert/strict');
function update(d,outcome){let x=d;if(outcome==='CORRECT')x=Math.min(1,x+0.05);if(outcome==='PARTIAL')x=Math.max(0,x-0.02);if(outcome==='INCORRECT')x=Math.max(0,x-0.08);return x;}
assert.equal(update(.5,'CORRECT'),.55);assert.equal(update(.5,'PARTIAL'),.48);assert.equal(update(.5,'INCORRECT'),.42);assert.equal(update(0,'INCORRECT'),0);assert.equal(update(1,'CORRECT'),1);console.log('recommendation/adaptive focused checks: 5/5 passed');
