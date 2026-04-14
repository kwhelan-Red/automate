<?php
/**
 * Vehicle Inspections API
 *
 * Handles CRUD operations for vehicle inspections
 */

require_once 'config.php';

// Only accept POST and GET requests
if (!in_array($_SERVER['REQUEST_METHOD'], ['POST', 'GET'])) {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed. Use POST or GET.'
    ]);
    exit();
}

// Get database connection
$conn = getDbConnection();

// Route based on action parameter
$action = isset($_GET['action']) ? $_GET['action'] : (isset($_POST['action']) ? $_POST['action'] : '');

switch ($action) {
    case 'submit':
        submitInspection($conn);
        break;

    case 'list':
        listInspections($conn);
        break;

    case 'get':
        getInspection($conn);
        break;

    case 'update':
        updateInspection($conn);
        break;

    default:
        http_response_code(400);
        echo json_encode([
            'success' => false,
            'message' => 'Invalid action. Use: submit, list, get, or update'
        ]);
}

$conn->close();

/**
 * Submit a new vehicle inspection
 */
function submitInspection($conn) {
    // Get JSON input
    $json_data = file_get_contents('php://input');
    $data = json_decode($json_data, true);

    if (!$data) {
        http_response_code(400);
        echo json_encode(['success' => false, 'message' => 'Invalid JSON data']);
        return;
    }

    // Required fields validation
    $required = ['vehicle_reg', 'fitter_name', 'mileage'];
    foreach ($required as $field) {
        if (empty($data[$field])) {
            http_response_code(400);
            echo json_encode(['success' => false, 'message' => "Missing required field: $field"]);
            return;
        }
    }

    // Insert or update vehicle
    $vehicle_reg = trim(strtoupper($data['vehicle_reg']));
    $stmt = $conn->prepare("
        INSERT INTO vehicles (registration, nct_due_date, mileage)
        VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE
            mileage = VALUES(mileage),
            nct_due_date = VALUES(nct_due_date),
            updated_at = NOW()
    ");

    $nct_date = !empty($data['nct_date']) ? $data['nct_date'] : null;
    $stmt->bind_param("ssi", $vehicle_reg, $nct_date, $data['mileage']);
    $stmt->execute();
    $stmt->close();

    // Get vehicle ID
    $result = $conn->query("SELECT id FROM vehicles WHERE registration = '$vehicle_reg'");
    $vehicle_id = $result->fetch_assoc()['id'];

    // Insert inspection
    $stmt = $conn->prepare("
        INSERT INTO vehicle_inspections (
            vehicle_id, vehicle_reg, fitter_name, mechanic_name, nct_date, mileage,
            wipers, wipers_notes, horn, horn_notes,
            front_lights, front_lights_notes,
            rear_lights, rear_lights_notes,
            side_lights, side_lights_notes,
            driver_front_tread, driver_front_pressure, driver_front_condition, driver_front_notes,
            driver_rear_tread, driver_rear_pressure, driver_rear_condition, driver_rear_notes,
            passenger_front_tread, passenger_front_pressure, passenger_front_condition, passenger_front_notes,
            passenger_rear_tread, passenger_rear_pressure, passenger_rear_condition, passenger_rear_notes,
            spare_wheel, spare_wheel_notes,
            front_pads, front_pads_notes,
            rear_pads, rear_pads_notes,
            front_disks, front_disks_notes,
            rear_disks, rear_disks_notes,
            alignment, alignment_notes,
            overall_status, general_notes, status
        ) VALUES (
            ?, ?, ?, ?, ?, ?,
            ?, ?, ?, ?,
            ?, ?, ?, ?, ?, ?,
            ?, ?, ?, ?,
            ?, ?, ?, ?,
            ?, ?, ?, ?,
            ?, ?, ?, ?,
            ?, ?,
            ?, ?, ?, ?, ?, ?, ?, ?,
            ?, ?, ?, ?, ?
        )
    ");

    $mechanic_name = $data['mechanic_name'] ?? null;
    $status = $data['status'] ?? 'completed';
    $overall_status = $data['overall_status'] ?? 'pass';

    $stmt->bind_param("issssississsssdississdississdississsssssssssssssss",
        $vehicle_id, $vehicle_reg, $data['fitter_name'], $mechanic_name, $nct_date, $data['mileage'],
        $data['wipers'], $data['wipers_notes'] ?? null,
        $data['horn'], $data['horn_notes'] ?? null,
        $data['front_lights'], $data['front_lights_notes'] ?? null,
        $data['rear_lights'], $data['rear_lights_notes'] ?? null,
        $data['side_lights'], $data['side_lights_notes'] ?? null,
        $data['driver_front_tread'] ?? null, $data['driver_front_pressure'] ?? null,
        $data['driver_front_condition'], $data['driver_front_notes'] ?? null,
        $data['driver_rear_tread'] ?? null, $data['driver_rear_pressure'] ?? null,
        $data['driver_rear_condition'], $data['driver_rear_notes'] ?? null,
        $data['passenger_front_tread'] ?? null, $data['passenger_front_pressure'] ?? null,
        $data['passenger_front_condition'], $data['passenger_front_notes'] ?? null,
        $data['passenger_rear_tread'] ?? null, $data['passenger_rear_pressure'] ?? null,
        $data['passenger_rear_condition'], $data['passenger_rear_notes'] ?? null,
        $data['spare_wheel'], $data['spare_wheel_notes'] ?? null,
        $data['front_pads'], $data['front_pads_notes'] ?? null,
        $data['rear_pads'], $data['rear_pads_notes'] ?? null,
        $data['front_disks'], $data['front_disks_notes'] ?? null,
        $data['rear_disks'], $data['rear_disks_notes'] ?? null,
        $data['alignment'], $data['alignment_notes'] ?? null,
        $overall_status, $data['general_notes'] ?? null, $status
    );

    if ($stmt->execute()) {
        $inspection_id = $stmt->insert_id;

        // Log to history
        $conn->query("INSERT INTO inspection_history (inspection_id, action, performed_by)
                     VALUES ($inspection_id, 'created', '{$data['fitter_name']}')");

        http_response_code(201);
        echo json_encode([
            'success' => true,
            'message' => 'Inspection submitted successfully',
            'inspection_id' => $inspection_id
        ]);
    } else {
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Failed to submit inspection: ' . $stmt->error
        ]);
    }

    $stmt->close();
}

/**
 * Get list of inspections
 */
function listInspections($conn) {
    $limit = isset($_GET['limit']) ? (int)$_GET['limit'] : 50;
    $offset = isset($_GET['offset']) ? (int)$_GET['offset'] : 0;
    $vehicle_reg = isset($_GET['vehicle_reg']) ? $_GET['vehicle_reg'] : null;

    $sql = "SELECT * FROM inspection_summary ORDER BY created_at DESC LIMIT ? OFFSET ?";

    if ($vehicle_reg) {
        $sql = "SELECT * FROM inspection_summary WHERE vehicle_reg = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sii", $vehicle_reg, $limit, $offset);
    } else {
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ii", $limit, $offset);
    }

    $stmt->execute();
    $result = $stmt->get_result();

    $inspections = [];
    while ($row = $result->fetch_assoc()) {
        $inspections[] = $row;
    }

    echo json_encode([
        'success' => true,
        'inspections' => $inspections,
        'count' => count($inspections)
    ]);

    $stmt->close();
}

/**
 * Get detailed inspection by ID
 */
function getInspection($conn) {
    $id = isset($_GET['id']) ? (int)$_GET['id'] : 0;

    if ($id <= 0) {
        http_response_code(400);
        echo json_encode(['success' => false, 'message' => 'Invalid inspection ID']);
        return;
    }

    $stmt = $conn->prepare("SELECT * FROM vehicle_inspections WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        echo json_encode([
            'success' => true,
            'inspection' => $row
        ]);
    } else {
        http_response_code(404);
        echo json_encode([
            'success' => false,
            'message' => 'Inspection not found'
        ]);
    }

    $stmt->close();
}

/**
 * Update existing inspection
 */
function updateInspection($conn) {
    // Get JSON input
    $json_data = file_get_contents('php://input');
    $data = json_decode($json_data, true);

    if (!isset($data['id'])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'message' => 'Inspection ID required']);
        return;
    }

    // Build dynamic update query based on provided fields
    // (simplified version - in production you'd want more robust handling)

    $stmt = $conn->prepare("UPDATE vehicle_inspections SET status = ?, updated_at = NOW() WHERE id = ?");
    $status = $data['status'] ?? 'completed';
    $stmt->bind_param("si", $status, $data['id']);

    if ($stmt->execute()) {
        echo json_encode([
            'success' => true,
            'message' => 'Inspection updated successfully'
        ]);
    } else {
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Failed to update inspection'
        ]);
    }

    $stmt->close();
}
?>
