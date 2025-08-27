package com.flinksight.common.service;

import com.flinksight.common.dto.*;
import com.flinksight.common.model.PageResult;

import java.io.OutputStream;
import java.util.List;

/**
 * 报警事件业务接口
 * Alert Service
 */
public interface AlertService  extends SoftDeleteService<AlertDTO, Long> {
    /**
     * 统一分页查询：
     * - 仅 status（= -1 或 null 表示不过滤）
     * - 仅 jobId + status
     * - 仅 level + status
     * - jobId + level + status 交集
     */
    PageResult<AlertDTO> list(Long jobId, String level, Integer status, int page, int size);

    AlertDTO getAlert(Long id);

    List<AlertRuleDTO> getAlertRules(Long alertId);        // 与该告警匹配或已绑定的规则集合

    List<AlertHistoryDTO> getAlertHistory(Long alertId);

    AlertOpsDTO getAlertOps(Long alertId);

    void deleteAlerts(List<Long> ids);

    AlertDTO updateAlert(Long id, AlertDTO patch);         // 部分字段更新

    void exportAlerts(String level, Integer status, OutputStream out); // CSV


}
