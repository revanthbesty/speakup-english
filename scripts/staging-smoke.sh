#!/usr/bin/env bash
set -euo pipefail
API_URL="${API_URL:-http://localhost:4000}"
WEB_URL="${WEB_URL:-http://localhost:3000}"
ADMIN_URL="${ADMIN_URL:-http://localhost:3001}"
check(){ local name="$1" url="$2" expected="${3:-200}"; code="$(curl -sS -o /dev/null -w '%{http_code}' "$url" || true)"; [[ "$code" == "$expected" ]] || { echo "FAIL: $name returned HTTP $code from $url" >&2; exit 1; }; echo "PASS: $name HTTP $code"; }
check_json(){ local name="$1" url="$2" expected="$3"; body="$(curl -fsS "$url")" || { echo "FAIL: $name request failed: $url" >&2; exit 1; }; printf '%s' "$body" | grep -q "$expected" || { echo "FAIL: $name response missing $expected" >&2; exit 1; }; echo "PASS: $name"; }
check_json "API health" "$API_URL/health" '"status":"ok"'
check_json "API readiness" "$API_URL/ready" '"status":"ready"'
check "Web" "$WEB_URL/"
check "Admin" "$ADMIN_URL/"
# Contract-level checks: unauthenticated protected routes must reject access.
check "Protected Home auth" "$API_URL/api/v1/home" 401
check "Protected Progress auth" "$API_URL/api/v1/progress" 401
echo "PASS: staging smoke checks"
