/**
 * Flinksight Mock API 服务（Express+MockJS）
 * 适合本地开发/前后端联调，包含 SSO、主表 CRUD、分页、搜索等。
 */
const express = require('express');
const bodyParser = require('body-parser');
const { mock, Random } = require('mockjs');

const app = express();
app.use(bodyParser.json());


// --- SSO OAuth2 Mock ---
app.get('/oauth2/authorize', (req, res) => {
    const { redirect_uri, state } = req.query;
    // 模拟 SSO 登录页，这里直接302带code跳转
    const code = 'mock-auth-code-123'; // 模拟返回的授权码
    let url = `${redirect_uri}?code=${code}`;
    if (state) url += `&state=${encodeURIComponent(state)}`;
    res.redirect(url);
});

app.post('/oauth2/token', (req, res) => {
    res.json({
        access_token: 'mock-access-token',
        token_type: 'Bearer',
        expires_in: 3600,
        refresh_token: 'mock-refresh-token'
    });
});
app.get('/oauth2/userinfo', (req, res) => {
    res.json({
        sub: 'mock-sso-id-001',
        preferred_username: 'mockuser',
        name: '测试用户',
        avatar: 'http://localhost:4000/avatar.png'
    });
});

// --- 通用主表数据 ---
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
const users = generateList('user', 10);
const roles = generateList('role', 5);
// ...其余主表同上...

// --- 通用 CRUD API ---
app.get('/api/:type/list', (req, res) => {
    const { type } = req.params;
    let data = [];
    switch (type) {
        case 'user': data = users; break;
        case 'role': data = roles; break;
        // ...同理其它表
    }
    res.json({ success: true, data });
});
app.get('/api/:type/:id', (req, res) => {
    const { type, id } = req.params;
    let data = null;
    switch (type) {
        case 'user': data = users.find(x => x.id == id); break;
        case 'role': data = roles.find(x => x.id == id); break;
        // ...其它同理
    }
    res.json({ success: !!data, data });
});
app.post('/api/:type/create', (req, res) => {
    res.json({ success: true, data: req.body });
});
app.put('/api/:type/update', (req, res) => {
    res.json({ success: true, data: req.body });
});
app.delete('/api/:type/:id', (req, res) => {
    res.json({ success: true });
});

// --- 启动 Mock 服务，只保留一次 listen ---
const PORT = 4000;
app.listen(PORT, () => {
    console.log(`Flinksight Mock API (含SSO) running at http://localhost:${PORT}`);
});
