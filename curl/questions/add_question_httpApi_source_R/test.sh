#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@script.r" \
  -F "dataSourceType=HTTP_API" \
  -F "dataSourceResource=https://schemata.io/query/query?queryToken=PSLXNBJ9EJPIJM2I1GRSKEPBVGF4H338HY6XSN3Q" \
  -F "scriptType=R" \
  -F "dataType=FILE" \
  -F "cron=0 0 3 * * ?" \
  -F "tags=tag3" \
  -F "tags=tag2" \
  ${server}"/questions/questions"

