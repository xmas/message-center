#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info5
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@${dir}/script.py" \
  -F "dataSourceType=NONE" \
  -F "scriptType=PYTHON" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  -F "tags=tag2" \
  -F "cron=3 * * * * ?" \
  -F "scriptArgs={\"arg1\":\"asdfgafdg\", \"arg2\":\"asdfgsagDF\"}" \
  ${server}"/questions"

