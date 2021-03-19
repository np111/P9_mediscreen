#!/bin/bash
set -Eeuo pipefail
cd "$(dirname "$(readlink -f "$0")")"

desc=$(echo "$*" | sed 's/\s/_/g')
datetime="$(date -u +%Y.%m.%d@%H*60*60+%M*60+%S)"
date="$(echo "$datetime" | cut -d@ -f1)"
time="$(printf "%05d" "$(echo "$datetime" | cut -d@ -f2 | bc)")"
echo "V${date}.${time}__${desc}.sql"
exit 0
