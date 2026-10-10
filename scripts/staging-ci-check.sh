#!/usr/bin/env bash
set -euo pipefail
command -v docker >/dev/null || { echo 'FAIL: Docker is required'; exit 1; }
command -v curl >/dev/null || { echo 'FAIL: curl is required'; exit 1; }
[[ "${POSTGRES_PASSWORD:-}" != "" ]] || { echo 'FAIL: POSTGRES_PASSWORD is required'; exit 1; }
[[ "${AI_MODE:-mock}" == "mock" || "${AI_MODE:-}" == "real" ]] || { echo 'FAIL: AI_MODE must be mock or real'; exit 1; }
echo 'PASS: staging CI environment prerequisites'
