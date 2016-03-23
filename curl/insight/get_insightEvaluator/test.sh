#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k -G \
-d "questionId=3" \
${server}"/insights/evals"