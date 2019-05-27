#!/usr/bin/env bash

curl -XPOST -H "Content-Type: application/x-ndjson" localhost:9200/$1/_bulk --data-binary "@alice.jsonl"