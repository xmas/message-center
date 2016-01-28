#!/usr/bin/env bash
pathVariables=(`
    curl -G "localhost:8080/push/questions/3/answers" -stderr |\
    python -m json.tool |\
    grep -Po '((?<=dataDir":\s")[1-9a-f]+)|(((?<=date":\s")[^",]+))'
`)

curl -G "localhost:8080/push/questions/data/${pathVariables[0]}/${pathVariables[1]}/" -stderr


