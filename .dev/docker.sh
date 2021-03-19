#!/bin/bash
set -Eeuo pipefail
cd "$(dirname "$(readlink -f "$0")")"

command='command'
if [[ "$USER" != "root" ]] && ! groups "$USER" | grep -q '\bdocker\b'; then
  command='sudo'
fi
${command} docker-compose -p 'mediscreen_dev' -f './dev-docker-compose.yml' "$@"
exit 0
