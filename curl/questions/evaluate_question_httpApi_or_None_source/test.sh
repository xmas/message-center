#!/usr/bin/env bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
read -r server < ${dir}/../server.info
curl --silent -k -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "data=" \
  ${server}"/questions/4"