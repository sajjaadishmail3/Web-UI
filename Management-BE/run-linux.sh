#!/usr/bin/env bash
set -euo pipefail

# Ensure the script runs from the module directory
cd "$(dirname "$0")"

if [[ "${1:-}" != "--skip-build" ]]; then
  mvn -q -DskipTests clean package
fi

jar=$(ls -1 target/*.jar | grep -v original | head -n1)
if [[ -z "$jar" ]]; then
  echo "Jar not found in target" >&2
  exit 1
fi
