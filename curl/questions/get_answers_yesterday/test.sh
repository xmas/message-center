#!/usr/bin/env bash
read -r server < ../server.info
yesterday="from="$(date -d "1 days ago" +"%Y-%m-%d")
curl --silent -k -G ${server}"/questions/questions/3/answers" -d ${yesterday} | python -m json.tool