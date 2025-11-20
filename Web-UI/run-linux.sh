#!/usr/bin/env bash
set -euo pipefail

API_URL=${MANAGEMENT_API_BASE_URL:-"http://localhost:8081/api"}
echo "[INFO] Using MANAGEMENT_API_BASE_URL=$API_URL"
export MANAGEMENT_API_BASE_URL="$API_URL"

if [[ "${1:-}" != "--skip-build" ]]; then
  echo "[INFO] Building Web-UI module..."
  mvn -q -pl Web-UI -am -DskipTests clean package
fi

JAR="$(dirname "$0")/target/book-catalogue-ui.jar"
if [[ ! -f "$JAR" ]]; then
  JAR="$(dirname "$0")/target/book-catalogue-ui.jar.original"
fi

echo "[INFO] Starting Web UI on http://localhost:8080"
exec java -jar "$JAR"
