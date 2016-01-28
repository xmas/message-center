#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@script.r" \
  -F "dataSourceType=FILE_UPLOAD" \
  -F "scriptType=R" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  -F "tags=tag2" \
  ${server}"/push/questions"

