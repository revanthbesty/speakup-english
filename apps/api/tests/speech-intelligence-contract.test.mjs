import assert from 'node:assert/strict';
import { combineSpeechSignals, lowConfidenceSpeakingResult, evaluateSpeakingTranscript } from '../src/modules/speaking/speaking-evaluator.js';

const low=lowConfidenceSpeakingResult();
assert.equal(low.outcome,'NEUTRAL');
assert.equal(low.evidence.length,0);

const good=evaluateSpeakingTranscript('I eat rice for breakfast.');
const before=good.dimensions.sentenceConstruction;
combineSpeechSignals(good,.8,.7);
assert.ok(good.dimensions.sentenceConstruction < 1 && good.dimensions.sentenceConstruction > before*.75);
assert.ok(good.dimensions.completeness <= 1);

console.log('speech-intelligence-contract: 3/3 passed');
