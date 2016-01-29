#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -G ${server}"/questions" | python -m json.tool
