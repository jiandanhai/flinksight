/* eslint-disable */
/* tslint:disable */
// @ts-nocheck
/*
 * ---------------------------------------------------------------
 * ## THIS FILE WAS GENERATED VIA SWAGGER-TYPESCRIPT-API        ##
 * ##                                                           ##
 * ## AUTHOR: acacode                                           ##
 * ## SOURCE: https://github.com/acacode/swagger-typescript-api ##
 * ---------------------------------------------------------------
 */

/** 第三方OAuth绑定账号DTO */
export interface OauthAccountDTO {
  /**
   * 主键ID
   * @format int64
   */
  id?: number;
  /**
   * 用户ID
   * @format int64
   */
  userId?: number;
  /** 平台类型(如 wechat、github、google) */
  provider?: string;
  /** 平台openid */
  openid?: string;
  /** 平台unionid */
  unionid?: string;
  /** access token */
  accessToken?: string;
  /**
   * 过期时间
   * @format date-time
   */
  expireTime?: string;
  /**
   * 绑定时间
   * @format date-time
   */
  createTime?: string;
}

/** 角色DTO */
export interface RoleDTO {
  /**
   * 角色ID
   * @format int64
   */
  id?: number;
  /** 角色编码 */
  code?: string;
  /** 角色名称 */
  name?: string;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /** 角色备注 */
  remark?: string;
  /**
   * 是否删除 0=正常 1=删除
   * @format int32
   */
  isDeleted?: number;
}

/** 租户DTO */
export interface TenantDTO {
  /**
   * 租户ID
   * @format int64
   */
  id?: number;
  /** 租户唯一编码 */
  code?: string;
  /** 租户名称 */
  name?: string;
  /** 联系人 */
  contact?: string;
  /** 联系方式 */
  contactInfo?: string;
  /**
   * 状态 1启用 0禁用
   * @format int32
   */
  status?: number;
  /** 备注 */
  remark?: string;
  /**
   * 创建时间
   * @format date-time
   */
  createTime?: string;
  users?: UserDTO[];
  /**
   * 是否删除 0正常 1删除
   * @format int32
   */
  isDeleted?: number;
}

/** 用户DTO */
export interface UserDTO {
  /**
   * 用户ID
   * @format int64
   */
  id?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /** 用户名/账号 */
  username?: string;
  /** 昵称 */
  nickname?: string;
  /** 邮箱 */
  email?: string;
  /** 手机号 */
  phone?: string;
  /** 头像 */
  avatar?: string;
  /** SSO第三方唯一ID */
  ssoId?: string;
  /**
   * 状态 1启用 0禁用
   * @format int32
   */
  status?: number;
  /**
   * 软删除 0正常 1删除
   * @format int32
   */
  isDeleted?: number;
  /**
   * 注册时间
   * @format date-time
   */
  createdAt?: string;
  /**
   * 更新时间
   * @format date-time
   */
  updatedAt?: string;
  /** 角色列表 */
  roles?: RoleDTO[];
  /** 租户DTO */
  tenant?: TenantDTO;
  oauthAccounts?: OauthAccountDTO[];
}

/** 统一返回对象 */
export interface ApiResponseUserDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户DTO */
  data?: UserDTO;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponseTenantDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 租户DTO */
  data?: TenantDTO;
  /** 租户ID */
  traceId?: string;
}

/** 租户配置DTO */
export interface TenantConfigDTO {
  /**
   * 主键
   * @format int64
   */
  id?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /** 配置项Key */
  configKey?: string;
  /** 配置值 */
  configValue?: string;
  /** 说明 */
  description?: string;
  /**
   * 是否删除 0正常 1删除
   * @format int32
   */
  isDeleted?: number;
  /**
   * 创建时间
   * @format date-time
   */
  createTime?: string;
  /**
   * 更新时间
   * @format date-time
   */
  updateTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseTenantConfigDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 租户配置DTO */
  data?: TenantConfigDTO;
  /** 租户ID */
  traceId?: string;
}

/** 标签DTO */
export interface TagDTO {
  /**
   * 主键
   * @format int64
   */
  id?: number;
  /** 标签名 */
  name?: string;
  /** 标签类型 */
  type?: string;
  /** 标签颜色 */
  color?: string;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /**
   * 是否删除 0正常 1删除
   * @format int32
   */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseTagDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 标签DTO */
  data?: TagDTO;
  /** 租户ID */
  traceId?: string;
}

/** 系统参数DTO */
export interface SysParamDTO {
  /** @format int64 */
  id?: number;
  paramKey?: string;
  paramValue?: string;
  description?: string;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  updateTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseSysParamDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 系统参数DTO */
  data?: SysParamDTO;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponseRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 角色DTO */
  data?: RoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 资源DTO */
export interface ResourceDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  type?: string;
  uri?: string;
  /** @format int64 */
  size?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  ownerId?: number;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseResourceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 资源DTO */
  data?: ResourceDTO;
  /** 租户ID */
  traceId?: string;
}

/** 资源分组DTO */
export interface ResourceGroupDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  type?: string;
  /** @format int64 */
  parentId?: number;
  /** @format int64 */
  tenantId?: number;
  description?: string;
  /** @format date-time */
  createTime?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseResourceGroupDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 资源分组DTO */
  data?: ResourceGroupDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户扩展档案DTO */
export interface ProfileDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  userId?: number;
  avatar?: string;
  bio?: string;
  extraJson?: string;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  updateTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseProfileDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户扩展档案DTO */
  data?: ProfileDTO;
  /** 租户ID */
  traceId?: string;
}

/** 操作模板DTO */
export interface OperationTemplateDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  type?: string;
  content?: string;
  /** @format int64 */
  tenantId?: number;
  /** @format date-time */
  createTime?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseOperationTemplateDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 操作模板DTO */
  data?: OperationTemplateDTO;
  /** 租户ID */
  traceId?: string;
}

/** 消息通知DTO */
export interface NotificationDTO {
  /** @format int64 */
  id?: number;
  title?: string;
  content?: string;
  /** @format int32 */
  type?: number;
  /** @format int64 */
  userId?: number;
  /** @format int32 */
  status?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format date-time */
  sendTime?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseNotificationDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 消息通知DTO */
  data?: NotificationDTO;
  /** 租户ID */
  traceId?: string;
}

/** 节点DTO */
export interface NodeDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  type?: string;
  ip?: string;
  /** @format int64 */
  clusterId?: number;
  /** @format int32 */
  status?: number;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseNodeDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 节点DTO */
  data?: NodeDTO;
  /** 租户ID */
  traceId?: string;
}

/** 指标看板DTO */
export interface MetricDashboardDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  configJson?: string;
  /** @format int64 */
  tenantId?: number;
  /** @format int32 */
  status?: number;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseMetricDashboardDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 指标看板DTO */
  data?: MetricDashboardDTO;
  /** 租户ID */
  traceId?: string;
}

/** 标签DTO */
export interface LabelDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  color?: string;
  type?: string;
  /** @format int64 */
  tenantId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseLabelDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 标签DTO */
  data?: LabelDTO;
  /** 租户ID */
  traceId?: string;
}

export interface JobDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  clusterId?: number;
  name?: string;
  type?: string;
  /** @format int32 */
  status?: number;
  /** @format int64 */
  ownerId?: number;
  /** @format date-time */
  startTime?: string;
  /** @format date-time */
  endTime?: string;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
  /** @format date-time */
  updateTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseJobDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  data?: JobDTO;
  /** 租户ID */
  traceId?: string;
}

/** 第三方集成配置DTO */
export interface IntegrationConfigDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  type?: string;
  configJson?: string;
  /** @format int64 */
  tenantId?: number;
  /** @format int32 */
  status?: number;
  /** @format date-time */
  createTime?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseIntegrationConfigDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 第三方集成配置DTO */
  data?: IntegrationConfigDTO;
  /** 租户ID */
  traceId?: string;
}

/** 文件DTO */
export interface FileDTO {
  /** @format int64 */
  id?: number;
  fileName?: string;
  fileType?: string;
  fileUrl?: string;
  /** @format int64 */
  fileSize?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  ownerId?: number;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  uploadTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseFileDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 文件DTO */
  data?: FileDTO;
  /** 租户ID */
  traceId?: string;
}

/** 数据字典DTO */
export interface DictDTO {
  /** @format int64 */
  id?: number;
  dictType?: string;
  dictKey?: string;
  dictValue?: string;
  /** @format int32 */
  sort?: number;
  description?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseDictDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 数据字典DTO */
  data?: DictDTO;
  /** 租户ID */
  traceId?: string;
}

/** 数据源DTO */
export interface DataSourceDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  type?: string;
  connectInfo?: string;
  /** @format int64 */
  tenantId?: number;
  description?: string;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseDataSourceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 数据源DTO */
  data?: DataSourceDTO;
  /** 租户ID */
  traceId?: string;
}

export interface ClusterDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  tenantId?: number;
  name?: string;
  type?: string;
  endpoint?: string;
  version?: string;
  tags?: string;
  apiEndpoint?: string;
  /** @format int32 */
  status?: number;
  remark?: string;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
  healthStatus?: string;
  /** @format int32 */
  activeNodeCount?: number;
  /** @format int32 */
  jobCount?: number;
}

/** 统一返回对象 */
export interface ApiResponseClusterDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  data?: ClusterDTO;
  /** 租户ID */
  traceId?: string;
}

/** 操作审计日志DTO */
export interface AuditLogDTO {
  /**
   * 日志主键ID
   * @format int64
   */
  id?: number;
  /**
   * 用户ID
   * @format int64
   */
  userId?: number;
  /**
   * 操作时间
   * @format date-time
   */
  operateTime?: string;
  /** 操作人ID */
  operatorId?: string;
  /** 操作人名称 */
  operatorName?: string;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /** 操作类型（如CREATE/DELETE/EXPORT/LOGIN等） */
  action?: string;
  /** 目标类型（如User、Job、Cluster等） */
  targetType?: string;
  /** 目标对象ID */
  targetId?: string;
  /** 操作内容/详情（如变更前后、请求参数等） */
  content?: string;
  /** 前端来源页面、接口 */
  source?: string;
  /** traceId，全链路追踪标识 */
  traceId?: string;
  /** 操作结果（SUCCESS/FAIL等） */
  result?: string;
  /** 失败原因（如有） */
  failReason?: string;
  /** ip地址 */
  ip?: string;
  /**
   * 注册时间
   * @format date-time
   */
  createTime?: string;
  /**
   * 是否删除 0正常 1删除
   * @format int32
   */
  isDeleted?: number;
  tenantName?: string;
  userName?: string;
}

/** 统一返回对象 */
export interface ApiResponseAuditLogDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 操作审计日志DTO */
  data?: AuditLogDTO;
  /** 租户ID */
  traceId?: string;
}

/** 接口白名单DTO */
export interface ApiWhitelistDTO {
  /** @format int64 */
  id?: number;
  path?: string;
  method?: string;
  description?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseApiWhitelistDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 接口白名单DTO */
  data?: ApiWhitelistDTO;
  /** 租户ID */
  traceId?: string;
}

/** API密钥DTO */
export interface ApiKeyDTO {
  /** @format int64 */
  id?: number;
  name?: string;
  apiKey?: string;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  userId?: number;
  /** @format int32 */
  status?: number;
  /** @format date-time */
  expireTime?: string;
  /** @format date-time */
  createTime?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseApiKeyDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** API密钥DTO */
  data?: ApiKeyDTO;
  /** 租户ID */
  traceId?: string;
}

/** API访问日志DTO */
export interface ApiAccessLogDTO {
  /** @format int64 */
  id?: number;
  url?: string;
  httpMethod?: string;
  params?: string;
  /** @format int32 */
  status?: number;
  /** @format int64 */
  userId?: number;
  /** @format int64 */
  tenantId?: number;
  ip?: string;
  /** @format date-time */
  accessTime?: string;
  /** @format int64 */
  duration?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseApiAccessLogDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** API访问日志DTO */
  data?: ApiAccessLogDTO;
  /** 租户ID */
  traceId?: string;
}

export interface AlertDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  jobId?: number;
  level?: string;
  type?: string;
  message?: string;
  /** @format int32 */
  status?: number;
  /** @format int64 */
  handlerId?: number;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
  /** @format date-time */
  updateTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseAlertDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  data?: AlertDTO;
  /** 租户ID */
  traceId?: string;
}

/** 报警历史DTO */
export interface AlertHistoryDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  alertId?: number;
  /** @format int64 */
  ruleId?: number;
  content?: string;
  /** @format int32 */
  level?: number;
  /** @format int32 */
  status?: number;
  /** @format int64 */
  operatorId?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format date-time */
  operateTime?: string;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseAlertHistoryDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 报警历史DTO */
  data?: AlertHistoryDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户注册请求DTO */
export interface SsoAuthRegisterRequestDTO {
  account?: string;
  password?: string;
  nickname?: string;
  email?: string;
}

/** 统一返回对象 */
export interface ApiResponseSsoAuthResponseDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 单点登录相应DTO */
  data?: SsoAuthResponseDTO;
  /** 租户ID */
  traceId?: string;
}

/** 单点登录相应DTO */
export interface SsoAuthResponseDTO {
  /** JWT认证成功后返回的JWT令牌 */
  token?: string;
  /** 用户DTO */
  user?: UserDTO;
  redirectUrl?: string;
}

/** 统一返回对象 */
export interface ApiResponseVoid {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: object;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponseOauthAccountDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 第三方OAuth绑定账号DTO */
  data?: OauthAccountDTO;
  /** 租户ID */
  traceId?: string;
}

/** 单点登录请求DTO */
export interface SsoAuthLoginRequestDTO {
  /** 登录账号 */
  account: string;
  /** 登录密码（明文传输，传输层加密） */
  password: string;
}

/** 统一返回对象 */
export interface ApiResponseBoolean {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: boolean;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponseUserTenantDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-租户关联DTO */
  data?: UserTenantDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-租户关联DTO */
export interface UserTenantDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  userId?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseUserRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-角色关联DTO */
  data?: UserRoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-角色关联DTO */
export interface UserRoleDTO {
  /**
   * 主键ID
   * @format int64
   */
  id?: number;
  /**
   * 用户ID
   * @format int64
   */
  userId?: number;
  /**
   * 角色ID
   * @format int64
   */
  roleId?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /**
   * 分配时间
   * @format date-time
   */
  assignTime?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseUserPostDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-岗位关联DTO */
  data?: UserPostDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-岗位关联DTO */
export interface UserPostDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  userId?: number;
  /** @format int64 */
  postId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseUserPermissionDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-权限关联DTO */
  data?: UserPermissionDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-权限关联DTO */
export interface UserPermissionDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  userId?: number;
  /** @format int64 */
  permissionId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseUserGroupDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-分组关联DTO */
  data?: UserGroupDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-分组关联DTO */
export interface UserGroupDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  userId?: number;
  /** @format int64 */
  groupId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseUserDepartmentDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-部门关联DTO */
  data?: UserDepartmentDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-部门关联DTO */
export interface UserDepartmentDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  userId?: number;
  /** @format int64 */
  departmentId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseUserApiDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-API权限关联DTO */
  data?: UserApiDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-API权限关联DTO */
export interface UserApiDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  userId?: number;
  /** @format int64 */
  apiId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseTenantResourceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 租户-资源关联DTO */
  data?: TenantResourceDTO;
  /** 租户ID */
  traceId?: string;
}

/** 租户-资源关联DTO */
export interface TenantResourceDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  resourceId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseRolePermissionDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 角色-权限关联DTO */
  data?: RolePermissionDTO;
  /** 租户ID */
  traceId?: string;
}

/** 角色-权限关联DTO */
export interface RolePermissionDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  roleId?: number;
  /** @format int64 */
  permissionId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseRoleMenuDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 角色-菜单关联DTO */
  data?: RoleMenuDTO;
  /** 租户ID */
  traceId?: string;
}

/** 角色-菜单关联DTO */
export interface RoleMenuDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  roleId?: number;
  /** @format int64 */
  menuId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseRoleDataScopeDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 角色-数据权限范围关联DTO */
  data?: RoleDataScopeDTO;
  /** 租户ID */
  traceId?: string;
}

/** 角色-数据权限范围关联DTO */
export interface RoleDataScopeDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  roleId?: number;
  /** @format int64 */
  dataScopeId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseResourceLabelDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 资源-标签关联DTO */
  data?: ResourceLabelDTO;
  /** 租户ID */
  traceId?: string;
}

/** 资源-标签关联DTO */
export interface ResourceLabelDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  resourceId?: number;
  /** @format int64 */
  labelId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 组织架构树节点DTO */
export interface OrgNodeDTO {
  /**
   * 节点ID
   * @format int64
   */
  id?: number;
  /**
   * 父节点ID
   * @format int64
   */
  parentId?: number;
  /** 节点名称 */
  name?: string;
  /** 节点类型（公司/部门/组等） */
  type?: string;
  /**
   * 排序序号
   * @format int32
   */
  sortOrder?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /**
   * 创建时间
   * @format date-time
   */
  createdAt?: string;
  /**
   * 是否删除 0=正常 1=删除
   * @format int32
   */
  isDeleted?: number;
  /** 子节点列表（树结构） */
  children?: OrgNodeDTO[];
}

/** 统一返回对象 */
export interface ApiResponseOrgNodeDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 组织架构树节点DTO */
  data?: OrgNodeDTO;
  /** 租户ID */
  traceId?: string;
}

/** 运维自动化任务DTO */
export interface OpsTaskDTO {
  /**
   * 任务ID
   * @format int64
   */
  id?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /** 任务名称 */
  name?: string;
  /** 任务类型（如备份、扩容、升级等） */
  type?: string;
  /** 状态（PENDING、RUNNING、SUCCESS、FAILED等） */
  status?: string;
  /** 任务描述 */
  description?: string;
  /**
   * 创建时间
   * @format date-time
   */
  createdAt?: string;
  /**
   * 执行时间
   * @format date-time
   */
  executedAt?: string;
  /**
   * 是否删除 0=正常 1=删除
   * @format int32
   */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseOpsTaskDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 运维自动化任务DTO */
  data?: OpsTaskDTO;
  /** 租户ID */
  traceId?: string;
}

/** 通知渠道DTO */
export interface NotifyChannelDTO {
  /**
   * 主键ID
   * @format int64
   */
  id?: number;
  /** 渠道类型（email/dingding/wechat/sms等） */
  type?: string;
  /** 配置内容(JSON字符串) */
  config?: string;
  /** 渠道名称 */
  name?: string;
  /**
   * 是否启用 0=禁用 1=启用
   * @format int32
   */
  enabled?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /**
   * 创建时间
   * @format date-time
   */
  createdAt?: string;
  /**
   * 是否删除 0=正常 1=删除
   * @format int32
   */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseNotifyChannelDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 通知渠道DTO */
  data?: NotifyChannelDTO;
  /** 租户ID */
  traceId?: string;
}

export interface JobMetricDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  jobId?: number;
  metricKey?: string;
  /** @format double */
  value?: number;
  /** @format date-time */
  ts?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseJobMetricDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  data?: JobMetricDTO;
  /** 租户ID */
  traceId?: string;
}

/** 登录历史DTO */
export interface LoginHistoryDTO {
  /**
   * 日志ID
   * @format int64
   */
  id?: number;
  /**
   * 用户ID
   * @format int64
   */
  userId?: number;
  /** IP */
  ipAddress?: string;
  /** 登录类型 */
  loginType?: string;
  /** 设备信息 */
  deviceInfo?: string;
  /**
   * 登录时间
   * @format date-time
   */
  loginTime?: string;
  /**
   * 是否成功
   * @format int32
   */
  successFlag?: number;
  /** 失败原因 */
  failReason?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseLoginHistoryDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 登录历史DTO */
  data?: LoginHistoryDTO;
  /** 租户ID */
  traceId?: string;
}

export interface JobLogDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  jobId?: number;
  level?: string;
  content?: string;
  /** @format date-time */
  logTime?: string;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseJobLogDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  data?: JobLogDTO;
  /** 租户ID */
  traceId?: string;
}

export interface JobRegisterRequestDTO {
  jobName?: string;
  /** @format int64 */
  jobId?: number;
  /** @format int64 */
  tenantId?: number;
  jobType?: string;
  projectCode?: string;
  operator?: string;
  source?: string;
  traceId?: string;
  remark?: string;
  /** @format int64 */
  registerAt?: number;
}

/** 统一返回对象 */
export interface ApiResponseJobInfoDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: JobInfoDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface JobInfoDTO {
  /** @format int64 */
  id?: number;
  jobName?: string;
  /** @format int64 */
  tenantId?: number;
  jobType?: string;
  projectCode?: string;
  operator?: string;
  source?: string;
  traceId?: string;
  remark?: string;
  /** @format int64 */
  registerAt?: number;
  /** @format int32 */
  isDeleted?: number;
  createdAt?: string;
  updatedAt?: string;
}

/** 作业运行实例DTO */
export interface JobInstanceDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  jobId?: number;
  batchNo?: string;
  /** @format date-time */
  startTime?: string;
  /** @format date-time */
  endTime?: string;
  /** @format int32 */
  status?: number;
  logContent?: string;
  /** @format int64 */
  tenantId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseJobInstanceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 作业运行实例DTO */
  data?: JobInstanceDTO;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponseGroupRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 组织-角色关联DTO */
  data?: GroupRoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 组织-角色关联DTO */
export interface GroupRoleDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  groupId?: number;
  /** @format int64 */
  roleId?: number;
  /** @format int32 */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponseDeptRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 部门-角色关联DTO */
  data?: DeptRoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 部门-角色关联DTO */
export interface DeptRoleDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  deptId?: number;
  /** @format int64 */
  roleId?: number;
  /** @format int32 */
  isDeleted?: number;
}

export interface AlertRuleDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  clusterId?: number;
  metricKey?: string;
  /** @format double */
  threshold?: number;
  compareOp?: string;
  channel?: string;
  /** @format int32 */
  enable?: number;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseAlertRuleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  data?: AlertRuleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponsePageResultOauthAccountDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultOauthAccountDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultOauthAccountDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: OauthAccountDTO[];
}

/** 统一返回对象 */
export interface ApiResponseUserTokenStateDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户令牌版本状态 */
  data?: UserTokenStateDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户令牌版本状态 */
export interface UserTokenStateDTO {
  /**
   * 用户id
   * @format int64
   */
  userId?: number;
  /** 访问令牌 (JWT Access Token) */
  accessToken?: string;
  /**
   * 访问令牌有效期（秒）
   * @format int64
   */
  expiresIn?: number;
  /** 刷新令牌 (可选) */
  refreshToken?: string;
  /**
   * 刷新令牌有效期（秒，可选）
   * @format int64
   */
  refreshExpiresIn?: number;
  /** 令牌类型，通常为 Bearer */
  tokenType?: string;
  /**
   * 当前 token 版本号（用于后端 tokenVersion 校验）
   * @format int32
   */
  tokenVersion?: number;
  /**
   * 最后更新时间
   * @format date-time
   */
  lastUpdated?: string;
}

/** 统一返回对象 */
export interface ApiResponseUserPermissionResDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 用户-权限前端 */
  data?: UserPermissionResDTO;
  /** 租户ID */
  traceId?: string;
}

/** 用户-权限前端 */
export interface UserPermissionResDTO {
  permissions?: string[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserTenantDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserTenantDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserTenantDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserTenantDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserRoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserRoleDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserRoleDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserPostDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserPostDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserPostDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserPostDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserPermissionDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserPermissionDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserPermissionDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserPermissionDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserGroupDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserGroupDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserGroupDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserGroupDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserDepartmentDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserDepartmentDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserDepartmentDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserDepartmentDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultUserApiDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultUserApiDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultUserApiDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: UserApiDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultTenantDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultTenantDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultTenantDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: TenantDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultTenantResourceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultTenantResourceDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultTenantResourceDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: TenantResourceDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultTenantConfigDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultTenantConfigDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultTenantConfigDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: TenantConfigDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultTagDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultTagDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultTagDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: TagDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultSysParamDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultSysParamDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultSysParamDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: SysParamDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultRoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultRoleDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: RoleDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultRolePermissionDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultRolePermissionDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultRolePermissionDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: RolePermissionDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultRoleMenuDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultRoleMenuDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultRoleMenuDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: RoleMenuDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultRoleDataScopeDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultRoleDataScopeDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultRoleDataScopeDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: RoleDataScopeDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultResourceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultResourceDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultResourceDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: ResourceDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultResourceLabelDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultResourceLabelDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultResourceLabelDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: ResourceLabelDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultResourceGroupDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultResourceGroupDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultResourceGroupDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: ResourceGroupDTO[];
}

/** 统一返回对象 */
export interface ApiResponseListOrgNodeDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: OrgNodeDTO[];
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponsePageResultOrgNodeDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultOrgNodeDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultOrgNodeDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: OrgNodeDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultOpsTaskDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultOpsTaskDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultOpsTaskDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: OpsTaskDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultOperationTemplateDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultOperationTemplateDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultOperationTemplateDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: OperationTemplateDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultNotifyChannelDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultNotifyChannelDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultNotifyChannelDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: NotifyChannelDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultNotificationDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultNotificationDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultNotificationDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: NotificationDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultNodeDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultNodeDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultNodeDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: NodeDTO[];
}

/** 统一返回对象 */
export interface ApiResponseNodeHealthDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 节点健康DTO */
  data?: NodeHealthDTO;
  /** 租户ID */
  traceId?: string;
}

/** 节点健康DTO */
export interface NodeHealthDTO {
  /**
   * 主键
   * @format int64
   */
  id?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /**
   * 节点ID
   * @format int64
   */
  nodeId?: number;
  /** 健康状态（如 HEALTHY/UNHEALTHY/WARNING） */
  healthStatus?: string;
  /**
   * 健康检测时间
   * @format date-time
   */
  checkTime?: string;
  /** 状态描述 */
  message?: string;
  /**
   * 是否删除 0正常 1删除
   * @format int32
   */
  isDeleted?: number;
}

/** 统一返回对象 */
export interface ApiResponsePageResultNodeHealthDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultNodeHealthDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultNodeHealthDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: NodeHealthDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultJobMetricDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultJobMetricDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultJobMetricDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: JobMetricDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultJobInstanceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultJobInstanceDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultJobInstanceDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: JobInstanceDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultMetricDashboardDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultMetricDashboardDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultMetricDashboardDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: MetricDashboardDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultLoginHistoryDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultLoginHistoryDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultLoginHistoryDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: LoginHistoryDTO[];
}

/** 统一返回对象 */
export interface ApiResponseLong {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /**
   * 返回数据
   * @format int64
   */
  data?: number;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponsePageResultLabelDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultLabelDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultLabelDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: LabelDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultJobLogDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultJobLogDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultJobLogDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: JobLogDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultJobDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultJobDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultJobDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: JobDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultJobFunnelDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultJobFunnelDTO;
  /** 租户ID */
  traceId?: string;
}

/** 业务转化漏斗统计DTO */
export interface JobFunnelDTO {
  /** 业务阶段，如NEW、RUNNING、COMPLETED、FAILED等 */
  stage?: string;
  /**
   * 当前阶段任务数量
   * @format int32
   */
  count?: number;
  /** 上一阶段转化率（百分比字符串，如87.2%） */
  conversionRate?: string;
  /** 阶段业务描述 */
  stageDesc?: string;
}

/** 返回数据 */
export interface PageResultJobFunnelDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: JobFunnelDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultJobPermissionDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultJobPermissionDTO;
  /** 租户ID */
  traceId?: string;
}

/** 作业权限分配DTO */
export interface JobPermissionDTO {
  /**
   * 主键ID
   * @format int64
   */
  id?: number;
  /**
   * 作业ID
   * @format int64
   */
  jobId?: number;
  /**
   * 租户ID
   * @format int64
   */
  tenantId?: number;
  /** 用户ID */
  userId?: string;
  /**
   * 权限ID
   * @format int64
   */
  permissionId?: number;
  /** 权限编码（可选） */
  permissionCode?: string;
  /** 权限名称（可选） */
  permissionName?: string;
  /** 作业名称（可选） */
  jobName?: string;
}

/** 返回数据 */
export interface PageResultJobPermissionDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: JobPermissionDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultJobDiagnosticLogDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultJobDiagnosticLogDTO;
  /** 租户ID */
  traceId?: string;
}

export interface JobDiagnosticLogDTO {
  /** @format int64 */
  id?: number;
  /** @format int64 */
  jobId?: number;
  jobName?: string;
  logLevel?: string;
  logContent?: string;
  suggestion?: string;
  /** @format date-time */
  logTime?: string;
  source?: string;
  nodeId?: string;
  /** @format int64 */
  tenantId?: number;
  /** @format int64 */
  operatorId?: number;
  auditSource?: string;
  traceId?: string;
  /** @format int32 */
  isDeleted?: number;
  /** @format date-time */
  createdAt?: string;
  /** @format date-time */
  updatedAt?: string;
}

/** 返回数据 */
export interface PageResultJobDiagnosticLogDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: JobDiagnosticLogDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultIntegrationConfigDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultIntegrationConfigDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultIntegrationConfigDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: IntegrationConfigDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultGroupRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultGroupRoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultGroupRoleDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: GroupRoleDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultFileDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultFileDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultFileDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: FileDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultDictDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultDictDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultDictDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: DictDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultDeptRoleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultDeptRoleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultDeptRoleDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: DeptRoleDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultDataSourceDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultDataSourceDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultDataSourceDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: DataSourceDTO[];
}

/** 统一返回对象 */
export interface ApiResponseKPIStatusSummaryDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 大盘核心指标统计DTO */
  data?: KPIStatusSummaryDTO;
  /** 租户ID */
  traceId?: string;
}

/** 大盘核心指标统计DTO */
export interface KPIStatusSummaryDTO {
  /**
   * 集群总数
   * @format int32
   */
  clusterCount?: number;
  /**
   * 任务总数
   * @format int32
   */
  jobCount?: number;
  /**
   * 报警事件数
   * @format int32
   */
  alertCount?: number;
  /**
   * 活跃任务数
   * @format int32
   */
  activeJobCount?: number;
  /**
   * 整体健康得分（0-100）
   * @format int32
   */
  healthScore?: number;
  /**
   * 失败作业数量
   * @format int64
   */
  failedJobs?: number;
  /**
   * 用户数量
   * @format int64
   */
  userCount?: number;
  /** 统计时间（yyyy-MM-dd HH:mm:ss） */
  statTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseMetricSeriesDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: MetricSeriesDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface MetricSeriesDTO {
  /** 时间序列 */
  times?: string[];
  /** 指标值 */
  values?: number[];
}

/** 统一返回对象 */
export interface ApiResponseHealthDistributionDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 集群健康分布统计DTO */
  data?: HealthDistributionDTO;
  /** 租户ID */
  traceId?: string;
}

/** 集群健康分布统计DTO */
export interface HealthDistributionDTO {
  /**
   * 健康集群数量
   * @format int32
   */
  healthyCount?: number;
  /**
   * 警告集群数量
   * @format int32
   */
  warningCount?: number;
  /**
   * 异常集群数量
   * @format int32
   */
  errorCount?: number;
  /** 统计时间（yyyy-MM-dd HH:mm:ss） */
  statTime?: string;
}

/** 统一返回对象 */
export interface ApiResponseClusterTrendDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 集群状态趋势 DTO */
  data?: ClusterTrendDTO;
  /** 租户ID */
  traceId?: string;
}

/** 集群状态趋势 DTO */
export interface ClusterTrendDTO {
  /** 时间序列 */
  times?: string[];
  /** 健康数量 */
  healthy?: number[];
  /** 预警数量 */
  warning?: number[];
  /** 异常数量 */
  critical?: number[];
}

/** 统一返回对象 */
export interface ApiResponseClusterHealthMetricsDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 集群健康指标统计 DTO */
  data?: ClusterHealthMetricsDTO;
  /** 租户ID */
  traceId?: string;
}

/** 集群健康指标统计 DTO */
export interface ClusterHealthMetricsDTO {
  /**
   * 总集群数量
   * @format int32
   */
  totalClusters?: number;
  /**
   * 活跃节点总数
   * @format int32
   */
  totalActiveNodes?: number;
  /**
   * 平均 CPU 使用率
   * @format double
   */
  avgCpuUsage?: number;
  /**
   * 平均内存使用率
   * @format double
   */
  avgMemoryUsage?: number;
  /** 统计时间 */
  statTime?: string;
}

/** 返回数据 */
export interface AlertTrendDTO {
  /** 时间列表 */
  times?: string[];
  /** 总告警数量 */
  total?: number[];
  /** 严重告警数量 */
  fatal?: number[];
  /** 预警告警数量 */
  warn?: number[];
}

/** 统一返回对象 */
export interface ApiResponseAlertTrendDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: AlertTrendDTO;
  /** 租户ID */
  traceId?: string;
}

/** 统一返回对象 */
export interface ApiResponsePageResultClusterDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultClusterDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultClusterDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: ClusterDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultAuditLogDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultAuditLogDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultAuditLogDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: AuditLogDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultApiKeyDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultApiKeyDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultApiKeyDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: ApiKeyDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultApiAccessLogDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultApiAccessLogDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultApiAccessLogDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: ApiAccessLogDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultAlertDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultAlertDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultAlertDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: AlertDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultAlertRuleDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultAlertRuleDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultAlertRuleDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: AlertRuleDTO[];
}

/** 统一返回对象 */
export interface ApiResponsePageResultAlertHistoryDTO {
  /** 是否成功 */
  success?: boolean;
  /** @format int32 */
  code?: number;
  /** 错误信息 */
  message?: string;
  /** 返回数据 */
  data?: PageResultAlertHistoryDTO;
  /** 租户ID */
  traceId?: string;
}

/** 返回数据 */
export interface PageResultAlertHistoryDTO {
  /** @format int64 */
  total?: number;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
  data?: AlertHistoryDTO[];
}
