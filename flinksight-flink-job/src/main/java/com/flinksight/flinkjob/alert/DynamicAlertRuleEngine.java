package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 动态报警规则引擎 动态业务规则引擎集成（可对接平台API动态拉取报警/恢复策略）
 */
public class DynamicAlertRuleEngine {

    /**
     * 动态业务规则判定（从event.extraJson获取）
     */
    public static boolean match(JobMetricsEventDTO event) {
        if (event == null || event.getExtraJson() == null) return false;
        try {
            JsonNode rule = new ObjectMapper().readTree(event.getExtraJson());
            // 可支持表达式引擎，如 delay > 30000 && status=="RUNNING"
            if (rule.has("lagThreshold")) {
                double lagThreshold = rule.get("lagThreshold").asDouble();
                return event.getLag() != null && event.getLag() > lagThreshold;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
