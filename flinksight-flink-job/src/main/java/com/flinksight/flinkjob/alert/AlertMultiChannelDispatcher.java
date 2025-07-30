package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import java.util.List;

/**
 * 统一报警通道和多级通知策略（平台自动升级/降级）
 * 报警多通道调度分发器
 * 支持通知升级/降级、多通道兜底、平台自动切换
 */
public class AlertMultiChannelDispatcher extends ProcessFunction<JobMetricsEventDTO, JobMetricsEventDTO> {
    private final List<String> channels;

    /**
     * 构造方法，指定多通道
     * @param channels 通道列表如["wechat", "dingding", "sms", "email"]
     */
    public AlertMultiChannelDispatcher(List<String> channels) {
        this.channels = channels;
    }

    @Override
    public void processElement(JobMetricsEventDTO value, Context ctx, Collector<JobMetricsEventDTO> out) {
        for (String channel : channels) {
            try {
                switch (channel) {
                    // 钉钉报警（可扩展实现）
                    // case "dingding":
                    //     DingDingAlertSender.send(value);
                    //     break;

                    // 企业微信报警
                    case "wechat":
                        // 推荐：可自定义消息模板/用户，支持平台配置
                        WeChatAlertSender.send(
                                getWeChatToUser(value),
                                buildAlertMsg(value),
                                true // 是否markdown格式
                        );
                        break;

                    // 短信报警（可扩展实现）
                    // case "sms":
                    //     SmsAlertSender.send(value);
                    //     break;

                    // 邮件报警（可扩展实现）
                    // case "email":
                    //     EmailAlertSender.send(value);
                    //     break;

                    default:
                        // 忽略未知通道
                        break;
                }
            } catch (Exception ex) {
                // 兜底日志，不影响主流程
                System.err.println("Alert channel " + channel + " send failed: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        // 继续下游
        out.collect(value);
    }

    /**
     * 告警内容组装，可自定义支持 markdown 或文本
     */
    private String buildAlertMsg(JobMetricsEventDTO event) {
        // 可根据事件类型/等级/租户自定义丰富内容
        return String.format(
                "**作业报警**\n" +
                        "- 作业名: %s\n" +
                        "- 租户: %s\n" +
                        "- 报警类型: %s\n" +
                        "- 等级: %s\n" +
                        "- 内容: %s\n" +
                        "- 时间: %s",
                event.getJobName(),
                event.getTenantId(),
                event.getAlertType(),
                event.getAlertLevel(),
                event.getAlertMsg(),
                event.getTimestamp() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(event.getTimestamp()) : ""
        );
    }

    /**
     * 选择推送目标用户（可自定义从event获取负责人或用配置中心）
     */
    private String getWeChatToUser(JobMetricsEventDTO event) {
        // 生产建议根据租户/负责人动态分配，或配置中心管理
        // 这里只做演示，实际项目建议用 event.getOwnerUserId() 或配置中心
        return "zhangsan|lisi";
    }
}
