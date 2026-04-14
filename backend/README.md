# Backend API Documentation

This directory contains the PHP backend for the Customer Form app.

## Files

- **config.php** - Database configuration and connection management
- **submit_customer.php** - Main API endpoint for receiving customer data
- **setup_database.sql** - Database schema and setup script
- **test_connection.php** - Database connection tester
- **.htaccess** - Apache security and configuration rules

## Setup

1. Configure database credentials in `config.php`
2. Run `setup_database.sql` to create the database
3. Copy all files to your web server
4. Test connection via `test_connection.php`

## Security Features

✓ Prepared statements (SQL injection protection)  
✓ Input validation and sanitization  
✓ Email format validation  
✓ CORS headers for mobile app access  
✓ Error handling with proper HTTP status codes  
✓ .htaccess file blocks access to sensitive files  

## API Endpoint

**POST** `/submit_customer.php`

Send JSON with customer data. Returns success/error response.

See main [README.md](../README.md) for complete API documentation.
