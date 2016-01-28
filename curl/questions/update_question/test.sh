#!/usr/bin/env bash
read -r server < ../server.info
curl -i -X PUT -H "Content-Type: multipart/form-data"  \
  -F "script=@script.r" \
  -F "answerTemplate=@template.json" \
  -F "dataSourceType=FILE_UPLOAD" \
  -F "scriptType=R" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  ${server}"/questions/questions/3"