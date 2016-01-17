#!/usr/bin/env bash
curl -i -X POST -H "Content-Type: multipart/form-data"  \
  -F "data=" \
  localhost:8080/push/questions/3