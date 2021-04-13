#!/bin/bash
set -Eeuo pipefail
cd "$(dirname "$(readlink -f "$0")")"

desc="$(echo "$*" | sed 's/^\(.\)/\U\1/g' | sed 's/\s\(.\)/\U\1/g')"
datetime="$(date -u +%Y%m%d@%H*60*60+%M*60+%S)"
date="$(echo "$datetime" | cut -d@ -f1)"
time="$(printf "%05d" "$(echo "$datetime" | cut -d@ -f2 | bc)")"
echo "V${date}_${time}__${desc}.java"
echo "@ChangeLog(order = \"${date}_${time}\")"
exit 0
