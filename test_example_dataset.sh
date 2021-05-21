#!/bin/bash
set -Eeuo pipefail
shopt -s inherit_errexit
. "$(dirname "$(readlink -f "$0")")/.dev/colors.inc.sh"

function read_csv() {
  python3 - "$1" <<EOF
import sys
import csv
with open(sys.argv[1], 'r') as file:
  reader = csv.reader(file)
  for row in reader:
    print('\x1e'.join(row), end='\0')
EOF
}

function bash_escape() {
  ret=
  for arg in "$@"; do
    [[ -n "$ret" ]] && ret+=' '
    if [[ "$arg" =~ ^[a-zA-Z0-9_-]+$ ]]; then
      ret+="$arg"
    elif [[ "$arg" != *"'"* ]]; then
      ret+="'$arg'"
    else
      ret+="'${arg//\'/\'\\\'\'}'"
    fi
  done
  echo "$ret"
}

function do_curl() {
  echo "${gray2}>> $(bash_escape curl "$@")${reset2}" >&2
  res="$(curl "$@")"
  echo "${gray2}<< ${res}${reset2}" >&2
  echo "$res"
}

function create_patient() {
  data="$(jq -rMnc \
    --arg firstName "$1" \
    --arg lastName "$2" \
    --arg birthdate "$3" \
    --arg gender "$4" \
    --arg address "$5" \
    --arg phone "$6" \
    '{"firstName": $firstName, "lastName": $lastName, "birthdate": $birthdate, "gender": $gender, "address": $address, "phone": $phone}')"
  res="$(do_curl -fsSL -X POST 'http://localhost:8081/patient' \
    --header 'Content-Type: application/json' \
    --data-raw "$data")"
  echo "Patient ${2} created." >&2
  jq -rM .id <<<"$res"
}

function add_note() {
  name="$1"
  data="$(jq -rMnc \
    --arg patientId "$2" \
    --arg content "$3" \
    '{"patientId": $patientId, "content": $content}')"
  res="$(do_curl -fsSL -X POST 'http://localhost:8082/note' \
    --header 'Content-Type: application/json' \
    --data-raw "$data")"
  echo "Note added for ${name}." >&2
}

function check_assess() {
  name="$1"
  data="$(jq -rMnc \
    --arg patientId "$2" \
    '{"patientId": $patientId}')"
  exceptedRiskLevel="${3:-}"

  res="$(do_curl -fsSL -X POST 'http://localhost:8083/assessment' \
    --header 'Content-Type: application/json' \
    --data-raw "$data")"

  riskLevel="$(jq -rM .riskLevel <<<"$res")"
  if [[ -z "$exceptedRiskLevel" ]]; then
    riskLevelResult=
  elif [[ "$exceptedRiskLevel" == "$riskLevel" ]]; then
    riskLevelResult=" => ${green2}OK${reset2}"
  else
    riskLevelResult=" => ${red2}ERROR (excepting: ${exceptedRiskLevel})${reset2}"
  fi

  echo "Assessment for ${name}: ${magenta2}${riskLevel}${reset2}${riskLevelResult}" >&2
}

function test_dataset() {
  examplePrefix="$1"

  declare -A patientsIds
  declare -A patientsExceptedAssessment

  echo -e "\n${yellow2}Creating patients...${reset2}" >&2
  while IFS=$'\x1e' read -r -d '' lastName firstName birthdate gender address phone _; do
    case "$gender" in
    M) gender='MALE' ;;
    F) gender='FEMALE' ;;
    esac
    phone="1 $(echo "$phone" | tr -d '-')"

    patientId="$(create_patient "$firstName" "$lastName" "$birthdate" "$gender" "$address" "$phone")"
    patientsIds+=(["$lastName"]="$patientId")
  done < <(read_csv "${examplePrefix}_patients.csv" || kill $$)

  echo -e "\n${yellow2}Adding notes...${reset2}" >&2
  while IFS=$'\x1e' read -r -d '' lastName note _; do
    patientId="${patientsIds[$lastName]}"

    add_note "$lastName" "$patientId" "$note"
  done < <(read_csv "${examplePrefix}_notes.csv" || kill $$)

  echo -e "\n${yellow2}Checking risk level...${reset2}" >&2
  if [[ -f "${examplePrefix}_assessments.csv" ]]; then
    while IFS=$'\x1e' read -r -d '' lastName exceptedAssessment _; do
      patientsExceptedAssessment+=(["$lastName"]="$exceptedAssessment")
    done < <(read_csv "${examplePrefix}_assessments.csv" || kill $$)
  fi
  for lastName in "${!patientsIds[@]}"; do
    patientId="${patientsIds[$lastName]}"
    exceptedAssessment="${patientsExceptedAssessment[$lastName]:-}"

    check_assess "$lastName" "$patientId" "$exceptedAssessment"
  done
}

function main() {
  if [[ $# -lt 1 ]]; then
    echo "${red2}Error:${reset2} You must specify a dataset to test." >&2
    echo "For example: $0 ${magenta2}example-dataset/oc_example1${reset2}" >&2
    return 1
  fi

  test_dataset "$1"
}

main "$@"
exit 0
