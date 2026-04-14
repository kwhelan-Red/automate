<?php
/**
 * Customer Data Submission API
 *
 * Receives customer data from Android app and stores it in database
 */

require_once 'config.php';

// Only accept POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use POST.'
    ]);
    exit();
}

// Get JSON input
$json_data = file_get_contents('php://input');
$data = json_decode($json_data, true);

// Validate input
if (!$data) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid JSON data'
    ]);
    exit();
}

// Required fields
$required_fields = ['name', 'email', 'phone'];
$missing_fields = [];

foreach ($required_fields as $field) {
    if (empty($data[$field])) {
        $missing_fields[] = $field;
    }
}

if (!empty($missing_fields)) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Missing required fields: ' . implode(', ', $missing_fields)
    ]);
    exit();
}

// Sanitize and validate data
$name = trim($data['name']);
$email = trim($data['email']);
$phone = trim($data['phone']);
$company = isset($data['company']) ? trim($data['company']) : '';
$notes = isset($data['notes']) ? trim($data['notes']) : '';

// Validate email format
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid email address'
    ]);
    exit();
}

// Validate name length
if (strlen($name) < 2 || strlen($name) > 100) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Name must be between 2 and 100 characters'
    ]);
    exit();
}

// Get database connection
$conn = getDbConnection();

try {
    // Prepare SQL statement
    $stmt = $conn->prepare(
        "INSERT INTO customers (name, email, phone, company, notes, created_at)
         VALUES (?, ?, ?, ?, ?, NOW())"
    );

    if (!$stmt) {
        throw new Exception('Failed to prepare statement');
    }

    // Bind parameters
    $stmt->bind_param("sssss", $name, $email, $phone, $company, $notes);

    // Execute statement
    if ($stmt->execute()) {
        $customer_id = $stmt->insert_id;

        http_response_code(201);
        echo json_encode([
            'success' => true,
            'message' => 'Customer data saved successfully',
            'customer_id' => $customer_id
        ]);
    } else {
        throw new Exception('Failed to insert data');
    }

    $stmt->close();

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Database error: ' . $e->getMessage()
    ]);
} finally {
    $conn->close();
}
?>
