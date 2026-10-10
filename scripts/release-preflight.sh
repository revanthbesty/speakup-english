#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
fail(){ echo "FAIL: $1" >&2; exit 1; }
req=(package.json pnpm-workspace.yaml docker-compose.yml .env.production.example apps/api/src/main.ts apps/web/app/page.tsx apps/admin/app/page.tsx database/seed/001_mvp.sql .github/workflows/ci.yml .github/workflows/release-gates.yml)
for f in "${req[@]}"; do [[ -f "$ROOT/$f" ]] || fail "missing $f"; done
count=$(find "$ROOT/database/migrations" -maxdepth 1 -name '*.sql' | wc -l | tr -d ' ')
[[ "$count" -ge 16 ]] || fail "expected at least 16 migrations, found $count"
for s in build lint typecheck test check; do grep -q '"'"$s"'"' "$ROOT/package.json" || fail "missing package script $s"; done
grep -q 'db:migrate' "$ROOT/package.json" || fail "missing db migration script"
grep -q 'db:seed' "$ROOT/package.json" || fail "missing db seed script"
echo "PASS: release preflight"
echo "migrations=$count"
