# Flinksight 多模块大数据平台（企业级）

## 目录结构说明

    flinksight-root/
    ├── flinksight-common/ # 公共DTO、工具等（纯Java模块，无Spring依赖）
    ├── flinksight-backend/ # Spring Boot后端API服务（管理/指标/报警/用户/任务等）
    ├── flinksight-flink-job/ # Flink采集/指标/报警流任务
    ├── flinksight-spark-job/ # Spark流批任务
    ├── flinksight-frontend-mock/ # 前端Mock API（Express/JSON-server）
    ├── deploy/ # 一键部署（docker-compose、k8s模板等）
    ├── Jenkinsfile # CI/CD流水线脚本
    ├── test/ # 自动化测试与示例数据
    ├── README.md # 本文档
    └── pom.xml # 多模块父pom


## 模块说明

- **flinksight-common**  
  放置通用DTO、VO、工具类。所有模块可依赖，无业务逻辑。
  公共DTO/VO、工具类、密码工具、常量枚举，所有模块均依赖。
- **flinksight-backend**  
  Spring Boot服务，提供RESTful API（用户、指标、采集、报警等）。支持JWT鉴权、Kafka消费、异步任务、指标聚合/报警等。
  Spring Boot 3.x，包含所有主业务REST API（用户/租户/指标/报警/工单/审计/权限），带完整注解与异常处理，RBAC/多租户/Security/JWT一应俱全，默认集成MySQL。
- **flinksight-flink-job**  
  Flink流采集与计算任务，可对接Kafka、汇聚多源指标，聚合分析并推送报警到后端API。
  Flink任务采集、报警流转，支持Kafka/JDBC/API多种数据源Sink。样板任务即插即用，可根据实际业务拓展采集/规则流。
- **flinksight-spark-job**  
  Spark批处理与流式分析任务，可实现大批量数据离线计算/准实时处理。
  Spark批量采集/报警任务，支持定时离线批量运维，接口/数据结构与主表完全一致。
- **flinksight-frontend-mock**  
  Mock API服务器，方便前端或集成联调。支持Express、JSON-Server、或其他Node工具。
  Node+Express+MockJS，批量生成主表数据和所有API，前端/测试/联调无需后端即可开发
- **deploy/**  
- 包含docker-compose（本地一键起MySQL、Kafka、Zookeeper、后端等）、k8s（生产可用）、数据库初始化、Jenkins流水线等脚本与说明
- **test/**  
  HTTP自动化测试脚本（如Postman/VSCode HTTP）、批量模拟数据文件（json/csv）等。
  单元/集成自动化测试、Junit样例、批量数据脚本、CI集成回归。
  - **Jenkinsfile**  
    CI/CD流水线，支持多模块打包、单测、镜像、部署全流程。
    生产级CI/CD，自动build、测试、镜像、部署，零人工介入。

## 初始化&本地启动指引

1. **git clone ... 拉取代码/生成项目结构**
2. 服务器运行：修改 deploy/docker-compose.yml 和 application.yml 配置数据库等参数（如需）（本地运行：用IDEA或VSCode “Open” `flinksight-root` ）
3. 确认已安装JDK 17+、Maven 3.8+，Node.js（如需Mock前端）
4. docker-compose up -d 一键启动或者本地运行：`cd flinksight-root`，运行
5. 访问Swagger UI或Mock API进行接口联调与业务演示


# mvn clean install
6. 启动Flink/Spark Job：
- 参考各模块`job/`目录下`FlinkMainJob.java`/`SparkMainJob.java`
- 可在IDEA直接运行主类
7. 启动Mock API（任选）：
   cd flinksight-frontend-mock
   - 启动：`npm install && npm start`
   - 访问：`http://localhost:3001/api/user/list`、`/api/job/list` 等
8. 一键本地环境（需Docker）：
- docker部署：
   cd deploy
   docker-compose up -d
- Kubernetes部署:
   kubectl apply -f deploy/k8s/
9. 数据库初始化    
- mysql -u root -p < deploy/init-db.sql
  mysql -u root -p flinksight < test/test-data.sql

10. 用Postman/VSCode test/api.test.http 体验接口。
    访问后端API: http://localhost:8080/swagger-ui/index.html
    访问Mock API: http://localhost:3001/api/user/list
---

#二次开发/运维建议
- 数据库：建议使用MySQL 8.x，可自定义主从分片，支持软删与分区表。
- 接口与业务：所有接口均支持分页/批量/软删，字段、注释、权限/租户可直接对齐实际业务，定制化扩展友好。
- CI/CD：推荐Jenkins流水线与镜像仓库，全部业务与采集任务自动化发布，无须手工。
- 监控与审计：支持Prometheus/Alertmanager对接，所有指标可拓展推送至第三方。
- 前端开发：可使用前端Mock API快速联调，无须等待后端接口上线。
- 安全合规：全链路日志审计、权限最小化、所有敏感操作均有日志与告警。
- 高可用部署：生产建议K8S多副本与分布式部署，推荐配合LVS/Ingress做弹性伸缩。

# 联系方式/商业定制
- 如需更多功能（如报表、数据大屏、更多SaaS定制）、运维服务或深度二开，请联系团队 support@flinksight.com
- 可签署交付/保密/运维/培训/后续定制合同，源码、流程、运维体系皆合规可交付。

# 附录：FAQ与二次开发最佳实践
- 所有DTO、表结构、接口字段完全对齐业务需求，前后端解耦，强类型，注释齐全
- 权限/租户/审计/监控/指标/采集/报警/工单/运维 全流程打通
- 支持全自动测试与批量Mock，回归、上云、商用均零阻力
- 代码与文档均生产可交付，运维与业务团队均可独立交接

  flinksight-root/
  ├── flinksight-common/ # 公共DTO、工具等（纯Java模块，无Spring依赖）
  ├── flinksight-backend/ # Spring Boot后端API服务（管理/指标/报警/用户/任务等）
  ├── flinksight-flink-job/ # Flink采集/指标/报警流任务
  ├── flinksight-spark-job/ # Spark流批任务
  ├── flinksight-frontend-mock/ # 前端Mock API（Express/JSON-server）
  ├── deploy/ # 一键部署（docker-compose、k8s模板等）
  ├── Jenkinsfile # CI/CD流水线脚本
  ├── test/ # 自动化测试与示例数据
  ├── README.md # 本文档
  └── pom.xml # 多模块父pom

flinksight/
├── backend/                    # Spring Boot 3 控制面（K8s/CRD 渲染/PromQL/事件汇聚/审计）
├── frontend/                   # Next.js 控制台（作业/集群/告警/观测）
├── flink-ops/                  # Flink 生产采集/告警联动作业
├── spark-ops/                  # Spark 3.4 监听器 + Runner（Kafka 上报）
├── k8s/                        # CRD 模板、RBAC、监控与告警规则、Operator 安装
├── dashboards/                 # Grafana 面板（JSON）
├── ci/                         # CI（GitHub Actions）
└── README.md                   # 一键部署说明
### 1) 安装 Operator 与监控栈
./k8s/install-operators.sh
kubectl apply -f k8s/rbac/backend-rbac.yaml
kubectl apply -f k8s/monitoring/

### 2) 配置并启动 Backend/Frontend
cd backend && mvn -B -DskipTests package && docker build -t reg/ops-backend:1.0 .
# 部署后端到 K8s（自行创建 Deployment/Service/Ingress, SA=ops/backend）

### 3) 通过前端创建 Job/Cluster
- 前端：jobs/create 填写 JobSpec，提交后由后端渲染 CRD 并下发
- 观察 Prometheus/Grafana 面板与 Alertmanager 告警

### 4) Spark/Flink 作业上报
- Spark：为 Driver 挂上 FlinksightSparkListener（随 spark-ops 提供），Kafka 上报 job-metrics
- Flink：FlinkOpsJob 订阅 job-metrics, 执行动态规则与下沉






