#!/usr/bin/env bash
curl -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@script.r" \
  -F "answerTemplate=@template.json" \
  -F "dataSourceType=HTTP_API" \
  -F "dataSourceResource=https://schemata.io/query/query?queryToken=PSLXNBJ9EJPIJM2I1GRSKEPBVGF4H338HY6XSN3Q" \
  -F "scriptType=R" \
  -F "dataType=FILE" \
  -F "tags=tag3" \
  -F "tags=tag2" \
  localhost:8080/push/questions

