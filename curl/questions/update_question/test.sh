#!/usr/bin/env bash
curl -i -X PUT -H "Content-Type: multipart/form-data"  \
  -F "script=@script.r" \
  -F "answerTemplate=@template.json" \
  -F "dataSourceType=FILE_UPLOAD" \
  -F "scriptType=R" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  localhost:8080/push/questions/3