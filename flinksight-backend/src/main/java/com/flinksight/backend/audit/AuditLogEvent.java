package com.flinksight.backend.audit;
import com.flinksight.common.dto.AuditLogDTO;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuditLogEvent {
    // 你原有的字段...
    private final Long tenantId;
    private final Long userId;
    private final String operatorId;
    private final String operator;     // 操作人名称
    private final String action;
    private final String targetType;
    private final String targetId;
    private final String content;
    private final String source;
    private final String traceId;
    private final String result;
    private final String failReason;
    private final String ip;

    /** ← 保持你原来的构造器访问级别(可能是包可见/私有)，不要改动也没关系 */
    AuditLogEvent(Long tenantId, Long userId, String operatorId, String operator,
                  String action, String targetType, String targetId,
                  String content, String source, String traceId,
                  String result, String failReason, String ip) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.operatorId = operatorId;
        this.operator = operator;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.content = content;
        this.source = source;
        this.traceId = traceId;
        this.result = result;
        this.failReason = failReason;
        this.ip = ip;
    }

    /** ✅ 新增：对外公开的工厂方法，切面调用它即可 */
    public static AuditLogEvent from(AuditLogDTO dto) {
        return new AuditLogEvent(
                dto.getTenantId(),
                dto.getUserId(),
                dto.getOperatorId(),
                dto.getOperatorName(),
                dto.getAction(),
                dto.getTargetType(),
                dto.getTargetId(),
                dto.getContent(),
                dto.getSource(),
                dto.getTraceId(),
                dto.getResult(),
                dto.getFailReason(),
                dto.getIp()
        );
    }
}
