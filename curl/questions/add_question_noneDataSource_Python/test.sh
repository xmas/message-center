#!/usr/bin/env bash
curl -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@script.py" \
  -F "dataSourceType=NONE" \
  -F "scriptType=PYTHON" \
  -F "dataType=FILE" \
  -F "tags=tag1" \
  -F "tags=tag2" \
  localhost:8080/push/questions

