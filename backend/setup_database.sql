-- Database setup for Customer Form App
-- Run this SQL to create the database and table

-- Create database
CREATE DATABASE IF NOT EXISTS customer_data
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE customer_data;

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    company VARCHAR(100) DEFAULT NULL,
    notes TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Optional: Create a user for the application (more secure than using root)
-- Replace 'your_password' with a strong password
-- CREATE USER 'customer_app'@'localhost' IDENTIFIED BY 'your_password';
-- GRANT SELECT, INSERT, UPDATE ON customer_data.* TO 'customer_app'@'localhost';
-- FLUSH PRIVILEGES;

-- Display table structure
DESCRIBE customers;

-- Show all customers (for testing)
-- SELECT * FROM customers ORDER BY created_at DESC;
