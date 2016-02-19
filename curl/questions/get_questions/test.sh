#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -G ${server}"/questions/3" | python -m json.tool
