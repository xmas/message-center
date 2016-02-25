#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST  \
  ${server}"/questions/3/insightEval/4"