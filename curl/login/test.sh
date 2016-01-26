#!/usr/bin/env bash
curl -i -X POST -H "Content-Type: application/form-data"  \
  -F "login=admin" \
  -F "password=password" \
  localhost:8080/push/login