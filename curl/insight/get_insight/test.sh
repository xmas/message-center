#!/usr/bin/env bash
read -r server < ../server.info
echo ${server}
curl -k -G ${server}"/questions/3/insightEval/"