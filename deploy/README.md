# Flinksight Deploy Module
    deploy/
    ├── docker-compose.yml
    ├── k8s/
    │   ├── flinksight-backend-deployment.yaml
    │   ├── flinksight-flink-job-deployment.yaml
    │   ├── mysql-deployment.yaml
    │   └── ...
    ├── init-db.sql
    ├── README.md
    └── Jenkinsfile
# 部署与运维指南

## Docker一键部署

1. 修改 deploy/docker-compose.yml 配置数据库与镜像名称（如有必要）
2. `docker-compose up -d` 即可一键启动全部依赖与主平台
3. 访问后端API和Swagger UI进行功能验证

## K8S高可用部署

1. 按照 deploy/k8s/ 下各yaml脚本，分步apply部署
2. 可选外部MySQL、高可用Ingress、持久化存储，按需配置
3. 推荐结合Prometheus/Alertmanager进行平台自监控

## Jenkins CI/CD

1. 拷贝根目录 Jenkinsfile 至自有Jenkins，配置SCM与凭证
2. 配合docker registry自动推送/回滚，批量多环境一键发布
3. 建议定期自动回归test/目录的所有自动化测试

---

## FAQ

- 数据库初始化问题：确保MySQL 8+权限，执行 deploy/init-db.sql
- 端口冲突：调整compose或yaml端口映射
- 镜像构建异常：请确认本地或CI环境已安装Docker与Maven

## 联系与定制服务

如有特殊生产环境、企业定制需求、二次开发、私有化/大屏/报表/授权等，
可随时邮件 support@flinksight.com 获得快速响应。