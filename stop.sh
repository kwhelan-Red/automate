#!/bin/bash

echo "🛑 Stopping Customer Form Backend..."
echo ""

docker-compose down

echo ""
echo "✅ Backend stopped!"
echo ""
echo "Note: Database data is preserved in Docker volume 'mysql_data'"
echo "To remove all data: docker-compose down -v"
