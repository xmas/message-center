#!/usr/bin/env bash
curl --silent -G "localhost:8080/push/questions" | python -m json.tool
