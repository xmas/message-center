#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info5
pathVariables=(`
    curl -k -G ${server}"/questions/3/answers" -stderr |\
    python -m json.tool |\
    grep -Po '((?<=dataDir":\s")[0-9a-f]+)|(((?<=date":\s")[^",]+))'
`)

curl -k -G ${server}"/questions/data/${pathVariables[0]}/${pathVariables[1]}/" -stderr


