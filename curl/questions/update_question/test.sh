#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info5
curl -i -X PUT -H "Content-Type: multipart/form-data"  \
  -F "script=@${dir}/script.r" \
  -F "answerTemplate=@template.json" \
  -F "dataSourceType=FILE_UPLOAD" \
  -F "scriptType=R" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  ${server}"/questions/3"