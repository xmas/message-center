#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k \
-H "Accept: application/json" \
-G ${server}"/insights/36" -stderr