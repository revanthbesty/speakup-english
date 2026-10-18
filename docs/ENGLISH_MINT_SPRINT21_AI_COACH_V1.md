# English Mint Sprint 21 — AI Coach V1: Safe Rule-Based Personalization

## Source
Recovered original `English_Mint_Sprint19_Plus_Master_SRS_Designs_AI_Prompt.docx`, Sprint 21: **AI Coach V1** with rule-based personalization, optional AI feedback, an opt-out and quality-reviewed practice plans. This implementation focuses on a free, offline-capable foundation and does **not** claim LLM integration.

## Delivered
- Home Mint Coach card, personalization **off by default** and a persisted `mint_coach_opt_in` preference.
- Deterministic two-step short plan tailored to chosen learning goal/difficulty and existing completion signals (two real-world quest completions); directs to existing routes only.
- Optional typed-response writing tips with supportive language; no unsupported grammar evaluation, pronunciation scoring or invented performance analytics.
- No network calls, microphone initiation, transcript persistence or background data collection from this coach.
- Reuses existing Games, Real-World Quests, Learn and Speak routes. Mint Garden awards remain limited to actual verified activity events; merely using the coach does not earn a Bloom.
- Unit tests for deterministic routing, simple adaptation, no invented confidence scores and safe tip behavior.

## Privacy and quality limitations
This is a rule-based *coach planner*, not an online generative AI teacher. Goal-based recommendations are hidden when off. The existing user goals and saved lesson and quest records remain accessible to the rest of the app as before; this toggle only controls this coach's personalization, not other app features.
Quality review of actual learner plans on physical devices is pending. Plan difficulty is selected from existing user choice, not inferred CEFR or measured proficiency. Feedback is a basic heuristic writing tip, not a reliable correction of a learner's grammar, accent or pronunciation. No cloud storage or new secrets.

## Acceptance
1. Fresh user sees Coach disabled; toggle on/off and restart, verify setting persists.
2. On: see a short recommended plan with both buttons opening real screens. Choose speaking-oriented vs grammar-oriented goals and verify suggestions vary.
3. Complete a Real-World Quest; return Home and verify plan shifts when both of the two MVP quests are completed.
4. Type a short response; see supportive coaching tip. Clear it and verify submit is disabled.
5. Turning personalization off hides the plan and reply editor. No rewards are minted for toggling or viewing the card.
6. Test small screen, large font, dark/light, TalkBack and reduced motion.
7. Require same-commit Android build, unit tests, lint and PR release gates. Owner device acceptance still required.

## Deferred, not claimed complete
- LLM-based correction, offline language model or voice coach personas
- Different language settings and dedicated goal editor (uses existing learning goal)
- Independent human quality evaluation of every recommended plan
- End-to-end digital accessibility and real-device QA
- No merge/deployment/payments without explicit approval

Draft stacked on unmerged Sprint 20 PR #39.
