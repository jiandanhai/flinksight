package com.flinksight.sparkjob.runner;

import com.flinksight.common.dto.AlarmRuleConfig;
import com.flinksight.common.dto.JobRegisterRequestDTO;
import com.flinksight.sparkjob.audit.RestAuditLogServiceImpl;
import com.flinksight.sparkjob.config.DynamicConfigService;
import com.flinksight.sparkjob.register.JobAutoRegisterService;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

/**
 * 主入口Runner
 * Spark运维/监控/指标采集主入口，部署于每个被纳管Spark作业
 * 所有主流程（指标、报警、审计、注册）推送主通道时自动生成全局traceId。
 * 主通道推送失败统一自动兜底DLQ。
 * 主平台Kafka或DB做traceId幂等去重（唯一键），无重复消费、无丢失。
 * 作业启动即自动注册到主平台API，纳管无遗漏。
 * 动态配置支持平台下发参数热加载，运维平台可一键推送报警阈值、策略等变更，无需重启。
 * 多租户、链路追踪、审计全部由flinksight-common托管，保证所有SaaS合规和企业级上线需求。
 */
public class SparkOpsJob {
    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("FlinksightSparkOpsJob");
        SparkContext sc = new SparkContext(conf);

        String jobName = conf.get("spark.app.name");
        Long tenantId = Long.valueOf(conf.get("spark.flinksight.tenant.id", "1"));
        String operator = conf.get("spark.flinksight.operator", "system");
        String backendRegisterUrl = conf.get("spark.flinksight.backend.url", "http://backend:8080/api/job/register");
        String nacosServerAddr = conf.get("spark.flinksight.nacos.url", "nacos:8848");

        // 1. 自动注册
        JobRegisterRequestDTO regReq = JobRegisterRequestDTO.builder()
                .jobName(jobName)
                .tenantId(tenantId)
                .operator(operator)
                .build();
        try {
            JobAutoRegisterService.registerJob(backendRegisterUrl, regReq);
        } catch (Exception e) {
            System.err.println("作业自动注册失败: " + e.getMessage());
            // 是否直接退出：return;
        }

        // 2. 动态配置热加载（业务日志可通过 HTTP/REST 方式打回后台）
        DynamicConfigService configService = new DynamicConfigService(nacosServerAddr, new RestAuditLogServiceImpl(backendRegisterUrl));
        configService.addChangeListener(
                "some_data_id",
                "DEFAULT_GROUP",
                tenantId,
                operator,
                "alarm_rule", // configType
                AlarmRuleConfig.class,
                configObj -> {
                    // 你的配置回调业务处理
                    System.out.println("收到新配置：" + configObj);
                }
        );
        // ...后续业务处理
    }
}
