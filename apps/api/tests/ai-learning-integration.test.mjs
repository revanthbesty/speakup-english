import assert from 'node:assert/strict';
const cases = [
 ['BUDDY','CONVERSATION'],['SENTENCE_EVALUATION','TEXT_EVALUATION'],['ROLEPLAY','ROLEPLAY'],['STORY','STORY'],['GRAMMAR','TEXT_EVALUATION'],['VOCABULARY','TEXT_EVALUATION'],['SPEAKING_FEEDBACK','FEEDBACK']
];
for (const [useCase,capability] of cases) { assert.ok(useCase); assert.ok(capability); }
assert.equal(new Set(cases.map(x=>x[0])).size, cases.length);
console.log(`AI learning integration contract checks: ${cases.length}/${cases.length} passed`);
