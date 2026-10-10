#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
fail(){ echo "FAIL: $1" >&2; exit 1; }
command -v node >/dev/null 2>&1 || fail "Node.js is required"
command -v corepack >/dev/null 2>&1 || fail "Corepack is required"
node -e 'const [m]=process.versions.node.split(".").map(Number); if(m<22) process.exit(1)' || fail "Node.js 22+ is required"
corepack enable >/dev/null 2>&1 || true
if ! command -v pnpm >/dev/null 2>&1 || [[ "$(pnpm --version 2>/dev/null || true)" != "10.0.0" ]]; then
  corepack prepare pnpm@10.0.0 --activate >/dev/null 2>&1 || fail "Unable to activate pnpm 10.0.0"
fi
[[ "$(pnpm --version)" == "10.0.0" ]] || fail "Expected pnpm 10.0.0"
[[ -f package.json && -f pnpm-workspace.yaml ]] || fail "Workspace root files missing"
pnpm install --frozen-lockfile=false
pnpm lint
pnpm typecheck
pnpm test
pnpm build
echo "PASS: full release verification"
