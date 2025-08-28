package com.flinksight.common.service;

import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.dto.RuleMatchResultDTO;
import com.flinksight.common.dto.RuleTestRequestDTO;
import com.flinksight.common.model.PageResult;

import java.util.List;

/**
 * 报警规则业务接口
 * AlertRule Service
 */
public interface AlertRuleService  extends SoftDeleteService<AlertRuleDTO, Long>{
    AlertRuleDTO createAlertRule(AlertRuleDTO rule);

    AlertRuleDTO getAlertRule(Long id);

    /**
     * 按租户与集群查询
     */
    PageResult<AlertRuleDTO> list(Long clusterId, Integer enable,String keyword,int page, int size);


    AlertRuleDTO updateAlertRule(Long id, AlertRuleDTO patch);

    void deleteAlertRules(List<Long> ids);

    /** 单条启停 */
    AlertRuleDTO setEnable(Long id, boolean enable);

    /** 批量启停 */
    void setEnableBatch(List<Long> ids, boolean enable);

    /** 规则测试匹配：按样例事件返回排序后的候选规则（带评分与解释） */
    List<RuleMatchResultDTO> testMatch(RuleTestRequestDTO req);
}
