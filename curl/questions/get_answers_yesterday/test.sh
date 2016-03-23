#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info5
yesterday="from="$(date -d "1 days ago" +"%Y-%m-%d")
curl --silent -k -G ${server}"/questions/3/answers" -d ${yesterday} | python -m json.tool