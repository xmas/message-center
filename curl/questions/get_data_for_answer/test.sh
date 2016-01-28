#!/usr/bin/env bash
read -r server < ../server.info
pathVariables=(`
    curl -k -G ${server}"/push/questions/3/answers" -stderr |\
    python -m json.tool |\
    grep -Po '((?<=dataDir":\s")[1-9a-f]+)|(((?<=date":\s")[^",]+))'
`)

curl -k -G ${server}"/push/questions/data/${pathVariables[0]}/${pathVariables[1]}/" -stderr


