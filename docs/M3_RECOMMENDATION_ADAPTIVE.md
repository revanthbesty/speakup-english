# M3 Recommendation + Adaptive Difficulty

This slice adds an explainable rule-based recommendation engine and learner-specific adaptive difficulty state.

## APIs
- `GET /api/v1/recommendations/next`
- `GET /api/v1/adaptive-difficulty`

## Recommendation inputs
- active learning path and unfinished activities
- skill mastery and confidence
- weakest demonstrated skill
- activity type
- current adaptive difficulty

## Adaptive rules
- two consecutive correct outcomes increase difficulty by 0.05
- partial decreases difficulty by 0.02
- incorrect decreases difficulty by 0.08
- neutral does not change difficulty
- difficulty is always clamped to 0..1
- confidence grows conservatively

## Safety/learning rules
- recommendation is explainable and rule-based
- technical failures do not count as learner outcomes
- recommendation does not mutate mastery, XP, proficiency, or entitlement
- AI personalization can later consume this contract without replacing the authoritative state
