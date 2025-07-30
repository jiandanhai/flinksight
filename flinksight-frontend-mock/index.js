const express = require('express');
const app = express();
app.use(express.json());

// 示例：GET /api/metric
app.get('/api/metric', (req, res) => {
    res.json([
        { id: 1, name: "CPU使用率", type: "gauge", value: 0.37, timestamp: Date.now() }
    ]);
});

// 示例：POST /api/login
app.post('/api/login', (req, res) => {
    res.json({ token: "mock-jwt-token", user: { id: 1, username: "demo" } });
});

app.listen(3000, () => console.log('Mock API listening on 3000'));
