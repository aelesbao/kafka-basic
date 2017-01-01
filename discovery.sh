#!/bin/bash

function get_containers_by_service() {
  local service=$1
  curl -s --unix-socket /tmp/docker.sock http://localhost/containers/json | \
    jq "map(select(.Labels.\"com.docker.compose.service\" == \"${service}\" and .Labels.\"com.docker.compose.oneoff\" == \"False\"))"
}

function get_kafka_servers() {
  get_containers_by_service "kafka" | \
    jq "map(.NetworkSettings.Networks | .[].IPAddress | select (. != null))" | \
    jq -r "reduce .[] as \$brokers (null; . + \$brokers + \":${KAFKA_PORT},\")" |
    sed 's/,$//'
}

apk add --no-cache curl jq

export KAFKA_BOOTSTRAP_SERVERS=$(get_kafka_servers)

exec /docker-entrypoint.sh "$@"
