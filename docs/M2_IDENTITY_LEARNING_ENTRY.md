# M2 — Identity & Learning Entry

## Implemented

### Identity
- Registration
- Login
- Logout
- Password hashing with Node scrypt
- Opaque session tokens stored as hashes
- Session expiry/revocation
- User → LearnerProfile relationship
- Learner preferences
- Authenticated API boundary

### Onboarding
State foundation:
`NOT_STARTED → AUDIENCE_SELECTED → GOALS_SELECTED → SELF_ASSESSMENT_COMPLETED → DAILY_GOAL_SELECTED`

Implemented operations:
- audience selection
- learning goals
- self assessment
- daily learning goal
- onboarding retrieval

### Privacy / safety foundation
- Consent records table
- learner data separated from account identity
- audience stored on learner profile
- raw passwords never persisted
- session tokens never persisted in plaintext
- generic invalid-credential response

## Supported audience values

`KIDS`, `TEENS`, `STUDENTS`, `COLLEGE`, `JOB_SEEKERS`, `PROFESSIONALS`, `ADULTS`, `SENIORS`

The SRS requires the same underlying learning engine while adapting vocabulary, difficulty, speaking speed, interface complexity, activities, scenarios, examples, rewards and feedback.

## Supported goal values

`GENERAL_ENGLISH`, `CONVERSATION`, `WORKPLACE`, `INTERVIEW`, `ACADEMIC`, `TRAVEL`, `CONFIDENCE`

## API

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`
- `GET /api/v1/onboarding`
- `PATCH /api/v1/onboarding/audience`
- `PATCH /api/v1/onboarding/goals`
- `PATCH /api/v1/onboarding/self-assessment`
- `PATCH /api/v1/onboarding/daily-goal`

## Security notes

This is the M2 foundation, not the final production security gate. Production still requires rate limiting, email verification/recovery, device/session controls, CSRF/CORS policy where applicable, audit events, security testing, child-consent workflows, and staging validation.

## Next

Diagnostic creation → section attempts → completion → result → placement recommendation → learner confirmation/override.
