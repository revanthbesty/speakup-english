# SpeakUp — Staging Deployment

This staging layer provides a repeatable containerized environment for validating the v5.2.6 release candidate before production promotion.

## 1. Toolchain

Docker Engine with Docker Compose v2 and Node.js 22+ are required.

```bash
./scripts/staging-preflight.sh
```

## 2. Configure staging

```bash
export POSTGRES_PASSWORD='change-this-to-a-long-random-value'
export AI_MODE='mock'
```

For a real-provider staging run, set the AI provider endpoint/key/model variables from `infrastructure/staging/.env.staging.example` using your secret manager or shell environment. Never commit credentials.

## 3. Start the complete staging stack

```bash
docker compose -f infrastructure/staging/docker-compose.staging.yml up -d --build
```

The stack starts, in order:

1. PostgreSQL
2. database migrations + seed
3. API
4. Web
5. Admin

Expected local ports: API 4000, Web 3000, Admin 3001.

## 4. Smoke test

```bash
API_URL=http://localhost:4000 WEB_URL=http://localhost:3000 ADMIN_URL=http://localhost:3001 ./scripts/staging-smoke.sh
```

The smoke gate checks API health/readiness, Web/Admin availability, and protected Home/Progress authentication behavior.

## 5. Inspect / stop

```bash
docker compose -f infrastructure/staging/docker-compose.staging.yml ps
docker compose -f infrastructure/staging/docker-compose.staging.yml logs --tail=200

docker compose -f infrastructure/staging/docker-compose.staging.yml down
```

Use `down -v` only when intentionally resetting the staging database.

## 6. Learner E2E acceptance gate

After infrastructure smoke passes, validate:

Register → onboarding → diagnostic → placement → Home → Journey → lesson → sentence builder → speaking → feedback → retry → progress.

The SRS defines the MVP proof loop as **Verb → Sentence → Speak → Feedback → Retry → Reward → Progress**. Staging acceptance must verify this complete loop before production promotion.
## 7. Automated CI staging gate

A Docker-capable GitHub Actions runner can execute the complete staging gate with the checked-in workflow:

```text
.github/workflows/staging-e2e.yml
```

It provisions Node 22 and pnpm 10.0.0, runs the release/configuration and staging preflight checks, builds the full PostgreSQL → migration → API → Web/Admin stack, waits for readiness, executes the staging smoke gate, uploads Compose diagnostics, and tears the environment down even when a check fails.

Trigger it manually with **Actions → SpeakUp Staging E2E → Run workflow**, or push a tag matching `staging-*`.

