# AutoMate - Vehicle Inspection Management System

**Modern rebuild of the original AutoMate vehicle inspection app** with enhanced features, better UI/UX, and robust database backend.

## 🎯 What is AutoMate?

AutoMate is a comprehensive **vehicle inspection and NCT (National Car Test) management system** designed for mechanics, fitters, and automotive service centers. It streamlines the vehicle inspection process, stores inspection history, and generates detailed reports.

## ✨ Features

### Original AutoMate Features (Rebuilt & Improved):
- ✅ **Multi-Step Vehicle Inspection Workflow**
  - Wipers & Horn testing
  - Complete lighting system check (Front/Rear/Side lights)
  - Comprehensive tyre inspection (all 4 wheels + spare)
  - Brake system assessment (Pads & Disks)
  - Wheel alignment verification

- 👥 **Mechanic/Fitter Management**
  - Add and manage mechanics
  - Track who performed inspections
  - Role-based access (mechanic/fitter/admin)

- 🚗 **Vehicle Registry**
  - Track vehicle registration details
  - NCT due dates
  - Mileage history
  - Vehicle make/model/year

- 📊 **Inspection History & Reports**
  - Complete inspection audit trail
  - Pass/Fail/Advisory status tracking
  - Detailed notes for each inspection item
  - Historical comparisons

### New Improvements:
- 🗄️ **MySQL Database Backend** (instead of local storage)
- 🔒 **Secure API** with proper validation
- 📱 **Modern Material Design UI**
- 💾 **Cloud Storage** - Access from multiple devices
- 📈 **Analytics & Reporting** capabilities
- 🚀 **Better Performance** - Native Kotlin vs hybrid
- 🎨 **Cleaner, more intuitive interface**

## 🏗️ Architecture

### Backend (PHP + MySQL)
```
backend/
├── config.php                  # Database configuration
├── setup_automate_db.sql      # Database schema
├── api_inspections.php        # Inspection CRUD operations
├── api_mechanics.php          # Mechanics management
└── submit_customer.php        # Legacy customer form endpoint
```

### Database Schema

**Tables:**
- `mechanics` - Mechanic/fitter profiles
- `vehicles` - Vehicle registry
- `vehicle_inspections` - Main inspection records
- `inspection_history` - Audit log
- `customers` - Customer contact data (legacy)

**Views:**
- `inspection_summary` - Quick overview of inspections

### Android App (Kotlin)
```
app/src/main/
├── java/com/automate/customerform/
│   ├── models/
│   │   └── VehicleInspection.kt    # Data models
│   ├── MainActivity.kt              # Main activity
│   ├── ApiService.kt                # API interface
│   └── RetrofitClient.kt            # HTTP client
└── res/
    ├── layout/
    │   └── activity_main.xml        # UI layouts
    └── values/
        ├── strings.xml
        └── colors.xml
```

## 📊 Database Schema Details

### Vehicle Inspections Table
Stores comprehensive inspection data:
- **Vehicle Info**: Registration, NCT date, mileage
- **Basic Checks**: Wipers, horn (pass/fail/advisory)
- **Lights**: Front, rear, side lights status
- **Tyres**: Tread depth (mm), pressure (PSI), condition for all 4 wheels
- **Brakes**: Front/rear pads and disks status
- **Alignment**: Wheel alignment status
- **Notes**: Detailed notes for each inspection item
- **Status**: Draft/Completed/Submitted

### Mechanics Table
Manages service personnel:
- Name, email, phone
- Role (mechanic/fitter/admin)
- Active status
- Created/Updated timestamps

### Vehicles Table
Vehicle registry:
- Registration number (unique)
- NCT due date
- Current mileage
- Make, model, year, color
- Service history

## 🚀 Quick Start

### 1. Start the Backend
```bash
cd /Users/kwhelan/Projects/automate

# Start Docker services
./start.sh
```

Backend runs on: **http://localhost:8080/**

### 2. Verify Database Setup
```bash
# Check tables exist
docker exec customer_db mysql -u customer_app -pcustomer123 customer_data -e "SHOW TABLES;"

# View sample mechanics
curl 'http://localhost:8080/api_mechanics.php?action=list' | jq .
```

### 3. Build Android App
```bash
# Open in Android Studio
# File → Open → /Users/kwhelan/Projects/automate

# OR build via command line:
./gradlew assembleDebug

# Install on emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## 📡 API Endpoints

### Inspections API (`api_inspections.php`)

#### Submit Inspection
```bash
POST /api_inspections.php?action=submit
Content-Type: application/json

{
  "vehicle_reg": "12D12345",
  "fitter_name": "John Smith",
  "mileage": 50000,
  "nct_date": "2026-12-31",
  "wipers": "pass",
  "horn": "pass",
  "front_lights": "pass",
  "rear_lights": "advisory",
  "side_lights": "pass",
  "driver_front_tread": 4.5,
  "driver_front_pressure": 32,
  "driver_front_condition": "good",
  "driver_rear_tread": 3.8,
  "driver_rear_pressure": 32,
  "driver_rear_condition": "worn",
  "passenger_front_tread": 4.2,
  "passenger_front_pressure": 32,
  "passenger_front_condition": "good",
  "passenger_rear_tread": 3.9,
  "passenger_rear_pressure": 32,
  "passenger_rear_condition": "worn",
  "spare_wheel": "pass",
  "front_pads": "pass",
  "rear_pads": "advisory",
  "front_disks": "pass",
  "rear_disks": "pass",
  "alignment": "pass",
  "overall_status": "pass",
  "status": "completed"
}
```

#### List Inspections
```bash
GET /api_inspections.php?action=list&limit=20&offset=0
GET /api_inspections.php?action=list&vehicle_reg=12D12345
```

#### Get Inspection Details
```bash
GET /api_inspections.php?action=get&id=1
```

### Mechanics API (`api_mechanics.php`)

#### List Mechanics
```bash
GET /api_mechanics.php?action=list
GET /api_mechanics.php?action=list&active_only=true
```

#### Add Mechanic
```bash
POST /api_mechanics.php?action=add
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@automate.com",
  "phone": "0871112222",
  "role": "mechanic"
}
```

## 🧪 Testing

### Test Inspection Submission
```bash
curl -X POST 'http://localhost:8080/api_inspections.php?action=submit' \
  -H "Content-Type: application/json" \
  -d '{
    "vehicle_reg": "TEST123",
    "fitter_name": "John Smith",
    "mileage": 25000,
    "wipers": "pass",
    "horn": "pass",
    "front_lights": "pass",
    "rear_lights": "pass",
    "side_lights": "pass",
    "driver_front_tread": 5.0,
    "driver_front_pressure": 32,
    "driver_front_condition": "good",
    "driver_rear_tread": 4.8,
    "driver_rear_pressure": 32,
    "driver_rear_condition": "good",
    "passenger_front_tread": 5.1,
    "passenger_front_pressure": 32,
    "passenger_front_condition": "good",
    "passenger_rear_tread": 4.9,
    "passenger_rear_pressure": 32,
    "passenger_rear_condition": "good",
    "spare_wheel": "pass",
    "front_pads": "pass",
    "rear_pads": "pass",
    "front_disks": "pass",
    "rear_disks": "pass",
    "alignment": "pass",
    "overall_status": "pass",
    "status": "completed"
  }' | jq .
```

### View Inspection Data
```bash
# View all inspections
docker exec customer_db mysql -u customer_app -pcustomer123 customer_data -e "SELECT * FROM inspection_summary;"

# View detailed inspection
docker exec customer_db mysql -u customer_app -pcustomer123 customer_data -e "SELECT * FROM vehicle_inspections WHERE id = 1\\G"
```

## 📱 Android App Usage

1. **Login Screen** - Enter mechanic credentials
2. **Vehicle Registration** - Enter reg, NCT date, mileage
3. **Step-by-Step Inspection**:
   - Wipers check
   - Horn check
   - Lights (front/rear/side)
   - Each tyre (tread depth, pressure)
   - Brake pads & disks
   - Alignment
4. **Review** - Review all inspection data
5. **Submit** - Send to database

## 🔧 Configuration

### Backend Configuration
Edit `backend/config.php`:
```php
define('DB_HOST', getenv('DB_HOST') ?: 'localhost');
define('DB_USER', getenv('DB_USER') ?: 'customer_app');
define('DB_PASS', getenv('DB_PASS') ?: 'customer123');
define('DB_NAME', getenv('DB_NAME') ?: 'customer_data');
```

### Android App Configuration
Edit `app/src/main/java/com/automate/customerform/RetrofitClient.kt`:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/" // Emulator
// or
private const val BASE_URL = "http://YOUR_IP:8080/" // Physical device
```

## 📈 Future Enhancements

- [ ] PDF Report Generation
- [ ] Photo Upload for damage documentation
- [ ] Push Notifications for NCT reminders
- [ ] Multi-language support
- [ ] Dark mode
- [ ] Offline mode with sync
- [ ] Customer portal access
- [ ] SMS/Email reports
- [ ] Integration with NCT booking systems
- [ ] Dashboard analytics

## 🆚 Original vs Improved

| Feature | Original AutoMate | Improved AutoMate |
|---------|------------------|-------------------|
| **Platform** | Hybrid (Cordova/HTML) | Native Kotlin |
| **Storage** | Local Storage | MySQL Database |
| **Performance** | Slow | Fast |
| **Offline** | Yes (limited) | Planned |
| **UI** | Basic jQuery Mobile | Material Design 3 |
| **API** | Appery.io | Custom PHP REST API |
| **Multi-device** | No | Yes |
| **Reports** | Limited | Comprehensive |
| **History** | Client-side only | Server-side audit log |

## 📝 License

This is a rebuilt version of the AutoMate vehicle inspection app with significant improvements and enhancements.

## 🤝 Contributing

This project is part of the automate repository:
- **GitHub**: https://github.com/kwhelan-Red/automate
- **Backend**: Docker + MySQL + PHP
- **Frontend**: Android (Kotlin)

---

**Built with ❤️ using Docker, MySQL, PHP, and Kotlin**
