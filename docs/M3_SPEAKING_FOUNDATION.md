# M3 — Speaking Foundation

## Purpose

Introduce speaking as a first-class activity without coupling the learning engine to a speech provider.

## Speaking pipeline

```text
Learner
  ↓
Speaking Activity
  ↓
Microphone Permission
  ↓
Audio Capture
  ↓
Temporary Upload
  ↓
STT
  ↓
Transcript + Confidence
  ↓
Speaking Evaluation
  ↓
Feedback
  ↓
Retry
```

## Client states

### Microphone

```text
UNKNOWN → REQUESTING → GRANTED
                    ↘ DENIED
                    ↘ BLOCKED
                    ↘ UNAVAILABLE
```

### Audio

```text
IDLE → PREPARING → RECORDING → STOPPING
     → UPLOADING → PROCESSING → COMPLETE
                              ↘ FAILED
```

## Privacy

Raw audio is **not retained by default**.

The current MVP stores a temporary object key only while processing. Once STT succeeds, the processing record clears that key and explicitly keeps `audio_retained=false`.

A production object-storage adapter must enforce temporary lifecycle deletion as well; the current provider is still a mock.

## STT abstraction

The learning system depends on:

```text
SpeechProvider.transcribe(...)
```

not on a particular cloud/vendor API.

The current implementation uses `MockSttProvider`.

## Technical failure boundary

STT timeout/provider failure is represented as a technical failure and retryable state where appropriate. It is never converted into an English-learning error.

## API

```text
POST /api/v1/speaking/attempts
POST /api/v1/speaking/attempts/{id}/start
POST /api/v1/speaking/attempts/{id}/submit
```

## MVP speaking prompt

> What do you eat for breakfast?

Expected learning context:

```text
I eat rice for breakfast.
```

The learner's spoken response will next be evaluated for sentence construction, grammar, vocabulary, completeness and contextual relevance.

## Next

Connect the transcript to the formal AI Gateway / speaking evaluator, then route its result through Feedback → Retry.
