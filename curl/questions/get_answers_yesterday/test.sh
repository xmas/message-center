#!/usr/bin/env bash
yesterday="from="$(date -d "1 days ago" +"%Y-%m-%d")
curl -G "localhost:8080/push/questions/3/answers" -d ${yesterday}