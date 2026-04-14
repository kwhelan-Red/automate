#!/bin/bash

echo "🚀 Starting Customer Form Backend..."
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker is not running."
    echo "Please start Docker Desktop and try again."
    exit 1
fi

# Start Docker containers
echo "📦 Building and starting containers..."
docker-compose up -d --build

# Wait for services to be ready
echo ""
echo "⏳ Waiting for services to start..."
sleep 5

# Check if containers are running
if docker ps | grep -q "customer_api"; then
    echo ""
    echo "✅ Backend is ready!"
    echo ""
    echo "📍 API URL: http://localhost:8080/"
    echo "📍 Test Connection: http://localhost:8080/test_connection.php"
    echo "📍 MySQL Port: 3306"
    echo ""
    echo "Database credentials:"
    echo "  - Database: customer_data"
    echo "  - User: customer_app"
    echo "  - Password: customer123"
    echo ""
    echo "To view logs: docker-compose logs -f"
    echo "To stop: ./stop.sh or docker-compose down"
else
    echo ""
    echo "❌ Error: Containers failed to start"
    echo "Check logs with: docker-compose logs"
fi
