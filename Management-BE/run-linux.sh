#!/usr/bin/env bash
set -euo pipefail

if [[ ${1:-} != "--skip-build" ]]; then
  mvn -q -DskipTests clean package
fi

jar=$(ls -1 target/*.jar | grep -v original | head -n1)
if [[ -z "$jar" ]]; then
  echo "Jar not found in target" >&2
  exit 1
fi

exec java -jar "$jar"
