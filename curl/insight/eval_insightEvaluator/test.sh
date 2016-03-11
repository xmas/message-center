#!/usr/bin/env bash
read -r server < ../server.info
curl --silent -k -i -X POST  \
  ${server}"/insightevals/1"