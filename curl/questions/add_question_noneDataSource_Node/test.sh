#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@script.js" \
  -F "dataSourceType=NONE" \
  -F "scriptType=NODE" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  -F "tags=tag2" \
  ${server}"/push/questions"

