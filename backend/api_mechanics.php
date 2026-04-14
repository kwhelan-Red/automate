<?php
/**
 * Mechanics API
 *
 * Manage mechanics/fitters in the system
 */

require_once 'config.php';

// Get database connection
$conn = getDbConnection();

$action = isset($_GET['action']) ? $_GET['action'] : '';

switch ($action) {
    case 'list':
        listMechanics($conn);
        break;

    case 'add':
        addMechanic($conn);
        break;

    default:
        listMechanics($conn);
}

$conn->close();

function listMechanics($conn) {
    $active_only = isset($_GET['active_only']) && $_GET['active_only'] === 'true';

    $sql = "SELECT id, name, email, phone, role, active, created_at
            FROM mechanics ";

    if ($active_only) {
        $sql .= "WHERE active = TRUE ";
    }

    $sql .= "ORDER BY name ASC";

    $result = $conn->query($sql);

    $mechanics = [];
    while ($row = $result->fetch_assoc()) {
        $mechanics[] = $row;
    }

    echo json_encode([
        'success' => true,
        'mechanics' => $mechanics,
        'count' => count($mechanics)
    ]);
}

function addMechanic($conn) {
    $json_data = file_get_contents('php://input');
    $data = json_decode($json_data, true);

    if (empty($data['name'])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'message' => 'Name is required']);
        return;
    }

    $stmt = $conn->prepare("INSERT INTO mechanics (name, email, phone, role) VALUES (?, ?, ?, ?)");
    $role = $data['role'] ?? 'mechanic';
    $stmt->bind_param("ssss",
        $data['name'],
        $data['email'] ?? null,
        $data['phone'] ?? null,
        $role
    );

    if ($stmt->execute()) {
        http_response_code(201);
        echo json_encode([
            'success' => true,
            'message' => 'Mechanic added successfully',
            'mechanic_id' => $stmt->insert_id
        ]);
    } else {
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'message' => 'Failed to add mechanic: ' . $stmt->error
        ]);
    }

    $stmt->close();
}
?>
