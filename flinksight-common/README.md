# Flinksight Common Module

- 所有模块通用的DTO、枚举、常量、工具包
- 无Spring依赖，JDK17+
- 任何业务模块均可依赖，适合SaaS/私有化分层部署

# 目录结构
    flinksight-common/
    ├── src/main/java/com/flinksight/common/
    │   ├── dto/
    │   │   ├── UserDTO.java
    │   │   ├── RoleDTO.java
    │   │   ├── ClusterDTO.java
    │   │   ├── JobDTO.java
    │   │   ├── JobMetricDTO.java
    │   │   ├── JobLogDTO.java
    │   │   ├── AlertDTO.java
    │   │   ├── AlertRuleDTO.java
    │   │   ├── TicketDTO.java
    │   │   ├── AuditLogDTO.java
    │   │   ├── TenantDTO.java
    │   │   └── PermissionDTO.java
    │   ├── enums/
    │   │   ├── UserStatusEnum.java
    │   │   ├── JobStatusEnum.java
    │   │   ├── ClusterTypeEnum.java
    │   │   ├── AlertLevelEnum.java
    │   │   ├── AlertStatusEnum.java
    │   │   ├── TicketStatusEnum.java
    │   │   └── PermissionTypeEnum.java
    │   ├── constants/
    │   │   └── CommonConstants.java
    │   ├── utils/
    │   │   ├── DateUtils.java
    │   │   ├── IdGenUtils.java
    │   │   ├── PasswordUtils.java
    │   │   └── JsonUtils.java
    ├── pom.xml
    └── README.md
## 公共模块说明

本模块包含所有业务子系统通用的 DTO（数据传输对象）、VO（前端展示对象）、通用工具类（如密码加密、时间工具、常量枚举等），
为 `flinksight-backend`、`flinksight-flink-job`、`flinksight-spark-job` 等所有服务提供零依赖的通用 Java 代码基础。

- 代码100%无Spring依赖，可用于Java主程序、Flink、Spark等纯JVM环境。
- DTO/VO命名与后端主表、接口字段一一对应。
- 工具类（如 `PasswordUtils`, `DateUtils`）适用于所有模块，方便统一加密、时间格式化等操作。
- 所有类和字段均有详细注释，便于自动生成接口文档。

**建议所有业务逻辑与采集任务的参数/结构均只依赖本模块，减少外部依赖。**