#!/usr/bin/env bash
curl -i -k -X POST -H "Content-Type:application/json" \
http://localhost:8080/push/messages/v1 \
-d '@data.json'