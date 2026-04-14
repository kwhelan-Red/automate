# Docker Setup Guide - Super Easy! 🐳

This is the **easiest way** to get the Customer Form app running. No need for XAMPP, MAMP, or manual MySQL setup!

## Prerequisites

Just install Docker Desktop:
- **Mac**: [Download Docker Desktop for Mac](https://www.docker.com/products/docker-desktop)
- **Windows**: [Download Docker Desktop for Windows](https://www.docker.com/products/docker-desktop)
- **Linux**: Install Docker and Docker Compose from your package manager

## Quick Start (3 Steps!)

### Step 1: Start the Backend (1 command!)

Open Terminal in the project directory and run:

```bash
./start.sh
```

That's it! The script will:
- ✅ Build PHP/Apache container
- ✅ Start MySQL database container
- ✅ Create database and tables automatically
- ✅ Configure everything for you

You'll see:
```
✅ Backend is ready!

📍 API URL: http://localhost:8080/
📍 Test Connection: http://localhost:8080/test_connection.php
```

### Step 2: Test the Backend

Open your browser and visit:
```
http://localhost:8080/test_connection.php
```

You should see:
- ✓ Database connection successful!
- ✓ Customers table exists

### Step 3: Run the Android App

1. **Open Android Studio**
   - File → Open → `/Users/kwhelan/Projects/automate`
   - Wait for Gradle sync

2. **The app is already configured!**
   - RetrofitClient is set to `http://10.0.2.2:8080/`
   - This connects to your Docker backend

3. **Click Run** (green play button)
   - Select emulator or device
   - Fill the form and submit!

## What's Running?

When you run `./start.sh`, Docker starts these containers:

| Container | What It Does | Port |
|-----------|--------------|------|
| `customer_api` | PHP/Apache web server | 8080 |
| `customer_db` | MySQL database | 3306 |

## Useful Commands

```bash
# Start the backend
./start.sh

# Stop the backend
./stop.sh

# View real-time logs
./logs.sh

# Or use docker-compose directly:
docker-compose up -d        # Start in background
docker-compose down         # Stop containers
docker-compose logs -f      # View logs
docker-compose ps           # Show running containers
```

## Database Access

### From Android App
Already configured! Just run the app.

### From MySQL Client
```bash
Host: localhost
Port: 3306
Database: customer_data
Username: customer_app
Password: customer123
```

### Using Command Line
```bash
docker exec -it customer_db mysql -u customer_app -pcustomer123 customer_data
```

Then you can run SQL:
```sql
SELECT * FROM customers;
```

## Testing with Physical Android Device

If testing on a real phone (not emulator):

1. **Find your computer's IP address**:
   ```bash
   # Mac/Linux
   ifconfig | grep "inet "
   
   # Windows
   ipconfig
   ```

2. **Update RetrofitClient.kt**:
   ```kotlin
   private const val BASE_URL = "http://YOUR_IP:8080/"
   ```
   Example: `"http://192.168.1.100:8080/"`

3. **Make sure phone and computer are on same WiFi**

## Troubleshooting

### "Docker is not running"
- **Fix**: Open Docker Desktop application
- Wait for whale icon to be stable (not animating)

### "Port 8080 already in use"
- **Fix**: Change port in `docker-compose.yml`:
  ```yaml
  ports:
    - "8081:80"  # Use 8081 instead
  ```
- Update Android app to use new port

### "Container failed to start"
- **Check logs**: `./logs.sh` or `docker-compose logs`
- **Restart**: `./stop.sh && ./start.sh`

### Android app can't connect
- **Emulator**: Must use `http://10.0.2.2:8080/` (not localhost!)
- **Physical device**: Use your computer's IP address
- **Verify backend**: Visit `http://localhost:8080/test_connection.php` in browser

### Database connection failed
- **Wait longer**: Database takes ~10 seconds to initialize
- **Check containers**: `docker-compose ps` - both should be "Up"
- **Restart**: `docker-compose restart`

## File Changes and Hot Reload

### Backend PHP Files
Changes are **instant**! The `backend/` folder is mounted into the container:
1. Edit any `.php` file
2. Refresh browser or re-submit from app
3. Changes take effect immediately

### Database Schema Changes
If you modify `setup_database.sql`:
```bash
./stop.sh
docker-compose down -v  # Remove old database
./start.sh              # Recreate everything
```

## Data Persistence

Your customer data is stored in a Docker volume named `mysql_data`:
- **Survives container restarts**: `./stop.sh` and `./start.sh` keeps data
- **Clean slate**: `docker-compose down -v` deletes all data

## Production Deployment

For deploying to a real server:

1. **Update passwords** in `docker-compose.yml`
2. **Add HTTPS** using a reverse proxy (nginx/Traefik)
3. **Disable debug** in `backend/config.php`:
   ```php
   error_reporting(0);
   ini_set('display_errors', 0);
   ```
4. **Restrict CORS** in `backend/config.php`
5. **Use environment variables** for sensitive data

## Advantages of Docker

✅ **No manual installation** - No XAMPP, MAMP, or MySQL setup  
✅ **Consistent environment** - Works same on Mac, Windows, Linux  
✅ **Isolated** - Doesn't interfere with other projects  
✅ **Easy cleanup** - Just delete containers when done  
✅ **Portable** - Share exact same setup with team  
✅ **Quick reset** - Start fresh in seconds  

## Next Steps

- Read [README.md](README.md) for app features and API docs
- Check [QUICKSTART.md](QUICKSTART.md) for additional guidance
- Customize form fields in Android app
- Add more API endpoints
- Deploy to production server

## Need Help?

Check the logs for errors:
```bash
./logs.sh
```

View running containers:
```bash
docker ps
```

Enter PHP container:
```bash
docker exec -it customer_api bash
```

Enter MySQL container:
```bash
docker exec -it customer_db bash
```

---

**Ready to go?** Just run `./start.sh` and you're live! 🚀
