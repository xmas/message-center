#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -G ${server}"/push/questions" | python -m json.tool
