#!/usr/bin/env bash
read -r server < ../server.info
echo ${server}
curl --silent -k -G \
-d "questionId=3" \
${server}"/insightevals"