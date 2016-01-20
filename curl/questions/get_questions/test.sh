#!/usr/bin/env bash
curl -G "localhost:8080/push/questions" -d "tags=tag1" -d "tags=tag2"
