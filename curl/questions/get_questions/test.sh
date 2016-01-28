#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -G ${server}"/questions/questions" | python -m json.tool
