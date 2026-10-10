const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');

const root = path.resolve(__dirname, '..');
const service = fs.readFileSync(path.join(root, 'src/modules/progress/progress-service.ts'), 'utf8');
const routes = fs.readFileSync(path.join(root, 'src/modules/progress/progress-routes.ts'), 'utf8');
const evidence = fs.readFileSync(path.join(root, 'src/modules/evidence/evidence-routes.ts'), 'utf8');
const mvp = fs.readFileSync(path.join(root, 'src/modules/activities/mvp-activities.ts'), 'utf8');

test('progress exposes Home, progress and journey contracts', () => {
  assert.match(routes, /\/api\/v1\/home/);
  assert.match(routes, /\/api\/v1\/progress/);
  assert.match(routes, /\/api\/v1\/progress\/journey/);
  assert.match(service, /getProgressSnapshot/);
  assert.match(service, /getJourneyProgress/);
});

test('progress is evidence-driven', () => {
  assert.match(service, /exists\(select 1 from learning_evidence/);
  assert.match(service, /percentComplete/);
  assert.match(service, /nextActivity/);
});

test('evidence synchronizes lesson progress', () => {
  assert.match(evidence, /syncLessonProgressFromEvidence/);
  assert.match(evidence, /const progress=/);
});

test('deterministic evaluation persists its evidence mapping', () => {
  assert.match(mvp, /evidence:evaluation\.evidence/);
});
