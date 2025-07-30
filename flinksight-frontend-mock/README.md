# Flinksight Frontend Mock

    flinksight-frontend-mock/
    ├── mock-api.js
    ├── package.json
    ├── README.md

## Mock API服务
- 服务用于前端联调，支持所有主表REST接口、批量CRUD、自动生成假数据，支持多租户请求头（X-Tenant-Id）
- Node+Express+MockJS，所有主表自动生成批量假数据，支持CRUD、分页、ID检索等全量接口
- 支持多租户（Header:X-Tenant-Id），方便前端开发和自动化测试
- 启动快、接口全，支持RESTful风格
- 可自定义各主表结构、关联与返回内容，便于前后端联调

### 启动方式
- 启动：`npm install && npm start`
- 访问：`http://localhost:3001/api/user/list`、`/api/job/list` 等
### 常见Mock接口
- /api/user/list
- /api/job/list
- /api/alert/list
- /api/cluster/list
- ...其它所有主表
