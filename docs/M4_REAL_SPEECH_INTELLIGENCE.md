# M4 Real Speech Intelligence

## Scope
Completes the speaking loop with normalized STT, pronunciation and fluency signals, confidence-aware interpretation, retry-safe feedback, evidence-compatible evaluation, and speaking-learning metrics.

## Safety / learning rules
- STT confidence below 0.55 produces a neutral result and no learner evidence.
- Provider failure is a technical failure and remains retryable.
- Pronunciation and fluency are supporting signals; they do not independently mark a learner wrong.
- Raw audio is not retained by default and temporary storage keys are cleared after processing.
- Speaking metrics distinguish speaking time, sentence attempts, improvement, and meaningful learning.

## Flow
Speaking capture -> STT -> confidence gate -> pronunciation + fluency -> normalized speech intelligence -> speaking evaluator -> feedback/retry -> evidence/mastery pipeline.
