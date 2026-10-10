#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
fail(){ echo "FAIL: $1" >&2; exit 1; }
command -v node >/dev/null 2>&1 || fail "Node.js is required"
command -v corepack >/dev/null 2>&1 || fail "Corepack is required"
node -e 'const [m]=process.versions.node.split(".").map(Number); if(m<22) process.exit(1)' || fail "Node.js 22+ is required"
command -v docker >/dev/null 2>&1 || fail "Docker is required for containerized staging"
docker compose version >/dev/null 2>&1 || fail "Docker Compose v2 is required"
[[ -f package.json ]] || fail "package.json missing"
[[ -f pnpm-workspace.yaml ]] || fail "pnpm-workspace.yaml missing"
[[ -f infrastructure/staging/.env.staging.example ]] || fail "staging env template missing"
[[ -f infrastructure/staging/docker-compose.staging.yml ]] || fail "staging compose file missing"
[[ -f infrastructure/staging/docker/Dockerfile.api ]] || fail "API staging Dockerfile missing"
[[ -f infrastructure/staging/docker/Dockerfile.web ]] || fail "Web staging Dockerfile missing"
[[ -f infrastructure/staging/docker/Dockerfile.admin ]] || fail "Admin staging Dockerfile missing"
echo "PASS: staging preflight"
