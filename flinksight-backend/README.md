# flinksight-backend

## Spring Boot 后端API服务

- 覆盖用户/租户/角色/权限/集群/任务/指标/日志/报警/工单/审计全业务流
- 全量生产级 CRUD+批量+软删+分页，接口注释/Swagger/OpenAPI齐全
- 多租户（X-Tenant-Id）、RBAC（@Permission注解）、JWT认证、全局异常、统一返回结构
- 配置简单，兼容MySQL/PostgreSQL，支持数据库自动建表
- 一键本地启动/调试/部署，可对接主流CI/CD与K8S运维
- 所有主表、业务、接口均有测试样例和Mock脚本

### 快速启动

mvn clean package
java -jar target/flinksight-backend-*.jar
或用docker-compose一键启动


### 关键配置
application.yml: 数据库/JWT/日志
docker-compose.yml: 可直接对接MySQL和Mock API
Swagger UI: /swagger-ui/index.html

### 常见问题
端口冲突：修改application.yml的server.port
数据库建表失败：请检查MySQL连接与账号权限
权限/租户接口403：请确认前端Header传递X-Tenant-Id与有效Token