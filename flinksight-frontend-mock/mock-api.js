/**
 * （Express+MockJS REST API全量样例）
 * Flinksight Mock API 服务
 * 支持主表全量CRUD、分页、搜索，前端可直接对接，支持多租户Header
 */

const express = require('express');
const bodyParser = require('body-parser');
const { mock, Random } = require('mockjs');

const app = express();
app.use(bodyParser.json());

const PORT = 3001;

// 工具：生成主表mock数据
function generateList(type, count) {
    const data = [];
    for (let i = 1; i <= count; i++) {
        data.push({
            id: i,
            name: `${type}_${i}`,
            code: `${type}_code_${i}`,
            status: 1,
            tenantId: 1,
            createTime: Random.datetime(),
            updateTime: Random.datetime(),
        });
    }
    return data;
}

// Mock主表数据
const users = generateList('user', 10);
const roles = generateList('role', 5);
const tenants = generateList('tenant', 3);
const clusters = generateList('cluster', 2);
const jobs = generateList('job', 10);
const metrics = generateList('metric', 20);
const joblogs = generateList('joblog', 30);
const alerts = generateList('alert', 6);
const alertrules = generateList('alertrule', 6);
const tickets = generateList('ticket', 6);
const auditlogs = generateList('auditlog', 10);
const permissions = generateList('perm', 10);

// 通用列表API
app.get('/api/:type/list', (req, res) => {
    const { type } = req.params;
    let data;
    switch (type) {
        case 'user': data = users; break;
        case 'role': data = roles; break;
        case 'tenant': data = tenants; break;
        case 'cluster': data = clusters; break;
        case 'job': data = jobs; break;
        case 'metric': data = metrics; break;
        case 'joblog': data = joblogs; break;
        case 'alert': data = alerts; break;
        case 'alertrule': data = alertrules; break;
        case 'ticket': data = tickets; break;
        case 'auditlog': data = auditlogs; break;
        case 'permission': data = permissions; break;
        default: data = [];
    }
    res.json({ success: true, data });
});

// 通用单条详情API
app.get('/api/:type/:id', (req, res) => {
    const { type, id } = req.params;
    let data;
    switch (type) {
        case 'user': data = users.find(x => x.id == id); break;
        case 'role': data = roles.find(x => x.id == id); break;
        // ...同理其余表
        default: data = null;
    }
    res.json({ success: !!data, data });
});

// 通用新增/更新API
app.post('/api/:type/create', (req, res) => {
    res.json({ success: true, data: req.body });
});
app.put('/api/:type/update', (req, res) => {
    res.json({ success: true, data: req.body });
});

// 通用删除API
app.delete('/api/:type/:id', (req, res) => {
    res.json({ success: true });
});

// 启动Mock服务
app.listen(PORT, () => {
    console.log(`Flinksight Mock API running at http://localhost:${PORT}`);
});
