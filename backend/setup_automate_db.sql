-- AutoMate Vehicle Inspection Database Schema
-- Enhanced database for vehicle inspection management

USE customer_data;

-- Mechanics/Fitters table
CREATE TABLE IF NOT EXISTS mechanics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    role ENUM('mechanic', 'fitter', 'admin') DEFAULT 'mechanic',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    registration VARCHAR(20) NOT NULL UNIQUE,
    nct_due_date DATE,
    last_service_date DATE,
    mileage INT DEFAULT 0,
    make VARCHAR(50),
    model VARCHAR(50),
    year INT,
    color VARCHAR(30),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_registration (registration),
    INDEX idx_nct_due (nct_due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Vehicle Inspections table
CREATE TABLE IF NOT EXISTS vehicle_inspections (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT,
    vehicle_reg VARCHAR(20) NOT NULL,
    mechanic_id INT,
    fitter_name VARCHAR(100) NOT NULL,
    mechanic_name VARCHAR(100),

    -- Vehicle Info at time of inspection
    nct_date DATE,
    mileage INT NOT NULL,

    -- Basic Checks
    wipers ENUM('pass', 'fail', 'advisory') NOT NULL,
    wipers_notes TEXT,
    horn ENUM('pass', 'fail', 'advisory') NOT NULL,
    horn_notes TEXT,

    -- Lights
    front_lights ENUM('pass', 'fail', 'advisory') NOT NULL,
    front_lights_notes TEXT,
    rear_lights ENUM('pass', 'fail', 'advisory') NOT NULL,
    rear_lights_notes TEXT,
    side_lights ENUM('pass', 'fail', 'advisory') NOT NULL,
    side_lights_notes TEXT,

    -- Tyres
    driver_front_tread DECIMAL(4,2), -- mm
    driver_front_pressure INT, -- PSI
    driver_front_condition ENUM('good', 'worn', 'replace') NOT NULL,
    driver_front_notes TEXT,

    driver_rear_tread DECIMAL(4,2),
    driver_rear_pressure INT,
    driver_rear_condition ENUM('good', 'worn', 'replace') NOT NULL,
    driver_rear_notes TEXT,

    passenger_front_tread DECIMAL(4,2),
    passenger_front_pressure INT,
    passenger_front_condition ENUM('good', 'worn', 'replace') NOT NULL,
    passenger_front_notes TEXT,

    passenger_rear_tread DECIMAL(4,2),
    passenger_rear_pressure INT,
    passenger_rear_condition ENUM('good', 'worn', 'replace') NOT NULL,
    passenger_rear_notes TEXT,

    spare_wheel ENUM('pass', 'fail', 'advisory', 'none') NOT NULL,
    spare_wheel_notes TEXT,

    -- Brakes
    front_pads ENUM('pass', 'fail', 'advisory') NOT NULL,
    front_pads_notes TEXT,
    rear_pads ENUM('pass', 'fail', 'advisory') NOT NULL,
    rear_pads_notes TEXT,
    front_disks ENUM('pass', 'fail', 'advisory') NOT NULL,
    front_disks_notes TEXT,
    rear_disks ENUM('pass', 'fail', 'advisory') NOT NULL,
    rear_disks_notes TEXT,

    -- Alignment
    alignment ENUM('pass', 'fail', 'advisory') NOT NULL,
    alignment_notes TEXT,

    -- Overall
    overall_status ENUM('pass', 'fail', 'advisory') NOT NULL,
    general_notes TEXT,
    status ENUM('draft', 'completed', 'submitted') DEFAULT 'draft',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE SET NULL,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE SET NULL,
    INDEX idx_vehicle_reg (vehicle_reg),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_overall_status (overall_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inspection History/Audit Log
CREATE TABLE IF NOT EXISTS inspection_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inspection_id INT NOT NULL,
    action VARCHAR(50) NOT NULL, -- created, updated, submitted, reviewed
    performed_by VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (inspection_id) REFERENCES vehicle_inspections(id) ON DELETE CASCADE,
    INDEX idx_inspection_id (inspection_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample mechanics
INSERT INTO mechanics (name, email, phone, role) VALUES
('John Smith', 'john.smith@automate.com', '0871234567', 'mechanic'),
('Mary Johnson', 'mary.johnson@automate.com', '0877654321', 'fitter'),
('Admin User', 'admin@automate.com', '0879999999', 'admin')
ON DUPLICATE KEY UPDATE name=name;

-- Create views for easier querying
CREATE OR REPLACE VIEW inspection_summary AS
SELECT
    vi.id,
    vi.vehicle_reg,
    vi.fitter_name,
    vi.mechanic_name,
    vi.mileage,
    vi.overall_status,
    vi.status,
    vi.created_at,
    CASE
        WHEN vi.wipers = 'fail' OR vi.horn = 'fail' OR vi.front_lights = 'fail'
             OR vi.rear_lights = 'fail' OR vi.side_lights = 'fail'
             OR vi.front_pads = 'fail' OR vi.rear_pads = 'fail'
             OR vi.front_disks = 'fail' OR vi.rear_disks = 'fail'
             OR vi.alignment = 'fail'
             OR vi.driver_front_condition = 'replace'
             OR vi.driver_rear_condition = 'replace'
             OR vi.passenger_front_condition = 'replace'
             OR vi.passenger_rear_condition = 'replace'
        THEN 'FAILED'
        WHEN vi.overall_status = 'advisory' THEN 'ADVISORY'
        ELSE 'PASSED'
    END as result
FROM vehicle_inspections vi;

DESCRIBE vehicle_inspections;
DESCRIBE mechanics;
DESCRIBE vehicles;
