# M4 AI Learning Integration

All learner-facing AI use cases now have a single application boundary and must call the AI Gateway rather than a provider directly.

Use cases: Buddy, sentence evaluation, role play, stories, grammar, vocabulary, speaking feedback.

Rules:
- Provider/model selection stays inside the AI Gateway.
- Structured output is required.
- Safety is evaluated before learner-facing output.
- AI failure is technical failure, never learner failure.
- AI output does not directly mutate XP, mastery, streaks, proficiency, or entitlements.
- Evidence is created only by the existing learning/evaluation pipeline.
- Schema version is recorded for reproducibility.
- Raw learner audio remains outside this text-AI integration and is not retained by default.
