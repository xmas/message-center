#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "data=" \
  ${server}"localhost:8080/push/questions/3" | python -m json.tool