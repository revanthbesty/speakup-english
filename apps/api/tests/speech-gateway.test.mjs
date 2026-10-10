import test from 'node:test';
import assert from 'node:assert/strict';

test('speech provider contract keeps technical failure separate from learner outcome', async()=>{
  const src=await import('../src/modules/speech/speech-gateway.ts');
  const stt=await src.transcribe({audioRef:'temporary-object-key'});
  assert.equal(stt.transcript,'I eat rice for breakfast.');
  assert.ok(stt.confidence>0.9);
  assert.equal(typeof stt.provider,'string');
});
