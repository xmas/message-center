#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k \
-H "Accept: application/json" \
-G ${server}"/insights" -stderr \
`# Non predefined parameters that was added by evaluator. Nothing is required`\
`# Predefined parameters such as time and evalId. Nothing is required`\
-d "from=2016-03-23T17:27:20" \
-d "to=2016-03-26T22:34:33" \
-d "param2=2" \
-d "question=19" \ | python -m json.tool