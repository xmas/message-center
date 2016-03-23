#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k \
-H "Accept: application/json" \
-G ${server}"/insights" -stderr \
-d "param1=value3" \
-d "param2=5" \
-d "from=2016-03-23T14:57:20" \
-d "to=2016-03-26T22:34:33" \
-d "eval=1" \ | python -m json.tool