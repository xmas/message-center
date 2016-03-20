#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@${dir}/simpleTest.js" \
  -F "scriptType=NODE" \
  -F "questionId=3" \
  -F "cron=3 * * * * ?" \
  ${server}"/insights/evals"
