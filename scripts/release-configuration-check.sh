#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
fail(){ echo "FAIL: $1" >&2; exit 1; }
[[ -f package.json ]] || fail "package.json missing"
[[ "$(node -p "require('./package.json').packageManager")" == "pnpm@10.0.0" ]] || fail "packageManager must be pnpm@10.0.0"
for f in infrastructure/staging/docker-compose.staging.yml infrastructure/staging/docker/Dockerfile.api infrastructure/staging/docker/Dockerfile.web infrastructure/staging/docker/Dockerfile.admin scripts/staging-preflight.sh scripts/staging-smoke.sh; do [[ -f "$f" ]] || fail "missing $f"; done
for f in scripts/staging-preflight.sh scripts/staging-smoke.sh scripts/verify-release.sh; do [[ -x "$f" ]] || fail "$f must be executable"; done
count="$(find database/migrations -maxdepth 1 -type f -name '*.sql' | wc -l | tr -d ' ')"
[[ "$count" == "16" ]] || fail "expected 16 migrations, found $count"
for svc in postgres migrate api web admin; do grep -q "^  $svc:" infrastructure/staging/docker-compose.staging.yml || fail "staging service $svc missing"; done
grep -q 'condition: service_completed_successfully' infrastructure/staging/docker-compose.staging.yml || fail "API is not gated on successful migration"
grep -q 'condition: service_healthy' infrastructure/staging/docker-compose.staging.yml || fail "dependent services are not health-gated"
grep -q 'API health' scripts/staging-smoke.sh || fail "health smoke check missing"
grep -q 'Protected Home auth' scripts/staging-smoke.sh || fail "protected-route smoke check missing"
node - <<'NODE'
const fs=require('fs'); const p=JSON.parse(fs.readFileSync('package.json','utf8'));
for(const k of ['verify:release','staging:preflight','staging:smoke']) if(!p.scripts[k]) process.exit(1);
NODE

echo "PASS: release configuration consistency"
echo "PASS: migrations=$count"
echo "PASS: staging services=postgres,migrate,api,web,admin"
