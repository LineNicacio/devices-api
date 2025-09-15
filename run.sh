#!/bin/bash
set -e

echo "🔧 Building the project..."
./mvnw clean package -DskipTests

echo "🐳 Building and starting Docker containers..."
docker-compose -f infra/docker/docker-compose.yaml up --build