#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k \
-H "Accept: application/json" \
-G ${server}"/insights" -stderr