#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k -G ${server}"/questions" | python -m json.tool
