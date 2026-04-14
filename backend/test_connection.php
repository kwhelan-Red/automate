<?php
/**
 * Test Database Connection
 *
 * Run this file in your browser to verify database connection works
 * Example: http://localhost/api/test_connection.php
 */

require_once 'config.php';

echo "<h1>Database Connection Test</h1>";

try {
    $conn = getDbConnection();
    echo "<p style='color: green;'>✓ Database connection successful!</p>";

    // Test if customers table exists
    $result = $conn->query("SHOW TABLES LIKE 'customers'");
    if ($result->num_rows > 0) {
        echo "<p style='color: green;'>✓ Customers table exists</p>";

        // Count existing customers
        $count_result = $conn->query("SELECT COUNT(*) as count FROM customers");
        $count = $count_result->fetch_assoc()['count'];
        echo "<p>Total customers in database: <strong>$count</strong></p>";
    } else {
        echo "<p style='color: orange;'>⚠ Customers table not found. Run setup_database.sql first.</p>";
    }

    $conn->close();

} catch (Exception $e) {
    echo "<p style='color: red;'>✗ Error: " . $e->getMessage() . "</p>";
}

echo "<hr>";
echo "<p><strong>Configuration:</strong></p>";
echo "<ul>";
echo "<li>Host: " . DB_HOST . "</li>";
echo "<li>Database: " . DB_NAME . "</li>";
echo "<li>User: " . DB_USER . "</li>";
echo "</ul>";
?>
