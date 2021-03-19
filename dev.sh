#!/bin/bash
set -Eeuo pipefail
cd "$(dirname "$(readlink -f "$0")")"

. ./.dev/colors.inc.sh

function print_usage() {
  echo "${yellow}Usage:${reset}"
  echo " $0 <command> [options]"
  echo
  echo "${yellow}Available commands:${reset}"
  echo "${green} docker [...]${reset}          Manually use docker-compose commands for development containers"
  echo "${green} docker up -d${reset}          Create and start development containers (in background)"
  echo "${green} docker down${reset}           Stop and remove development containers"
  echo "${green} docker logs${reset}           Print development containers logs"
  echo "${green} iso3166-update${reset}        Update ISO 3166 data"
  echo "${green} db-migration <desc>${reset}   Print flyway database migration filename"
}

function main() {
  if [ $# -lt 1 ]; then
    print_usage
    return 0
  fi

  command="$1"
  shift
  case "$command" in
  docker)
    .dev/docker.sh "$@"
    ;;
  db-migration)
    .dev/db-migration.sh "$@"
    ;;
  iso3166-update)
    .dev/iso3166-update.sh "$@"
    ;;
  *)
    echo "${red2}Error: '${command}' is not a dev command.${reset2}" >&2
    return 1
    ;;
  esac
}

main "$@"
exit "$?"
