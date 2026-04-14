# Quick Start Guide

Get your Customer Form app running in 10 minutes!

## 1. Setup Backend (5 minutes)

### Using XAMPP (Recommended for beginners)

1. **Install XAMPP**: Download from [apachefriends.org](https://www.apachefriends.org/)

2. **Start Services**:
   - Open XAMPP Control Panel
   - Start "Apache" and "MySQL"

3. **Setup Database**:
   - Click "Admin" next to MySQL (opens phpMyAdmin)
   - Click "Import" tab
   - Choose file: `backend/setup_database.sql`
   - Click "Go"

4. **Copy Backend Files**:
   ```bash
   # Mac/Linux
   cp -r backend /Applications/XAMPP/htdocs/api

   # Windows
   xcopy backend C:\xampp\htdocs\api\ /E /I
   ```

5. **Configure Database**:
   - Edit `backend/config.php`
   - Set: `DB_USER` = `root`, `DB_PASS` = `""` (empty for XAMPP default)

6. **Test**: Visit `http://localhost/api/test_connection.php`
   - Should show "Database connection successful!"

## 2. Setup Android App (5 minutes)

1. **Open in Android Studio**:
   - File → Open → Select `/Users/kwhelan/Projects/automate`
   - Wait for Gradle sync

2. **Configure API URL**:
   - Open: `app/src/main/java/com/automate/customerform/RetrofitClient.kt`
   - Line 14: Keep `http://10.0.2.2/api/` for emulator
   - Or use `http://YOUR_IP/api/` for physical device

3. **Run the App**:
   - Click green "Run" button
   - Select device/emulator
   - Wait for installation

## 3. Test It!

1. **Fill the form**:
   - Name: Test User
   - Email: test@example.com
   - Phone: 1234567890
   - Company: Test Co
   - Notes: This is a test

2. **Submit** and watch for success message!

3. **Verify in Database**:
   - Open phpMyAdmin: `http://localhost/phpmyadmin`
   - Database: `customer_data`
   - Table: `customers`
   - You should see your test entry!

## Common Issues

### "Cannot connect to server"
- **Fix**: Make sure XAMPP Apache is running
- Check: Green indicator in XAMPP Control Panel

### "Database connection failed"  
- **Fix**: Make sure XAMPP MySQL is running
- Check: `config.php` has correct credentials

### "App crashes on submit"
- **Fix**: Check BASE_URL in `RetrofitClient.kt`
- Emulator must use `10.0.2.2` not `localhost`

### Physical device can't connect
- **Fix 1**: Phone and computer on same WiFi
- **Fix 2**: Update BASE_URL with computer's IP address
  - Mac: `ifconfig | grep inet`
  - Windows: `ipconfig`
  - Use something like `http://192.168.1.100/api/`

## What's Next?

- Read full [README.md](README.md) for advanced configuration
- Deploy to a real web server
- Customize the form fields
- Add more features!

## Need Help?

Check the logs:
- **Android**: Logcat in Android Studio
- **PHP**: XAMPP → Logs → php_error_log
- **MySQL**: XAMPP → Logs → mysql_error_log
