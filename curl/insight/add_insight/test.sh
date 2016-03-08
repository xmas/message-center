#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@test.js" \
  -F "scriptType=NODE" \
  -F "questionId=3" \
  ${server}"/insightevals"