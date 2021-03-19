#!/bin/bash
set -Eeuo pipefail
cd "$(dirname "$(readlink -f "$0")")"

# Create temp directory
tmp_dir="$(mktemp -d -t "iso3166-update_XXXXXXXX")"
tmp_dir_cleanup() { rm -rf "$tmp_dir"; }
trap tmp_dir_cleanup INT TERM EXIT

# Download world_countries data
curl -fSL 'https://github.com/stefangabos/world_countries/archive/master.tar.gz' | tar xfz - --strip-components=1 -C "$tmp_dir"
{
  find "${tmp_dir}/data" -type f -name 'world.json' -print0 | LANG=C sort -z
  echo -n "${tmp_dir}/data/en/world.json"
  echo -ne '\0'
} |
  xargs -0 jq -r '
    def locale: input_filename|split("/")[-2];
    reduce .[] as $i ({}; .[$i.alpha2]=$i * {name: {(locale): $i.name}})
  ' |
  sed 's/ (pays)"/"/g' |
  jq -r -s 'reduce .[] as $i ({}; . * $i) | to_entries | map(.value)' >'../patients/service/src/main/resources/iso3166.json'

exit 0
