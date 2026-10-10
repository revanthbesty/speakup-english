# SpeakUp English — Release Candidate v4.1.0

## Release gates
- Web/PWA smoke flow: landing → home → journey → lesson → activity → speaking → feedback → progress
- API health/readiness endpoints
- Database migrations are ordered and repeatable
- AI Gateway remains provider-agnostic with mock fallback
- Technical failures never become learner failures
- Offline queue remains idempotent
- Accessibility/localization configuration preserved
- Android source included
- Admin source included

## Production checks
1. Configure production secrets outside the repository.
2. Apply database migrations before enabling learner traffic.
3. Run smoke tests against staging.
4. Verify AI provider health and fallback.
5. Verify backups and restore procedure.
6. Enable observability and request IDs.
7. Promote only after all gates pass.

## Rollback
- Disable new release traffic.
- Restore previous application image/artifact.
- Do not roll back database migrations destructively; use forward-compatible migration strategy.
- Re-run health/readiness and learner smoke tests.
