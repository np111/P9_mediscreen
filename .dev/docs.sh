#!/bin/bash
set -Eeuo pipefail
cd "$(dirname "$(readlink -f "$0")")"

base_repo_dir="$(pwd)/.."
tmp_dir="$(pwd)/.tmp_docs"
docs_dir="$(pwd)/.tmp_docs/docs"

function generate() {
  git_rev="$(git rev-parse HEAD)"

  rm -rf "$docs_dir"
  mkdir -p "$docs_dir"

  # Copy openapi specification
  function openapi_filter() {
    jq '.info.description = "<a href=\"https://github.com/np111/P9_mediscreen\">View Source on GitHub</a>" | .servers = []'
  }
  curl -fsSL 'http://localhost:8081/api-docs' | openapi_filter >"${docs_dir}/openapi-patients.json"
  curl -fsSL 'http://localhost:8082/api-docs' | openapi_filter >"${docs_dir}/openapi-notes.json"
  curl -fsSL 'http://localhost:8083/api-docs' | openapi_filter >"${docs_dir}/openapi-assessment.json"

  # Copy swagger-ui
  pushd "$tmp_dir"
  npm i swagger-ui-dist@3.44.1
  rsync -av --progress 'node_modules/swagger-ui-dist/' "$docs_dir" \
    --exclude 'package.json' \
    --exclude 'README.md' \
    --exclude 'index.js' \
    --exclude 'swagger-ui.js' \
    --exclude 'absolute-path.js' \
    --exclude '*-es-*' \
    --exclude '*.map'
  swagger_urls=''
  swagger_urls+="{name: 'Patients API', url: 'openapi-patients.json?${git_rev}'},"
  swagger_urls+="{name: 'Notes API', url: 'openapi-notes.json?${git_rev}'},"
  swagger_urls+="{name: 'Assessment API', url: 'openapi-assessment.json?${git_rev}'},"
  sed -i 's#<title>Swagger UI</title>#<title>Mediscreen API Documentation</title>#' "${docs_dir}/index.html"
  sed -i 's#url: "https://petstore.swagger.io/v2/swagger.json",#urls: ['"$swagger_urls"'], readOnly: true,#' "${docs_dir}/index.html"
  popd

  echo 'Done!'
}

function publish() {
  pushd "$docs_dir"
  rm -rf .git
  git init
  cp "${base_repo_dir}/.git/config" '.git/config'
  git checkout --orphan docs
  git add .
  git commit -m 'Publish docs'
  git push -f origin docs
  popd

  echo 'Done!'
}

function main() {
  case "${1:-}" in
  generate)
    generate "$@"
    ;;
  publish)
    publish "$@"
    ;;
  *)
    echo "Usage: $0 generate|publish" >&2
    exit 1
    ;;
  esac
}

main "$@"
exit 0
