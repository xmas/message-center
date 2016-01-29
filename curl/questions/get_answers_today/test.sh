#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -G ${server}"/questions/3/answers" -stderr | python -m json.tool