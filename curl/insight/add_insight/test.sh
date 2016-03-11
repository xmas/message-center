#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@simpleTest.js" \
  -F "scriptType=NODE" \
  -F "questionId=3" \
  -F "cron=3 * * * * ?" \
  ${server}"/insightevals"
