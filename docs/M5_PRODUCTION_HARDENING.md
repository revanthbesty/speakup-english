# M5 Production Hardening

## Security
- request IDs on every API response
- security headers
- server-side provider secrets
- no client direct database/provider access
- no raw audio retention by default
- technical failures isolated from learner outcomes

## Reliability
- health/readiness endpoints
- provider circuit breakers and fallback
- idempotent learning events
- server-authoritative mastery, XP and streaks

## Release
1. Provision PostgreSQL.
2. Apply migrations.
3. Seed versioned content in non-production environments.
4. Configure secrets in the deployment secret manager.
5. Run typecheck, tests and build.
6. Run web/API smoke tests.
7. Deploy API before clients.
8. Verify `/health` and `/ready`.
9. Enable production AI providers only after golden evaluation gates pass.
