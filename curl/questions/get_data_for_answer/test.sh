#!/usr/bin/env bash
read -r server < ../server.info
pathVariables=(`
    curl -k -G ${server}"/questions/3/answers" -stderr |\
    python -m json.tool |\
    grep -Po '((?<=dataDir":\s")[0-9a-f]+)|(((?<=date":\s")[^",]+))'
`)

curl -k -G ${server}"/questions/data/${pathVariables[0]}/${pathVariables[1]}/" -stderr


