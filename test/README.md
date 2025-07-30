# Flinksight 自动化测试

- `UserServiceTest.java`：基础业务服务自动化单测，mock数据测试软删、创建等。
- `test-data.sql`：主表样例数据，集成测试可直接导入。
- 推荐结合CI/CD脚本与TestContainers自动回归。
  test/
  ├── backend/
  │   ├── UserServiceTest.java
  │   ├── JobServiceTest.java
  │   └── ...
  ├── test-data.sql
  ├── README.md

# 自动化测试说明

- 所有主表/业务均配有基础单元测试（JUnit），覆盖创建、查询、软删、权限校验等核心业务流
- test/test-data.sql 提供全量主表演示数据，集成回归可直接导入
- 建议结合Jenkins/CI工具，回归每次提交的全部核心逻辑
- 推荐Mock API与后端服务并行测试，保障接口稳定与数据一致

---

## 运行方法

- 后端单元测试：`mvn test -pl flinksight-backend`
- Mock API接口测试：可用Postman或curl直接访问 /api/ 下所有接口
- 集成测试：导入test-data.sql，再通过SwaggerUI或测试脚本批量调用接口