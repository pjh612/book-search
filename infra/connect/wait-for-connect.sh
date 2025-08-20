#!/bin/bash

# Kafka Connect가 준비될 때까지 대기
echo "Waiting for Kafka Connect to start..."
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' http://localhost:8083/connectors)" != "200" ]]; do
  echo "Kafka Connect is not ready yet. Retrying in 5 seconds..."
  sleep 5
done

echo "Kafka Connect is ready!"