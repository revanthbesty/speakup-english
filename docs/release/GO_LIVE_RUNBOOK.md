# SpeakUp Go-Live Runbook

## 1. Preconditions

- Provision PostgreSQL and object storage.
- Configure server-side AI/STT/TTS provider credentials.
- Configure production secrets; never commit them.
- Configure the public Web/PWA, API, and Admin domains.
- Confirm HTTPS, DNS, backups, monitoring, and alerting.

## 2. Database

```bash
pnpm install --frozen-lockfile
pnpm db:migrate
pnpm db:seed
```

Do not run destructive reset commands against production.

## 3. Application validation

```bash
pnpm check
bash scripts/release-preflight.sh
```

Then execute the browser/mobile smoke checklist in `tests/release/smoke-checklist.md`.

## 4. Provider validation

Verify each configured provider for the capabilities actually enabled in production:

- text generation
- structured output
- speech-to-text
- text-to-speech
- pronunciation
- fluency

Force a provider failure and confirm fallback/recovery behavior before launch.

## 5. Security

Confirm:

- production secrets are server-side only
- HTTPS is enforced
- authentication/session expiry works
- authorization boundaries are tested
- rate limits are active
- request IDs appear in logs
- raw audio is not retained beyond configured processing lifetime
- learner export/deletion flows are available

## 6. Launch

1. Deploy API.
2. Run migrations.
3. Deploy Web/PWA.
4. Deploy Admin separately.
5. Deploy Android release candidate through the chosen signing/distribution process.
6. Run smoke tests.
7. Monitor errors, latency, AI failures, speech failures, and database health.

## 7. Rollback

Rollback application versions first when possible. Never use destructive database reset operations as a rollback mechanism. Database changes must remain backward-compatible with the previous application version during the rollback window.
