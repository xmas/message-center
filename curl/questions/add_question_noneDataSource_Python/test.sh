#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@script.py" \
  -F "dataSourceType=NONE" \
  -F "scriptType=PYTHON" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  -F "tags=tag2" \
  -F "cron=3 * * * * ?" \
  -F "scriptArgs={\"arg1\":\"asdfgafdg\", \"arg2\":\"asdfgsagDF\"}" \
  ${server}"/questions/questions"

