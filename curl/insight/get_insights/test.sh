#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k \
-H "Accept: application/json" \
-G ${server}"/insights" -stderr \
`# Non predefined parameters that was added by evaluator. Nothing is required`\
-d "param2=2" \
`# Predefined parameters such as time when was evaluated, question and evalId. Nothing is required`\
-d "from=2016-03-22" \
-d "to=2016-03-26T22:15:31" \
-d "eval=1" \
-d "question=3" \ |
 python -m json.tool