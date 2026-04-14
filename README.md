# Customer Form Android App

A modern Android application that collects customer data through a form and submits it to a web API backend.

## Features

- Clean, Material Design UI
- Form validation (name, email, phone required)
- Real-time error feedback
- Sends data to PHP backend API
- Stores customer information in MySQL database
- Loading states and success/error messages

## 🚀 Quick Start with Docker (Recommended!)

**Easiest way to get started** - Everything runs in containers, no manual setup needed!

```bash
# 1. Start the backend (one command!)
./start.sh

# 2. Open in Android Studio and click Run!
```

That's it! See [DOCKER-SETUP.md](DOCKER-SETUP.md) for complete Docker guide.

**Alternative Setup**: Manual installation with XAMPP/MAMP (see below)

## Project Structure

```
automate/
├── app/                           # Android application
│   ├── src/main/
│   │   ├── java/com/automate/customerform/
│   │   │   ├── MainActivity.kt           # Main activity with form logic
│   │   │   ├── CustomerData.kt          # Data models
│   │   │   ├── ApiService.kt            # Retrofit API interface
│   │   │   └── RetrofitClient.kt        # Retrofit configuration
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml    # Form layout
│   │   │   └── values/
│   │   │       ├── strings.xml
│   │   │       ├── colors.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── backend/                       # PHP backend API
│   ├── config.php                # Database configuration
│   ├── submit_customer.php       # Customer submission endpoint
│   ├── setup_database.sql        # Database schema
│   └── test_connection.php       # Database connection tester
├── build.gradle
├── settings.gradle
└── README.md
```

## Customer Form Fields

- **Name** (required) - Customer's full name
- **Email** (required) - Must be valid email format
- **Phone** (required) - Customer's phone number
- **Company** (optional) - Company name
- **Notes** (optional) - Additional notes or messages

## Setup Instructions

### Part 1: Backend Setup

#### Prerequisites
- PHP 7.4 or higher
- MySQL 5.7 or higher
- Web server (Apache/Nginx) or XAMPP/MAMP/WAMP

#### Step 1: Database Setup

1. Start your MySQL server

2. Run the database setup script:
```bash
mysql -u root -p < backend/setup_database.sql
```

Or manually create the database:
- Open phpMyAdmin or MySQL client
- Create a new database named `customer_data`
- Import `backend/setup_database.sql`

#### Step 2: Configure Database Connection

1. Edit `backend/config.php`:
```php
define('DB_HOST', 'localhost');
define('DB_USER', 'your_username');      // Your MySQL username
define('DB_PASS', 'your_password');      // Your MySQL password
define('DB_NAME', 'customer_data');
```

#### Step 3: Deploy Backend Files

**Option A: Using XAMPP/MAMP/WAMP**
1. Copy the `backend` folder to your web server directory:
   - XAMPP: `C:\xampp\htdocs\api\` (Windows) or `/Applications/XAMPP/htdocs/api/` (Mac)
   - MAMP: `/Applications/MAMP/htdocs/api/`
   - WAMP: `C:\wamp\www\api\`

**Option B: Using a Remote Server**
1. Upload `backend` files to your server (via FTP/SSH)
2. Place them in your web root or a subdirectory like `public_html/api/`

#### Step 4: Test Backend

1. Open your browser and visit:
   - Local: `http://localhost/api/test_connection.php`
   - Remote: `http://yourdomain.com/api/test_connection.php`

2. You should see:
   - ✓ Database connection successful!
   - ✓ Customers table exists

### Part 2: Android App Setup

#### Prerequisites
- Android Studio (latest version)
- JDK 17 or higher
- Android SDK (API level 24+)

#### Step 1: Open Project in Android Studio

1. Launch Android Studio
2. Click "Open" and select the `/Users/kwhelan/Projects/automate` folder
3. Wait for Gradle sync to complete

#### Step 2: Configure API Endpoint

1. Open `app/src/main/java/com/automate/customerform/RetrofitClient.kt`

2. Update the `BASE_URL`:

**For Android Emulator:**
```kotlin
private const val BASE_URL = "http://10.0.2.2/api/"
```

**For Physical Device (same WiFi network):**
```kotlin
private const val BASE_URL = "http://YOUR_COMPUTER_IP/api/"
```
Find your computer's IP:
- Windows: `ipconfig` in Command Prompt
- Mac/Linux: `ifconfig` in Terminal

**For Production:**
```kotlin
private const val BASE_URL = "https://yourdomain.com/api/"
```

#### Step 3: Build and Run

1. Connect an Android device or start an emulator
2. Click the "Run" button (green play icon) in Android Studio
3. Select your device
4. The app will install and launch

## Testing the App

### Test with Android Emulator

1. Make sure your local server (XAMPP/MAMP) is running
2. The emulator uses `10.0.2.2` to reach `localhost` on your computer
3. Fill in the form and click Submit
4. Check your database to see the new customer record

### Test with Physical Device

1. Connect your phone to the **same WiFi network** as your computer
2. Update `BASE_URL` with your computer's local IP address
3. Make sure your firewall allows incoming connections on port 80
4. Test the form submission

### Verify Data in Database

Check if data was saved:
```sql
SELECT * FROM customers ORDER BY created_at DESC LIMIT 10;
```

Or use phpMyAdmin to browse the `customers` table.

## API Documentation

### Endpoint: Submit Customer

**URL:** `POST /submit_customer.php`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "company": "Acme Corp",
  "notes": "Interested in product demo"
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Customer data saved successfully",
  "customer_id": 42
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Missing required fields: name, email"
}
```

## Troubleshooting

### App Cannot Connect to Server

1. **Check BASE_URL** - Verify it matches your server address
2. **Firewall** - Ensure port 80 is open
3. **Server Running** - Confirm Apache/Nginx is running
4. **Network** - Phone and computer on same WiFi (for local testing)
5. **Logs** - Check Logcat in Android Studio for error messages

### Database Connection Failed

1. **Credentials** - Verify DB_USER and DB_PASS in `config.php`
2. **MySQL Running** - Ensure MySQL service is started
3. **Database Exists** - Confirm `customer_data` database was created
4. **Permissions** - Check user has INSERT privileges

### Form Validation Errors

- **Email** - Must be valid format (user@domain.com)
- **Name** - Required, 2-100 characters
- **Phone** - Required field

## Security Notes

⚠️ **Important for Production:**

1. **Use HTTPS** - Never send data over HTTP in production
2. **Secure Passwords** - Use strong database passwords
3. **Input Validation** - Already implemented in PHP
4. **SQL Injection** - Protected via prepared statements
5. **Error Reporting** - Disable in production:
   ```php
   error_reporting(0);
   ini_set('display_errors', 0);
   ```
6. **CORS** - Restrict allowed origins in production
7. **Rate Limiting** - Add rate limiting to prevent abuse

## Database Schema

```sql
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    company VARCHAR(100) DEFAULT NULL,
    notes TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## Technologies Used

### Android App
- **Language:** Kotlin
- **Min SDK:** API 24 (Android 7.0)
- **Target SDK:** API 34 (Android 14)
- **UI:** Material Design Components, XML layouts
- **Networking:** Retrofit 2.9.0, OkHttp 4.11.0
- **JSON:** Gson
- **Async:** Kotlin Coroutines

### Backend
- **Language:** PHP 7.4+
- **Database:** MySQL 5.7+
- **API Format:** RESTful JSON API
- **Security:** Prepared statements, input validation

## Next Steps

- [ ] Add user authentication
- [ ] Implement data encryption
- [ ] Add offline support with local database
- [ ] Create admin dashboard to view submissions
- [ ] Add push notifications
- [ ] Implement data export (CSV/Excel)
- [ ] Add image upload for customer profile

## License

This project is open source and available for modification.

## Support

For issues or questions, check:
1. Logcat output in Android Studio
2. PHP error logs (`error_log`)
3. MySQL error logs
4. Browser console for API testing
