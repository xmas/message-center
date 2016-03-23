#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k \
-H "Accept: application/json" \
-G ${server}"/insights" -stderr \
-d "question=3" \
-d "eval=4" \
-d "from=2016-03-21T22:34:33" \
-d "to=2016-03-24T22:34:33" \ | python -m json.tool