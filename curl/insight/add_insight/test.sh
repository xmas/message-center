#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "script=@test.js" \
  -F "scriptType=NODE" \
  ${server}"/questions/3/insightEval"