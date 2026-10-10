# SpeakUp Production Launch Package

This package is the continuation of the M6 production-validation build. It adds the concrete go-live runbook, production gates, environment matrix, and an executable repository preflight.

## Run repository preflight

```bash
bash scripts/release-preflight.sh
```

## Full verification

```bash
pnpm install --frozen-lockfile
pnpm check
```

`pnpm check` is the authoritative project gate for lint, typecheck, tests, and builds. If dependencies are unavailable, the gate is **not** considered passed.

## Deployment

Follow `docs/release/GO_LIVE_RUNBOOK.md` and do not skip the real-provider, backup/restore, security, staging, or production smoke-test gates.
