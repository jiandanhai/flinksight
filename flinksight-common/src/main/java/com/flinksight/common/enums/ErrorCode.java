package com.flinksight.common.enums;

/**
 * 平台标准错误码定义（SaaS/多租户/工单/监控/权限/大数据/第三方）
 * 每个错误码全局唯一，不可复用，建议同步维护文档和多语言
 */
public class ErrorCode {
    // ========== 基础 ==========
    public static final int SUCCESS                 = 0;    // 成功
    public static final int FAIL                    = 1;    // 通用失败（不建议用，明确用其它code）

    // ========== 1xxx：通用参数/格式/校验 ==========
    public static final int PARAM_INVALID           = 1001; // 参数无效/缺失
    public static final int PARAM_FORMAT_ERROR      = 1002; // 参数格式非法
    public static final int VALIDATION_FAILED       = 1003; // 参数校验失败
    public static final int REQUEST_ILLEGAL         = 1004; // 非法请求（如跨站、伪造等）

    // ========== 2xxx：认证/授权/安全 ==========
    public static final int UNAUTHORIZED            = 2001; // 未认证（未登录/Token缺失）
    public static final int FORBIDDEN               = 2003; // 无权限（权限不足）
    public static final int TOKEN_EXPIRED           = 2004; // Token过期
    public static final int TOKEN_INVALID           = 2005; // Token非法
    public static final int ACCOUNT_DISABLED        = 2010; // 账号被禁用
    public static final int ACCOUNT_LOCKED          = 2011; // 账号被锁定
    public static final int ACCOUNT_NOT_FOUND       = 2012; // 账号不存在
    public static final int PASSWORD_INCORRECT      = 2013; // 密码错误

    // ========== 3xxx：资源不存在/状态非法 ==========
    public static final int NOT_FOUND               = 3001; // 资源不存在
    public static final int ALREADY_EXISTS          = 3002; // 已存在/冲突
    public static final int STATE_ILLEGAL           = 3003; // 状态非法/不可操作
    public static final int DELETED                 = 3004; // 已被删除

    // ========== 4xxx：业务/多租户/权限/大数据/工单 ==========
    public static final int TENANT_ISOLATION        = 4001; // 租户串租/隔离失败
    public static final int TENANT_NOT_FOUND        = 4002; // 租户不存在
    public static final int BUSINESS_ERROR          = 4003; // 业务通用异常
    public static final int DATA_CONFLICT           = 4004; // 数据冲突
    public static final int PERMISSION_DENIED       = 4005; // 权限拒绝
    public static final int WORKFLOW_ILLEGAL        = 4010; // 工单流转非法
    public static final int ALERT_TRIGGERED         = 4020; // 报警已触发
    public static final int METRIC_OUT_OF_RANGE     = 4021; // 指标异常

    // ========== 5xxx：第三方/外部依赖 ==========
    public static final int THIRD_PARTY_ERROR       = 5001; // 第三方服务异常
    public static final int KAFKA_ERROR             = 5010; // Kafka相关异常
    public static final int FLINK_ERROR             = 5011; // Flink相关异常
    public static final int SPARK_ERROR             = 5012; // Spark相关异常
    public static final int STORAGE_ERROR           = 5020; // 存储相关异常（如ES、CK等）

    // ========== 9xxx：系统/未知 ==========
    public static final int SYSTEM_ERROR            = 9001; // 系统内部异常
    public static final int DB_ERROR                = 9002; // 数据库异常
    public static final int NETWORK_ERROR           = 9003; // 网络异常
    public static final int UNKNOWN_ERROR           = 9999; // 未知异常（catch all）
}