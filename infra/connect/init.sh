#!/bin/bash
set -e

CONNECT_URL=http://kafka-connect:8083/connectors

echo "Registering connector..."
curl -X POST -H "Content-Type: application/json" \
    --data @/connect/connect-config.json \
    $CONNECT_URL || echo "Connector already exists"