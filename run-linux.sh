#!/usr/bin/env bash
set -euo pipefail

API_URL="${1:-${MANAGEMENT_API_BASE_URL:-http://localhost:8081/api}}"

echo "[INFO] Using MANAGEMENT_API_BASE_URL=$API_URL"
export MANAGEMENT_API_BASE_URL="$API_URL"

if [ ! -f "target/book-ui.jar" ]; then
  echo "[INFO] Building project..."
  mvn -q -DskipTests clean package
fi

echo "[INFO] Starting Web UI on http://localhost:8080"
exec java -jar target/book-ui.jar
