<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Form API</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 20px;
            padding: 40px;
            max-width: 600px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
        }
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 32px;
        }
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-size: 16px;
        }
        .status {
            background: #10b981;
            color: white;
            padding: 12px 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            display: inline-block;
            font-weight: 600;
        }
        .section {
            margin-bottom: 25px;
        }
        .section h2 {
            color: #667eea;
            font-size: 18px;
            margin-bottom: 15px;
            border-bottom: 2px solid #e5e7eb;
            padding-bottom: 8px;
        }
        .endpoint {
            background: #f9fafb;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin-bottom: 12px;
            border-radius: 5px;
        }
        .endpoint-method {
            display: inline-block;
            background: #667eea;
            color: white;
            padding: 4px 10px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 700;
            margin-right: 10px;
        }
        .endpoint-url {
            color: #333;
            font-family: monospace;
            font-size: 14px;
        }
        .endpoint-desc {
            color: #666;
            font-size: 14px;
            margin-top: 8px;
        }
        a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }
        a:hover {
            text-decoration: underline;
        }
        .footer {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #e5e7eb;
            color: #999;
            font-size: 14px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🚀 Customer Form API</h1>
        <p class="subtitle">Backend API for Android Customer Form App</p>

        <div class="status">✅ API Server Running</div>

        <div class="section">
            <h2>📋 Available Endpoints</h2>

            <div class="endpoint">
                <span class="endpoint-method">POST</span>
                <span class="endpoint-url">/submit_customer.php</span>
                <div class="endpoint-desc">Submit customer data from mobile app</div>
            </div>

            <div class="endpoint">
                <span class="endpoint-method">GET</span>
                <span class="endpoint-url"><a href="test_connection.php">/test_connection.php</a></span>
                <div class="endpoint-desc">Test database connection and view stats</div>
            </div>
        </div>

        <div class="section">
            <h2>📱 Android App Configuration</h2>
            <div class="endpoint">
                <div class="endpoint-desc">
                    <strong>Emulator:</strong> <code>http://10.0.2.2:8080/</code><br>
                    <strong>Physical Device:</strong> <code>http://YOUR_IP:8080/</code>
                </div>
            </div>
        </div>

        <div class="section">
            <h2>🗄️ Database Info</h2>
            <div class="endpoint">
                <div class="endpoint-desc">
                    <strong>Database:</strong> customer_data<br>
                    <strong>Table:</strong> customers<br>
                    <strong>Status:</strong> Ready to receive data
                </div>
            </div>
        </div>

        <div class="footer">
            <p>Built with PHP, MySQL & Docker</p>
            <p style="margin-top: 5px;">
                <a href="https://github.com/kwhelan-Red/automate" target="_blank">View on GitHub</a>
            </p>
        </div>
    </div>
</body>
</html>
