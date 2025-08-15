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

import type {
  AlertDTO,
  AlertHistoryDTO,
  AlertRuleDTO,
  ApiAccessLogDTO,
  ApiKeyDTO,
  ApiResponseAlertDTO,
  ApiResponseAlertHistoryDTO,
  ApiResponseAlertRuleDTO,
  ApiResponseAlertTrendDTO,
  ApiResponseApiAccessLogDTO,
  ApiResponseApiKeyDTO,
  ApiResponseApiWhitelistDTO,
  ApiResponseAuditLogDTO,
  ApiResponseBoolean,
  ApiResponseClusterDTO,
  ApiResponseClusterHealthMetricsDTO,
  ApiResponseClusterTrendDTO,
  ApiResponseDataSourceDTO,
  ApiResponseDeptRoleDTO,
  ApiResponseDictDTO,
  ApiResponseFileDTO,
  ApiResponseGroupRoleDTO,
  ApiResponseHealthDistributionDTO,
  ApiResponseIntegrationConfigDTO,
  ApiResponseJobDTO,
  ApiResponseJobInfoDTO,
  ApiResponseJobInstanceDTO,
  ApiResponseJobLogDTO,
  ApiResponseJobMetricDTO,
  ApiResponseKPIStatusSummaryDTO,
  ApiResponseLabelDTO,
  ApiResponseListOrgNodeDTO,
  ApiResponseLoginHistoryDTO,
  ApiResponseLong,
  ApiResponseMetricDashboardDTO,
  ApiResponseMetricSeriesDTO,
  ApiResponseNodeDTO,
  ApiResponseNodeHealthDTO,
  ApiResponseNotificationDTO,
  ApiResponseNotifyChannelDTO,
  ApiResponseOauthAccountDTO,
  ApiResponseOperationTemplateDTO,
  ApiResponseOpsTaskDTO,
  ApiResponseOrgNodeDTO,
  ApiResponsePageResultAlertDTO,
  ApiResponsePageResultAlertHistoryDTO,
  ApiResponsePageResultAlertRuleDTO,
  ApiResponsePageResultApiAccessLogDTO,
  ApiResponsePageResultApiKeyDTO,
  ApiResponsePageResultAuditLogDTO,
  ApiResponsePageResultClusterDTO,
  ApiResponsePageResultDataSourceDTO,
  ApiResponsePageResultDeptRoleDTO,
  ApiResponsePageResultDictDTO,
  ApiResponsePageResultFileDTO,
  ApiResponsePageResultGroupRoleDTO,
  ApiResponsePageResultIntegrationConfigDTO,
  ApiResponsePageResultJobDiagnosticLogDTO,
  ApiResponsePageResultJobDTO,
  ApiResponsePageResultJobFunnelDTO,
  ApiResponsePageResultJobInstanceDTO,
  ApiResponsePageResultJobLogDTO,
  ApiResponsePageResultJobMetricDTO,
  ApiResponsePageResultJobPermissionDTO,
  ApiResponsePageResultLabelDTO,
  ApiResponsePageResultLoginHistoryDTO,
  ApiResponsePageResultMetricDashboardDTO,
  ApiResponsePageResultNodeDTO,
  ApiResponsePageResultNodeHealthDTO,
  ApiResponsePageResultNotificationDTO,
  ApiResponsePageResultNotifyChannelDTO,
  ApiResponsePageResultOauthAccountDTO,
  ApiResponsePageResultOperationTemplateDTO,
  ApiResponsePageResultOpsTaskDTO,
  ApiResponsePageResultOrgNodeDTO,
  ApiResponsePageResultResourceDTO,
  ApiResponsePageResultResourceGroupDTO,
  ApiResponsePageResultResourceLabelDTO,
  ApiResponsePageResultRoleDataScopeDTO,
  ApiResponsePageResultRoleDTO,
  ApiResponsePageResultRoleMenuDTO,
  ApiResponsePageResultRolePermissionDTO,
  ApiResponsePageResultSysParamDTO,
  ApiResponsePageResultTagDTO,
  ApiResponsePageResultTenantConfigDTO,
  ApiResponsePageResultTenantDTO,
  ApiResponsePageResultTenantResourceDTO,
  ApiResponsePageResultUserApiDTO,
  ApiResponsePageResultUserDepartmentDTO,
  ApiResponsePageResultUserDTO,
  ApiResponsePageResultUserGroupDTO,
  ApiResponsePageResultUserPermissionDTO,
  ApiResponsePageResultUserPostDTO,
  ApiResponsePageResultUserRoleDTO,
  ApiResponsePageResultUserTenantDTO,
  ApiResponseProfileDTO,
  ApiResponseResourceDTO,
  ApiResponseResourceGroupDTO,
  ApiResponseResourceLabelDTO,
  ApiResponseRoleDataScopeDTO,
  ApiResponseRoleDTO,
  ApiResponseRoleMenuDTO,
  ApiResponseRolePermissionDTO,
  ApiResponseSsoAuthResponseDTO,
  ApiResponseSysParamDTO,
  ApiResponseTagDTO,
  ApiResponseTenantConfigDTO,
  ApiResponseTenantDTO,
  ApiResponseTenantResourceDTO,
  ApiResponseUserApiDTO,
  ApiResponseUserDepartmentDTO,
  ApiResponseUserDTO,
  ApiResponseUserGroupDTO,
  ApiResponseUserPermissionDTO,
  ApiResponseUserPermissionResDTO,
  ApiResponseUserPostDTO,
  ApiResponseUserRoleDTO,
  ApiResponseUserTenantDTO,
  ApiResponseUserTokenStateDTO,
  ApiResponseVoid,
  ApiWhitelistDTO,
  AuditLogDTO,
  ClusterDTO,
  DataSourceDTO,
  DictDTO,
  FileDTO,
  IntegrationConfigDTO,
  JobDTO,
  JobInstanceDTO,
  JobLogDTO,
  JobMetricDTO,
  JobRegisterRequestDTO,
  LabelDTO,
  LoginHistoryDTO,
  MetricDashboardDTO,
  NodeDTO,
  NotificationDTO,
  NotifyChannelDTO,
  OauthAccountDTO,
  OperationTemplateDTO,
  OpsTaskDTO,
  OrgNodeDTO,
  ProfileDTO,
  ResourceDTO,
  ResourceGroupDTO,
  RoleDTO,
  SsoAuthLoginRequestDTO,
  SsoAuthRegisterRequestDTO,
  SysParamDTO,
  TagDTO,
  TenantConfigDTO,
  TenantDTO,
  UserDTO,
} from "./data-contracts";
// 1. 类型：只用于类型推导
import type { ContentType, RequestParams } from "./http-client";
// 2. 实现：会被 JS 编译，运行时可用
import { HttpClient } from "./http-client";

export class Api<
  SecurityDataType = unknown,
> extends HttpClient<SecurityDataType> {
  /**
   * @description Update user info
   *
   * @tags api
   * @name UpdateUser
   * @summary 更新用户信息
   * @request PUT:/api/user/update
   * @secure
   */
  updateUser = (data: UserDTO, params: RequestParams = {}) =>
    this.request<ApiResponseUserDTO, any>({
      path: `/api/user/update`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateSsoUserProfile
   * @request PUT:/api/user/profile
   * @secure
   */
  updateSsoUserProfile = (data: UserDTO, params: RequestParams = {}) =>
    this.request<void, any>({
      path: `/api/user/profile`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Update tenant info
   *
   * @tags api
   * @name UpdateTenant
   * @summary 更新租户信息
   * @request PUT:/api/tenant/update
   * @secure
   */
  updateTenant = (data: TenantDTO, params: RequestParams = {}) =>
    this.request<ApiResponseTenantDTO, any>({
      path: `/api/tenant/update`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateTenantConfig
   * @request PUT:/api/tenant-config
   * @secure
   */
  updateTenantConfig = (data: TenantConfigDTO, params: RequestParams = {}) =>
    this.request<ApiResponseTenantConfigDTO, any>({
      path: `/api/tenant-config`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateTenantConfig
   * @request POST:/api/tenant-config
   * @secure
   */
  createTenantConfig = (data: TenantConfigDTO, params: RequestParams = {}) =>
    this.request<ApiResponseTenantConfigDTO, any>({
      path: `/api/tenant-config`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllTags
   * @request GET:/api/tag
   * @secure
   */
  getAllTags = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultTagDTO, any>({
      path: `/api/tag`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateTag
   * @request PUT:/api/tag
   * @secure
   */
  updateTag = (data: TagDTO, params: RequestParams = {}) =>
    this.request<ApiResponseTagDTO, any>({
      path: `/api/tag`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateTag
   * @request POST:/api/tag
   * @secure
   */
  createTag = (data: TagDTO, params: RequestParams = {}) =>
    this.request<ApiResponseTagDTO, any>({
      path: `/api/tag`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllSysParams
   * @request GET:/api/sys-param
   * @secure
   */
  getAllSysParams = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultSysParamDTO, any>({
      path: `/api/sys-param`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateSysParam
   * @request PUT:/api/sys-param
   * @secure
   */
  updateSysParam = (data: SysParamDTO, params: RequestParams = {}) =>
    this.request<ApiResponseSysParamDTO, any>({
      path: `/api/sys-param`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateSysParam
   * @request POST:/api/sys-param
   * @secure
   */
  createSysParam = (data: SysParamDTO, params: RequestParams = {}) =>
    this.request<ApiResponseSysParamDTO, any>({
      path: `/api/sys-param`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateRole
   * @summary 编辑角色
   * @request PUT:/api/role/update
   * @secure
   */
  updateRole = (data: RoleDTO, params: RequestParams = {}) =>
    this.request<ApiResponseRoleDTO, any>({
      path: `/api/role/update`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllResources
   * @request GET:/api/resource
   * @secure
   */
  getAllResources = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultResourceDTO, any>({
      path: `/api/resource`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateResource
   * @request PUT:/api/resource
   * @secure
   */
  updateResource = (data: ResourceDTO, params: RequestParams = {}) =>
    this.request<ApiResponseResourceDTO, any>({
      path: `/api/resource`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateResource
   * @request POST:/api/resource
   * @secure
   */
  createResource = (data: ResourceDTO, params: RequestParams = {}) =>
    this.request<ApiResponseResourceDTO, any>({
      path: `/api/resource`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllResourceGroups
   * @request GET:/api/resource-group
   * @secure
   */
  getAllResourceGroups = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultResourceGroupDTO, any>({
      path: `/api/resource-group`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateResourceGroup
   * @request PUT:/api/resource-group
   * @secure
   */
  updateResourceGroup = (data: ResourceGroupDTO, params: RequestParams = {}) =>
    this.request<ApiResponseResourceGroupDTO, any>({
      path: `/api/resource-group`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateResourceGroup
   * @request POST:/api/resource-group
   * @secure
   */
  createResourceGroup = (data: ResourceGroupDTO, params: RequestParams = {}) =>
    this.request<ApiResponseResourceGroupDTO, any>({
      path: `/api/resource-group`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateProfile
   * @summary 更新用户档案
   * @request PUT:/api/profile
   * @secure
   */
  updateProfile = (data: ProfileDTO, params: RequestParams = {}) =>
    this.request<ApiResponseProfileDTO, any>({
      path: `/api/profile`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateProfile
   * @summary 创建用户档案
   * @request POST:/api/profile
   * @secure
   */
  createProfile = (data: ProfileDTO, params: RequestParams = {}) =>
    this.request<ApiResponseProfileDTO, any>({
      path: `/api/profile`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllOperationTemplates
   * @request GET:/api/operation-template
   * @secure
   */
  getAllOperationTemplates = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultOperationTemplateDTO, any>({
      path: `/api/operation-template`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateOperationTemplate
   * @request PUT:/api/operation-template
   * @secure
   */
  updateOperationTemplate = (
    data: OperationTemplateDTO,
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseOperationTemplateDTO, any>({
      path: `/api/operation-template`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateOperationTemplate
   * @request POST:/api/operation-template
   * @secure
   */
  createOperationTemplate = (
    data: OperationTemplateDTO,
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseOperationTemplateDTO, any>({
      path: `/api/operation-template`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllNotifications
   * @request GET:/api/notification
   * @secure
   */
  getAllNotifications = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultNotificationDTO, any>({
      path: `/api/notification`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateNotification
   * @request PUT:/api/notification
   * @secure
   */
  createNotification = (data: NotificationDTO, params: RequestParams = {}) =>
    this.request<ApiResponseNotificationDTO, any>({
      path: `/api/notification`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateNotification1
   * @request POST:/api/notification
   * @secure
   */
  createNotification1 = (data: NotificationDTO, params: RequestParams = {}) =>
    this.request<ApiResponseNotificationDTO, any>({
      path: `/api/notification`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllNodes
   * @request GET:/api/node
   * @secure
   */
  getAllNodes = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultNodeDTO, any>({
      path: `/api/node`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateNode
   * @request PUT:/api/node
   * @secure
   */
  updateNode = (data: NodeDTO, params: RequestParams = {}) =>
    this.request<ApiResponseNodeDTO, any>({
      path: `/api/node`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateNode
   * @request POST:/api/node
   * @secure
   */
  createNode = (data: NodeDTO, params: RequestParams = {}) =>
    this.request<ApiResponseNodeDTO, any>({
      path: `/api/node`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllMetricDashboards
   * @request GET:/api/metric-dashboard
   * @secure
   */
  getAllMetricDashboards = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultMetricDashboardDTO, any>({
      path: `/api/metric-dashboard`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateMetricDashboard
   * @request PUT:/api/metric-dashboard
   * @secure
   */
  updateMetricDashboard = (
    data: MetricDashboardDTO,
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseMetricDashboardDTO, any>({
      path: `/api/metric-dashboard`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateMetricDashboard
   * @request POST:/api/metric-dashboard
   * @secure
   */
  createMetricDashboard = (
    data: MetricDashboardDTO,
    params: RequestParams = {},
  ) =>
    this.request<MetricDashboardDTO, any>({
      path: `/api/metric-dashboard`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateLabel
   * @request PUT:/api/label
   * @secure
   */
  updateLabel = (data: LabelDTO, params: RequestParams = {}) =>
    this.request<ApiResponseLabelDTO, any>({
      path: `/api/label`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateLabel
   * @request POST:/api/label
   * @secure
   */
  createLabel = (data: LabelDTO, params: RequestParams = {}) =>
    this.request<ApiResponseLabelDTO, any>({
      path: `/api/label`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Update job info
   *
   * @tags api
   * @name UpdateJob
   * @summary 更新任务信息
   * @request PUT:/api/job/update
   * @secure
   */
  updateJob = (data: JobDTO, params: RequestParams = {}) =>
    this.request<ApiResponseJobDTO, any>({
      path: `/api/job/update`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllIntegrationConfigs
   * @request GET:/api/integration-config
   * @secure
   */
  getAllIntegrationConfigs = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultIntegrationConfigDTO, any>({
      path: `/api/integration-config`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateIntegrationConfig
   * @request PUT:/api/integration-config
   * @secure
   */
  updateIntegrationConfig = (
    data: IntegrationConfigDTO,
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseIntegrationConfigDTO, any>({
      path: `/api/integration-config`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateIntegrationConfig
   * @request POST:/api/integration-config
   * @secure
   */
  createIntegrationConfig = (
    data: IntegrationConfigDTO,
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseIntegrationConfigDTO, any>({
      path: `/api/integration-config`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllFiles
   * @request GET:/api/file
   * @secure
   */
  getAllFiles = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultFileDTO, any>({
      path: `/api/file`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateFile
   * @request PUT:/api/file
   * @secure
   */
  updateFile = (data: FileDTO, params: RequestParams = {}) =>
    this.request<ApiResponseFileDTO, any>({
      path: `/api/file`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateFile
   * @request POST:/api/file
   * @secure
   */
  createFile = (data: FileDTO, params: RequestParams = {}) =>
    this.request<ApiResponseFileDTO, any>({
      path: `/api/file`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateDict
   * @request PUT:/api/dict
   * @secure
   */
  updateDict = (data: DictDTO, params: RequestParams = {}) =>
    this.request<ApiResponseDictDTO, any>({
      path: `/api/dict`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateDict
   * @request POST:/api/dict
   * @secure
   */
  createDict = (data: DictDTO, params: RequestParams = {}) =>
    this.request<ApiResponseDictDTO, any>({
      path: `/api/dict`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Get clusters by tenant
   *
   * @tags api
   * @name GetAllDataSources
   * @request GET:/api/data-source
   * @secure
   */
  getAllDataSources = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultDataSourceDTO, any>({
      path: `/api/data-source`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get clusters by tenant
   *
   * @tags api
   * @name UpdateDataSource
   * @request PUT:/api/data-source
   * @secure
   */
  updateDataSource = (data: DataSourceDTO, params: RequestParams = {}) =>
    this.request<ApiResponseDataSourceDTO, any>({
      path: `/api/data-source`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Get clusters by tenant
   *
   * @tags api
   * @name CreateDataSource
   * @request POST:/api/data-source
   * @secure
   */
  createDataSource = (data: DataSourceDTO, params: RequestParams = {}) =>
    this.request<ApiResponseDataSourceDTO, any>({
      path: `/api/data-source`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Update cluster info
   *
   * @tags api
   * @name UpdateCluster
   * @summary 更新集群信息
   * @request PUT:/api/cluster/update
   * @secure
   */
  updateCluster = (data: ClusterDTO, params: RequestParams = {}) =>
    this.request<ApiResponseClusterDTO, any>({
      path: `/api/cluster/update`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateAuditLog
   * @request PUT:/api/audit-log
   * @secure
   */
  updateAuditLog = (data: AuditLogDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAuditLogDTO, any>({
      path: `/api/audit-log`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateAuditLog
   * @summary 分页获取所有报警历史记录
   * @request POST:/api/audit-log
   * @secure
   */
  createAuditLog = (data: AuditLogDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAuditLogDTO, any>({
      path: `/api/audit-log`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateApiWhitelist
   * @summary 分页获取所有报警历史记录
   * @request PUT:/api/api-whitelist
   * @secure
   */
  updateApiWhitelist = (data: ApiWhitelistDTO, params: RequestParams = {}) =>
    this.request<ApiResponseApiWhitelistDTO, any>({
      path: `/api/api-whitelist`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateApiWhitelist
   * @summary 分页获取所有报警历史记录
   * @request POST:/api/api-whitelist
   * @secure
   */
  createApiWhitelist = (data: ApiWhitelistDTO, params: RequestParams = {}) =>
    this.request<ApiResponseApiWhitelistDTO, any>({
      path: `/api/api-whitelist`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllApiKeys
   * @request GET:/api/api-key
   * @secure
   */
  getAllApiKeys = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultApiKeyDTO, any>({
      path: `/api/api-key`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateApiKey
   * @request PUT:/api/api-key
   * @secure
   */
  updateApiKey = (data: ApiKeyDTO, params: RequestParams = {}) =>
    this.request<ApiResponseApiKeyDTO, any>({
      path: `/api/api-key`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateApiKey
   * @request POST:/api/api-key
   * @secure
   */
  createApiKey = (data: ApiKeyDTO, params: RequestParams = {}) =>
    this.request<ApiKeyDTO, any>({
      path: `/api/api-key`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllApiAccessLogs
   * @request GET:/api/api-access-log
   * @secure
   */
  getAllApiAccessLogs = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultApiAccessLogDTO, any>({
      path: `/api/api-access-log`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateApiAccessLog
   * @request PUT:/api/api-access-log
   * @secure
   */
  updateApiAccessLog = (data: ApiAccessLogDTO, params: RequestParams = {}) =>
    this.request<ApiResponseApiAccessLogDTO, any>({
      path: `/api/api-access-log`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateApiAccessLog
   * @request POST:/api/api-access-log
   * @secure
   */
  createApiAccessLog = (data: ApiAccessLogDTO, params: RequestParams = {}) =>
    this.request<ApiResponseApiAccessLogDTO, any>({
      path: `/api/api-access-log`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Update alert info
   *
   * @tags api
   * @name UpdateAlert
   * @summary 更新报警事件
   * @request PUT:/api/alert/update
   * @secure
   */
  updateAlert = (data: AlertDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAlertDTO, any>({
      path: `/api/alert/update`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllAlertHistorys
   * @summary 分页获取所有报警历史记录
   * @request GET:/api/alert-history
   * @secure
   */
  getAllAlertHistorys = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAlertHistoryDTO, any>({
      path: `/api/alert-history`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateAlertHistory
   * @summary 更新报警历史记录
   * @request PUT:/api/alert-history
   * @secure
   */
  updateAlertHistory = (data: AlertHistoryDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAlertHistoryDTO, any>({
      path: `/api/alert-history`,
      method: "PUT",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Update alert info
   *
   * @tags api
   * @name CreateAlertHistory
   * @summary 创建报警历史事件
   * @request POST:/api/alert-history
   * @secure
   */
  createAlertHistory = (data: AlertHistoryDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAlertHistoryDTO, any>({
      path: `/api/alert-history`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name Register
   * @summary 注册
   * @request POST:/sso/register
   * @secure
   */
  register = (data: SsoAuthRegisterRequestDTO, params: RequestParams = {}) =>
    this.request<ApiResponseSsoAuthResponseDTO, any>({
      path: `/sso/register`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UnbindSsoOauthAccount
   * @summary 解绑第三方账号
   * @request POST:/sso/oauth-account/unbind
   * @secure
   */
  unbindSsoOauthAccount = (
    query: {
      /** @format int64 */
      userId: number;
      provider: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseVoid, any>({
      path: `/sso/oauth-account/unbind`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name BindSsoOauthAccount
   * @summary 绑定第三方账号
   * @request POST:/sso/oauth-account/bind
   * @secure
   */
  bindSsoOauthAccount = (data: OauthAccountDTO, params: RequestParams = {}) =>
    this.request<ApiResponseOauthAccountDTO, any>({
      path: `/sso/oauth-account/bind`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name Login
   * @summary 账号密码登录
   * @request POST:/sso/login
   * @secure
   */
  login = (data: SsoAuthLoginRequestDTO, params: RequestParams = {}) =>
    this.request<ApiResponseSsoAuthResponseDTO, any>({
      path: `/sso/login`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name SsoUserRegister
   * @request POST:/api/user/register
   * @secure
   */
  ssoUserRegister = (data: UserDTO, params: RequestParams = {}) =>
    this.request<UserDTO, any>({
      path: `/api/user/register`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name Logout
   * @summary 本地 JWT 登出（吊销当前访问令牌）
   * @request POST:/api/user/logout
   * @secure
   */
  logout = (
    query?: {
      /** @format int64 */
      userId?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/user/logout`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Create new user
   *
   * @tags api
   * @name CreateUser
   * @summary 创建用户
   * @request POST:/api/user/create
   * @secure
   */
  createUser = (data: UserDTO, params: RequestParams = {}) =>
    this.request<ApiResponseUserDTO, any>({
      path: `/api/user/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Check user password
   *
   * @tags api
   * @name CheckPassword
   * @summary 校验密码
   * @request POST:/api/user/checkPassword
   * @secure
   */
  checkPassword = (
    query: {
      /** @format int64 */
      userId: number;
      rawPwd: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseBoolean, any>({
      path: `/api/user/checkPassword`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveUserTenant
   * @request POST:/api/user-tenant/remove
   * @secure
   */
  removeUserTenant = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/user-tenant/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignUserTenant
   * @request POST:/api/user-tenant/assign
   * @secure
   */
  assignUserTenant = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      tenantId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserTenantDTO, any>({
      path: `/api/user-tenant/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveUserRole
   * @summary 用户移除角色
   * @request POST:/api/user-role/remove
   * @secure
   */
  removeUserRole = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      roleId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/user-role/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignUserRole
   * @summary 用户分配角色
   * @request POST:/api/user-role/assign
   * @secure
   */
  assignUserRole = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      roleId: number;
      /** @format int64 */
      tenantId?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserRoleDTO, any>({
      path: `/api/user-role/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveUserPost
   * @request POST:/api/user-post/remove
   * @secure
   */
  removeUserPost = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      postId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/user-post/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignUserPost
   * @request POST:/api/user-post/assign
   * @secure
   */
  assignUserPost = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      postId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserPostDTO, any>({
      path: `/api/user-post/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveUserPermission
   * @request POST:/api/user-permission/remove
   * @secure
   */
  removeUserPermission = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      permissionId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/user-permission/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignUserPermission
   * @request POST:/api/user-permission/assign
   * @secure
   */
  assignUserPermission = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      permissionId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserPermissionDTO, any>({
      path: `/api/user-permission/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveUserGroup
   * @request POST:/api/user-group/remove
   * @secure
   */
  removeUserGroup = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      groupId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/user-group/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignUserGroup
   * @request POST:/api/user-group/assign
   * @secure
   */
  assignUserGroup = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      groupId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserGroupDTO, any>({
      path: `/api/user-group/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveUserDepartment
   * @request POST:/api/user-department/remove
   * @secure
   */
  removeUserDepartment = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      departmentId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/user-department/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignUserDepartment
   * @request POST:/api/user-department/assign
   * @secure
   */
  assignUserDepartment = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      departmentId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserDepartmentDTO, any>({
      path: `/api/user-department/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveUserApi
   * @request POST:/api/user-api/remove
   * @secure
   */
  removeUserApi = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      apiId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/user-api/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignUserApi
   * @request POST:/api/user-api/assign
   * @secure
   */
  assignUserApi = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      apiId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserApiDTO, any>({
      path: `/api/user-api/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Create new tenant
   *
   * @tags api
   * @name CreateTenant
   * @summary 新建租户
   * @request POST:/api/tenant/create
   * @secure
   */
  createTenant = (data: TenantDTO, params: RequestParams = {}) =>
    this.request<ApiResponseTenantDTO, any>({
      path: `/api/tenant/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Delete tenant
   *
   * @tags api
   * @name RemoveTenantResource
   * @summary 删除租户
   * @request POST:/api/tenant-resource/remove
   * @secure
   */
  removeTenantResource = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format int64 */
      resourceId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/tenant-resource/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Delete tenant
   *
   * @tags api
   * @name AssignTenantResource
   * @summary 创建
   * @request POST:/api/tenant-resource/assign
   * @secure
   */
  assignTenantResource = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format int64 */
      resourceId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseTenantResourceDTO, any>({
      path: `/api/tenant-resource/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Create new role
   *
   * @tags api
   * @name CreateRole
   * @summary 新建角色
   * @request POST:/api/role/create
   * @secure
   */
  createRole = (data: RoleDTO, params: RequestParams = {}) =>
    this.request<ApiResponseRoleDTO, any>({
      path: `/api/role/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveRolePermission
   * @request POST:/api/role-permission/remove
   * @secure
   */
  removeRolePermission = (
    query: {
      /** @format int64 */
      roleId: number;
      /** @format int64 */
      permissionId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/role-permission/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignRolePermission
   * @request POST:/api/role-permission/assign
   * @secure
   */
  assignRolePermission = (
    query: {
      /** @format int64 */
      roleId: number;
      /** @format int64 */
      permissionId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseRolePermissionDTO, any>({
      path: `/api/role-permission/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveRoleMenu
   * @request POST:/api/role-menu/remove
   * @secure
   */
  removeRoleMenu = (
    query: {
      /** @format int64 */
      roleId: number;
      /** @format int64 */
      menuId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/role-menu/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignRoleMenu
   * @request POST:/api/role-menu/assign
   * @secure
   */
  assignRoleMenu = (
    query: {
      /** @format int64 */
      roleId: number;
      /** @format int64 */
      menuId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseRoleMenuDTO, any>({
      path: `/api/role-menu/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveRoleDataScope
   * @request POST:/api/role-data-scope/remove
   * @secure
   */
  removeRoleDataScope = (
    query: {
      /** @format int64 */
      roleId: number;
      /** @format int64 */
      dataScopeId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/role-data-scope/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignRoleDataScope
   * @request POST:/api/role-data-scope/assign
   * @secure
   */
  assignRoleDataScope = (
    query: {
      /** @format int64 */
      roleId: number;
      /** @format int64 */
      dataScopeId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseRoleDataScopeDTO, any>({
      path: `/api/role-data-scope/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveResourceLabel
   * @request POST:/api/resource-label/remove
   * @secure
   */
  removeResourceLabel = (
    query: {
      /** @format int64 */
      resourceId: number;
      /** @format int64 */
      labelId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/resource-label/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignResourceLabel
   * @request POST:/api/resource-label/assign
   * @secure
   */
  assignResourceLabel = (
    query: {
      /** @format int64 */
      resourceId: number;
      /** @format int64 */
      labelId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseResourceLabelDTO, any>({
      path: `/api/resource-label/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateOrgNode
   * @request POST:/api/org-node/update
   * @secure
   */
  updateOrgNode = (data: OrgNodeDTO, params: RequestParams = {}) =>
    this.request<ApiResponseOrgNodeDTO, any>({
      path: `/api/org-node/update`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteOrgNode
   * @request POST:/api/org-node/delete/{id}
   * @secure
   */
  deleteOrgNode = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/org-node/delete/${id}`,
      method: "POST",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateOrgNode
   * @request POST:/api/org-node/create
   * @secure
   */
  createOrgNode = (data: OrgNodeDTO, params: RequestParams = {}) =>
    this.request<ApiResponseOrgNodeDTO, any>({
      path: `/api/org-node/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateOpsTask
   * @request POST:/api/ops-task/update
   * @secure
   */
  updateOpsTask = (data: OpsTaskDTO, params: RequestParams = {}) =>
    this.request<ApiResponseOpsTaskDTO, any>({
      path: `/api/ops-task/update`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteOpsTask
   * @request POST:/api/ops-task/delete/{id}
   * @secure
   */
  deleteOpsTask = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/ops-task/delete/${id}`,
      method: "POST",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateOpsTask
   * @request POST:/api/ops-task/create
   * @secure
   */
  createOpsTask = (data: OpsTaskDTO, params: RequestParams = {}) =>
    this.request<ApiResponseOpsTaskDTO, any>({
      path: `/api/ops-task/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateNotifyChannel
   * @request POST:/api/notify-channel/update
   * @secure
   */
  updateNotifyChannel = (data: NotifyChannelDTO, params: RequestParams = {}) =>
    this.request<ApiResponseNotifyChannelDTO, any>({
      path: `/api/notify-channel/update`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteNotifyChannel
   * @request POST:/api/notify-channel/delete/{id}
   * @secure
   */
  deleteNotifyChannel = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/notify-channel/delete/${id}`,
      method: "POST",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateNotifyChannel
   * @request POST:/api/notify-channel/create
   * @secure
   */
  createNotifyChannel = (data: NotifyChannelDTO, params: RequestParams = {}) =>
    this.request<ApiResponseNotifyChannelDTO, any>({
      path: `/api/notify-channel/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Create job metric
   *
   * @tags api
   * @name CreateMetric
   * @summary 新建任务指标
   * @request POST:/api/metric/create
   * @secure
   */
  createMetric = (data: JobMetricDTO, params: RequestParams = {}) =>
    this.request<ApiResponseJobMetricDTO, any>({
      path: `/api/metric/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateLoginHistory
   * @request POST:/api/login-history
   * @secure
   */
  createLoginHistory = (data: LoginHistoryDTO, params: RequestParams = {}) =>
    this.request<ApiResponseLoginHistoryDTO, any>({
      path: `/api/login-history`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Create job log
   *
   * @tags api
   * @name CreateJobLog
   * @summary 新建任务日志
   * @request POST:/api/joblog/create
   * @secure
   */
  createJobLog = (data: JobLogDTO, params: RequestParams = {}) =>
    this.request<ApiResponseJobLogDTO, any>({
      path: `/api/joblog/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RegisterJob
   * @request POST:/api/job/register
   * @secure
   */
  registerJob = (data: JobRegisterRequestDTO, params: RequestParams = {}) =>
    this.request<ApiResponseJobInfoDTO, any>({
      path: `/api/job/register`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * @description Create new job
   *
   * @tags api
   * @name CreateJob
   * @summary 新建任务
   * @request POST:/api/job/create
   * @secure
   */
  createJob = (data: JobDTO, params: RequestParams = {}) =>
    this.request<ApiResponseJobDTO, any>({
      path: `/api/job/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RevokePermission
   * @request POST:/api/job-permission/revoke
   * @secure
   */
  revokePermission = (
    query: {
      /** @format int64 */
      jobId: number;
      userId: string;
      /** @format int64 */
      permissionId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/job-permission/revoke`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GrantJobPermissionByJobAndUserAndPermissionAndTenant
   * @request POST:/api/job-permission/grant
   * @secure
   */
  grantJobPermissionByJobAndUserAndPermissionAndTenant = (
    query: {
      /** @format int64 */
      jobId: number;
      userId: string;
      /** @format int64 */
      permissionId: number;
      /** @format int64 */
      tenantId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/job-permission/grant`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateJobInstance
   * @request POST:/api/job-instance/update-status
   * @secure
   */
  updateJobInstance = (
    query: {
      /** @format int64 */
      id: number;
      /** @format int32 */
      status: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/job-instance/update-status`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateJobInstance
   * @request POST:/api/job-instance/create
   * @secure
   */
  createJobInstance = (data: JobInstanceDTO, params: RequestParams = {}) =>
    this.request<ApiResponseJobInstanceDTO, any>({
      path: `/api/job-instance/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveGroupRole
   * @request POST:/api/group-role/remove
   * @secure
   */
  removeGroupRole = (
    query: {
      /** @format int64 */
      groupId: number;
      /** @format int64 */
      roleId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/group-role/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignGroupRole
   * @request POST:/api/group-role/assign
   * @secure
   */
  assignGroupRole = (
    query: {
      /** @format int64 */
      groupId: number;
      /** @format int64 */
      roleId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseGroupRoleDTO, any>({
      path: `/api/group-role/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name RemoveDeptRole
   * @request POST:/api/dept-role/remove
   * @secure
   */
  removeDeptRole = (
    query: {
      /** @format int64 */
      deptId: number;
      /** @format int64 */
      roleId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<boolean, any>({
      path: `/api/dept-role/remove`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name AssignDeptRole
   * @request POST:/api/dept-role/assign
   * @secure
   */
  assignDeptRole = (
    query: {
      /** @format int64 */
      deptId: number;
      /** @format int64 */
      roleId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseDeptRoleDTO, any>({
      path: `/api/dept-role/assign`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Create new cluster
   *
   * @tags api
   * @name CreateCluster
   * @summary 新建集群
   * @request POST:/api/cluster/create
   * @secure
   */
  createCluster = (data: ClusterDTO, params: RequestParams = {}) =>
    this.request<ApiResponseClusterDTO, any>({
      path: `/api/cluster/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateAlert
   * @summary 新建报警事件
   * @request POST:/api/alert/create
   * @secure
   */
  createAlert = (data: AlertDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAlertDTO, any>({
      path: `/api/alert/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UpdateAlertRule
   * @summary 更新报警规则
   * @request POST:/api/alert-rule/update
   * @secure
   */
  updateAlertRule = (data: AlertRuleDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAlertRuleDTO, any>({
      path: `/api/alert-rule/update`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteAlertRule
   * @summary 删除报警规则
   * @request POST:/api/alert-rule/delete
   * @secure
   */
  deleteAlertRule = (
    query: {
      /** @format int64 */
      id: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseBoolean, any>({
      path: `/api/alert-rule/delete`,
      method: "POST",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CreateAlertRule
   * @summary 新建报警规则
   * @request POST:/api/alert-rule/create
   * @secure
   */
  createAlertRule = (data: AlertRuleDTO, params: RequestParams = {}) =>
    this.request<ApiResponseAlertRuleDTO, any>({
      path: `/api/alert-rule/create`,
      method: "POST",
      body: data,
      secure: true,
      type: ContentType.Json,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name SsoLogin
   * @summary SSO 登录入口（重定向到认证中心）
   * @request GET:/sso/sso-login
   * @secure
   */
  ssoLogin = (
    query?: {
      redirect?: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<void, any>({
      path: `/sso/sso-login`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetSsoOauthAccountsByUser
   * @summary 查询当前用户所有已绑定账号
   * @request GET:/sso/oauth-account/list
   * @secure
   */
  getSsoOauthAccountsByUser = (
    query: {
      /** @format int64 */
      userId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultOauthAccountDTO, any>({
      path: `/sso/oauth-account/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetSsoOauthAccountByOpenId
   * @summary 根据平台openId查账号
   * @request GET:/sso/oauth-account/by-openid
   * @secure
   */
  getSsoOauthAccountByOpenId = (
    query: {
      provider: string;
      openid: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseOauthAccountDTO, any>({
      path: `/sso/oauth-account/by-openid`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description 退出 Keycloak 会话，可携带 id_token_hint 与 post_logout_redirect_uri
   *
   * @tags api
   * @name SsoLogout
   * @summary SSO 登出（重定向至 IdP 退出）
   * @request GET:/sso/logout
   * @secure
   */
  ssoLogout = (
    query?: {
      redirect?: string;
      idTokenHint?: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<void, any>({
      path: `/sso/logout`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name SsoCallback
   * @summary SSO 回调（处理授权码 -> 颁发本地JWT）
   * @request GET:/sso/callback
   * @secure
   */
  ssoCallback = (
    query: {
      code: string;
      state?: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<void, any>({
      path: `/sso/callback`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserTokenState
   * @summary 获取当前用户的Token版本状态
   * @request GET:/api/user/token-state/{userId}
   * @secure
   */
  getUserTokenState = (userId: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserTokenStateDTO, any>({
      path: `/api/user/token-state/${userId}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name UserPermissions
   * @summary 获取当前用户权限码列表
   * @request GET:/api/user/permissions
   * @secure
   */
  userPermissions = (
    query: {
      /** @format int64 */
      userId: number;
      /** @format int64 */
      tenantId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseUserPermissionResDTO, any>({
      path: `/api/user/permissions`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description 通过JWT/Session获取当前登录用户基础资料和角色信息
   *
   * @tags api
   * @name GetCurrentUser
   * @summary 获取当前用户信息
   * @request GET:/api/user/me
   * @secure
   */
  getCurrentUser = (params: RequestParams = {}) =>
    this.request<ApiResponseUserDTO, any>({
      path: `/api/user/me`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Get user list by tenant with paging
   *
   * @tags api
   * @name GetUsersByTenant
   * @summary 分页查询用户
   * @request GET:/api/user/list
   * @secure
   */
  getUsersByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserDTO, any>({
      path: `/api/user/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get user by ID
   *
   * @tags api
   * @name GetUser
   * @summary 根据ID查询用户
   * @request GET:/api/user/id/{id}
   * @secure
   */
  getUser = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserDTO, any>({
      path: `/api/user/id/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Soft delete user
   *
   * @tags api
   * @name DeleteUser
   * @summary 软删除用户
   * @request DELETE:/api/user/id/{id}
   * @secure
   */
  deleteUser = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/user/id/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetSsoUserByAccount
   * @request GET:/api/user/account/{account}
   * @secure
   */
  getSsoUserByAccount = (account: string, params: RequestParams = {}) =>
    this.request<ApiResponseUserDTO, any>({
      path: `/api/user/account/${account}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserTenant
   * @request GET:/api/user-tenant/{id}
   * @secure
   */
  getUserTenant = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserTenantDTO, any>({
      path: `/api/user-tenant/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteUserTenant
   * @request DELETE:/api/user-tenant/{id}
   * @secure
   */
  deleteUserTenant = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/user-tenant/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserTenantsByUser
   * @request GET:/api/user-tenant/user/{userId}
   * @secure
   */
  getUserTenantsByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserTenantDTO, any>({
      path: `/api/user-tenant/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserTenantsByTenant
   * @request GET:/api/user-tenant/tenant/{tenantId}
   * @secure
   */
  getUserTenantsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserTenantDTO, any>({
      path: `/api/user-tenant/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserRole
   * @request GET:/api/user-role/{id}
   * @secure
   */
  getUserRole = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserRoleDTO, any>({
      path: `/api/user-role/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteUserRole
   * @request DELETE:/api/user-role/{id}
   * @secure
   */
  deleteUserRole = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/user-role/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserRolesByTenant
   * @request GET:/api/user-role/by_tenant/{tenantId}
   * @secure
   */
  getUserRolesByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserRoleDTO, any>({
      path: `/api/user-role/by_tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserRolesByUser
   * @summary 获取用户所有角色
   * @request GET:/api/user-role/by-user/{userId}
   * @secure
   */
  getUserRolesByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserRoleDTO, any>({
      path: `/api/user-role/by-user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserRolesByRole
   * @summary 获取角色下所有用户
   * @request GET:/api/user-role/by-role/{roleId}
   * @secure
   */
  getUserRolesByRole = (
    roleId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserRoleDTO, any>({
      path: `/api/user-role/by-role/${roleId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserPost
   * @request GET:/api/user-post/{id}
   * @secure
   */
  getUserPost = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserPostDTO, any>({
      path: `/api/user-post/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteUserPost
   * @request DELETE:/api/user-post/{id}
   * @secure
   */
  deleteUserPost = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/user-post/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserPostsByUser
   * @request GET:/api/user-post/user/{userId}
   * @secure
   */
  getUserPostsByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserPostDTO, any>({
      path: `/api/user-post/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserPostsByPost
   * @request GET:/api/user-post/post/{postId}
   * @secure
   */
  getUserPostsByPost = (
    postId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserPostDTO, any>({
      path: `/api/user-post/post/${postId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserPermission
   * @request GET:/api/user-permission/{id}
   * @secure
   */
  getUserPermission = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserPermissionDTO, any>({
      path: `/api/user-permission/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteUserPermission
   * @request DELETE:/api/user-permission/{id}
   * @secure
   */
  deleteUserPermission = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/user-permission/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserPermissionsByUser
   * @request GET:/api/user-permission/user/{userId}
   * @secure
   */
  getUserPermissionsByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserPermissionDTO, any>({
      path: `/api/user-permission/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserPermissionsByPermission
   * @request GET:/api/user-permission/permission/{permissionId}
   * @secure
   */
  getUserPermissionsByPermission = (
    permissionId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserPermissionDTO, any>({
      path: `/api/user-permission/permission/${permissionId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserGroup
   * @request GET:/api/user-group/{id}
   * @secure
   */
  getUserGroup = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserGroupDTO, any>({
      path: `/api/user-group/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteUserGroup
   * @request DELETE:/api/user-group/{id}
   * @secure
   */
  deleteUserGroup = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/user-group/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserGroupsByUser
   * @request GET:/api/user-group/user/{userId}
   * @secure
   */
  getUserGroupsByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserGroupDTO, any>({
      path: `/api/user-group/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserGroupsByGroup
   * @request GET:/api/user-group/group/{groupId}
   * @secure
   */
  getUserGroupsByGroup = (
    groupId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserGroupDTO, any>({
      path: `/api/user-group/group/${groupId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserDepartment
   * @request GET:/api/user-department/{id}
   * @secure
   */
  getUserDepartment = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserDepartmentDTO, any>({
      path: `/api/user-department/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteUserDepartment
   * @request DELETE:/api/user-department/{id}
   * @secure
   */
  deleteUserDepartment = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/user-department/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserDepartmentsByUser
   * @request GET:/api/user-department/user/{userId}
   * @secure
   */
  getUserDepartmentsByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserDepartmentDTO, any>({
      path: `/api/user-department/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserDepartmentsByDepartment
   * @request GET:/api/user-department/department/{departmentId}
   * @secure
   */
  getUserDepartmentsByDepartment = (
    departmentId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserDepartmentDTO, any>({
      path: `/api/user-department/department/${departmentId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserApi
   * @request GET:/api/user-api/{id}
   * @secure
   */
  getUserApi = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseUserApiDTO, any>({
      path: `/api/user-api/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteUserApi
   * @request DELETE:/api/user-api/{id}
   * @secure
   */
  deleteUserApi = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/user-api/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserApisByUser
   * @request GET:/api/user-api/user/{userId}
   * @secure
   */
  getUserApisByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserApiDTO, any>({
      path: `/api/user-api/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetUserApisByApi
   * @request GET:/api/user-api/api/{apiId}
   * @secure
   */
  getUserApisByApi = (
    apiId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultUserApiDTO, any>({
      path: `/api/user-api/api/${apiId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get tenant by ID
   *
   * @tags api
   * @name GetTenant
   * @summary 根据ID查询租户
   * @request GET:/api/tenant/{id}
   * @secure
   */
  getTenant = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseTenantDTO, any>({
      path: `/api/tenant/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Delete tenant
   *
   * @tags api
   * @name DeleteTenant
   * @summary 删除租户
   * @request DELETE:/api/tenant/{id}
   * @secure
   */
  deleteTenant = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/tenant/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * @description Get all tenants
   *
   * @tags api
   * @name GetAllTenants
   * @summary 查询所有租户
   * @request GET:/api/tenant/list
   * @secure
   */
  getAllTenants = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultTenantDTO, any>({
      path: `/api/tenant/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Delete tenant
   *
   * @tags api
   * @name GetTenantResource
   * @summary 删除租户
   * @request GET:/api/tenant-resource/{id}
   * @secure
   */
  getTenantResource = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseTenantResourceDTO, any>({
      path: `/api/tenant-resource/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Delete tenant
   *
   * @tags api
   * @name DeleteTenantResource
   * @summary 删除租户
   * @request DELETE:/api/tenant-resource/{id}
   * @secure
   */
  deleteTenantResource = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/tenant-resource/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * @description Delete tenant
   *
   * @tags api
   * @name GetTenantResourcesByTenant
   * @summary 删除租户
   * @request GET:/api/tenant-resource/tenant/{tenantId}
   * @secure
   */
  getTenantResourcesByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultTenantResourceDTO, any>({
      path: `/api/tenant-resource/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Delete tenant
   *
   * @tags api
   * @name GetTenantResourcesByResource
   * @summary 删除租户
   * @request GET:/api/tenant-resource/resource/{resourceId}
   * @secure
   */
  getTenantResourcesByResource = (
    resourceId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultTenantResourceDTO, any>({
      path: `/api/tenant-resource/resource/${resourceId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetTenantConfig
   * @request GET:/api/tenant-config/{id}
   * @secure
   */
  getTenantConfig = (id: number, params: RequestParams = {}) =>
    this.request<TenantConfigDTO, any>({
      path: `/api/tenant-config/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteTenantConfig
   * @request DELETE:/api/tenant-config/{id}
   * @secure
   */
  deleteTenantConfig = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/tenant-config/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetTenantConfigsByTenant
   * @request GET:/api/tenant-config/tenant/{tenantId}
   * @secure
   */
  getTenantConfigsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultTenantConfigDTO, any>({
      path: `/api/tenant-config/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetTag
   * @request GET:/api/tag/{id}
   * @secure
   */
  getTag = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseTagDTO, any>({
      path: `/api/tag/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteTag
   * @request DELETE:/api/tag/{id}
   * @secure
   */
  deleteTag = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/tag/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetTagsByTenant
   * @request GET:/api/tag/tenant/{tenantId}
   * @secure
   */
  getTagsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultTagDTO, any>({
      path: `/api/tag/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetSysParam
   * @request GET:/api/sys-param/{id}
   * @secure
   */
  getSysParam = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseSysParamDTO, any>({
      path: `/api/sys-param/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteSysParam
   * @request DELETE:/api/sys-param/{id}
   * @secure
   */
  deleteSysParam = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/sys-param/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRolesByName
   * @summary 分页查询角色
   * @request GET:/api/role/page
   * @secure
   */
  getRolesByName = (
    query?: {
      name?: string;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRoleDTO, any>({
      path: `/api/role/page`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get all roles
   *
   * @tags api
   * @name GetAllRoles
   * @summary 查询所有角色
   * @request GET:/api/role/list
   * @secure
   */
  getAllRoles = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRoleDTO, any>({
      path: `/api/role/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get role by ID
   *
   * @tags api
   * @name GetRole
   * @summary 根据ID查询角色
   * @request GET:/api/role/id/{id}
   * @secure
   */
  getRole = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseRoleDTO, any>({
      path: `/api/role/id/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Delete role by ID
   *
   * @tags api
   * @name DeleteRole
   * @summary 删除角色
   * @request DELETE:/api/role/id/{id}
   * @secure
   */
  deleteRole = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/role/id/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRolePermission
   * @request GET:/api/role-permission/{id}
   * @secure
   */
  getRolePermission = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseRolePermissionDTO, any>({
      path: `/api/role-permission/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteRolePermission
   * @request DELETE:/api/role-permission/{id}
   * @secure
   */
  deleteRolePermission = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/role-permission/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRolePermissionsByRole
   * @request GET:/api/role-permission/role/{roleId}
   * @secure
   */
  getRolePermissionsByRole = (
    roleId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRolePermissionDTO, any>({
      path: `/api/role-permission/role/${roleId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRolePermissionsByPermission
   * @request GET:/api/role-permission/permission/{permissionId}
   * @secure
   */
  getRolePermissionsByPermission = (
    permissionId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRolePermissionDTO, any>({
      path: `/api/role-permission/permission/${permissionId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRoleMenu
   * @request GET:/api/role-menu/{id}
   * @secure
   */
  getRoleMenu = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseRoleMenuDTO, any>({
      path: `/api/role-menu/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteRoleMenu
   * @request DELETE:/api/role-menu/{id}
   * @secure
   */
  deleteRoleMenu = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/role-menu/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRoleMenusByRole
   * @request GET:/api/role-menu/role/{roleId}
   * @secure
   */
  getRoleMenusByRole = (
    roleId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRoleMenuDTO, any>({
      path: `/api/role-menu/role/${roleId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRoleMenusByMenu
   * @request GET:/api/role-menu/menu/{menuId}
   * @secure
   */
  getRoleMenusByMenu = (
    menuId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRoleMenuDTO, any>({
      path: `/api/role-menu/menu/${menuId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Delete role by ID
   *
   * @tags api
   * @name GetRoleDataScope
   * @summary 删除角色
   * @request GET:/api/role-data-scope/{id}
   * @secure
   */
  getRoleDataScope = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseRoleDataScopeDTO, any>({
      path: `/api/role-data-scope/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Delete role by ID
   *
   * @tags api
   * @name DeleteRoleDataScope
   * @summary 删除角色
   * @request DELETE:/api/role-data-scope/{id}
   * @secure
   */
  deleteRoleDataScope = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/role-data-scope/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRoleDataScopesByRole
   * @request GET:/api/role-data-scope/role/{roleId}
   * @secure
   */
  getRoleDataScopesByRole = (
    roleId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRoleDataScopeDTO, any>({
      path: `/api/role-data-scope/role/${roleId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetRoleDataScopesByDataScope
   * @request GET:/api/role-data-scope/data-scope/{dataScopeId}
   * @secure
   */
  getRoleDataScopesByDataScope = (
    dataScopeId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultRoleDataScopeDTO, any>({
      path: `/api/role-data-scope/data-scope/${dataScopeId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetResource
   * @request GET:/api/resource/{id}
   * @secure
   */
  getResource = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseResourceDTO, any>({
      path: `/api/resource/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteResource
   * @request DELETE:/api/resource/{id}
   * @secure
   */
  deleteResource = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/resource/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetResourcesByTenant
   * @request GET:/api/resource/tenant/{tenantId}
   * @secure
   */
  getResourcesByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultResourceDTO, any>({
      path: `/api/resource/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetResourceLabel
   * @request GET:/api/resource-label/{id}
   * @secure
   */
  getResourceLabel = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseResourceLabelDTO, any>({
      path: `/api/resource-label/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteResourceLabel
   * @request DELETE:/api/resource-label/{id}
   * @secure
   */
  deleteResourceLabel = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/resource-label/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetResourceLabelsByResource
   * @request GET:/api/resource-label/resource/{resourceId}
   * @secure
   */
  getResourceLabelsByResource = (
    resourceId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultResourceLabelDTO, any>({
      path: `/api/resource-label/resource/${resourceId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetResourceLabelsByLabel
   * @request GET:/api/resource-label/label/{labelId}
   * @secure
   */
  getResourceLabelsByLabel = (
    labelId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultResourceLabelDTO, any>({
      path: `/api/resource-label/label/${labelId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetResourceGroup
   * @request GET:/api/resource-group/{id}
   * @secure
   */
  getResourceGroup = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseResourceGroupDTO, any>({
      path: `/api/resource-group/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteResourceGroup
   * @request DELETE:/api/resource-group/{id}
   * @secure
   */
  deleteResourceGroup = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/resource-group/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetResourceGroupsByTenant
   * @request GET:/api/resource-group/tenant/{tenantId}
   * @secure
   */
  getResourceGroupsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultResourceGroupDTO, any>({
      path: `/api/resource-group/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetProfile
   * @summary 根据档案ID获取
   * @request GET:/api/profile/{id}
   * @secure
   */
  getProfile = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseProfileDTO, any>({
      path: `/api/profile/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteProfile
   * @summary 删除用户档案
   * @request DELETE:/api/profile/{id}
   * @secure
   */
  deleteProfile = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/profile/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetProfileByUser
   * @summary 根据用户ID获取
   * @request GET:/api/profile/user/{userId}
   * @secure
   */
  getProfileByUser = (userId: number, params: RequestParams = {}) =>
    this.request<ApiResponseProfileDTO, any>({
      path: `/api/profile/user/${userId}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOrgNodeTree
   * @request GET:/api/org-node/tree
   * @secure
   */
  getOrgNodeTree = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      siz?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseListOrgNodeDTO, any>({
      path: `/api/org-node/tree`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOrgNodesByTenant
   * @request GET:/api/org-node/list
   * @secure
   */
  getOrgNodesByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultOrgNodeDTO, any>({
      path: `/api/org-node/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOrgNode
   * @request GET:/api/org-node/get/{id}
   * @secure
   */
  getOrgNode = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseOrgNodeDTO, any>({
      path: `/api/org-node/get/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOpsTasksByTenant
   * @request GET:/api/ops-task/list
   * @secure
   */
  getOpsTasksByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultOpsTaskDTO, any>({
      path: `/api/ops-task/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOpsTasksByTenantAndStatus
   * @request GET:/api/ops-task/list-by-status
   * @secure
   */
  getOpsTasksByTenantAndStatus = (
    query: {
      /** @format int64 */
      tenantId: number;
      status: string;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultOpsTaskDTO, any>({
      path: `/api/ops-task/list-by-status`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOpsTask
   * @request GET:/api/ops-task/get/{id}
   * @secure
   */
  getOpsTask = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseOpsTaskDTO, any>({
      path: `/api/ops-task/get/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOperationTemplate
   * @request GET:/api/operation-template/{id}
   * @secure
   */
  getOperationTemplate = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseOperationTemplateDTO, any>({
      path: `/api/operation-template/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteOperationTemplate
   * @request DELETE:/api/operation-template/{id}
   * @secure
   */
  deleteOperationTemplate = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/operation-template/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetOperationTemplatesByTenant
   * @request GET:/api/operation-template/tenant/{tenantId}
   * @secure
   */
  getOperationTemplatesByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultOperationTemplateDTO, any>({
      path: `/api/operation-template/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNotifyChannelsByTenant
   * @request GET:/api/notify-channel/list
   * @secure
   */
  getNotifyChannelsByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultNotifyChannelDTO, any>({
      path: `/api/notify-channel/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNotifyChannel
   * @request GET:/api/notify-channel/get/{id}
   * @secure
   */
  getNotifyChannel = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseNotifyChannelDTO, any>({
      path: `/api/notify-channel/get/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNotification
   * @request GET:/api/notification/{id}
   * @secure
   */
  getNotification = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseNotificationDTO, any>({
      path: `/api/notification/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteNotification
   * @request DELETE:/api/notification/{id}
   * @secure
   */
  deleteNotification = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/notification/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNotificationsByUser
   * @request GET:/api/notification/user/{userId}
   * @secure
   */
  getNotificationsByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultNotificationDTO, any>({
      path: `/api/notification/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNotificationsByTenant
   * @request GET:/api/notification/tenant/{tenantId}
   * @secure
   */
  getNotificationsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultNotificationDTO, any>({
      path: `/api/notification/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNode
   * @request GET:/api/node/{id}
   * @secure
   */
  getNode = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseNodeDTO, any>({
      path: `/api/node/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteNode
   * @request DELETE:/api/node/{id}
   * @secure
   */
  deleteNode = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/node/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNodesByCluster
   * @request GET:/api/node/cluster/{clusterId}
   * @secure
   */
  getNodesByCluster = (
    clusterId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultNodeDTO, any>({
      path: `/api/node/cluster/${clusterId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNodeHealth
   * @request GET:/api/node-health/{id}
   * @secure
   */
  getNodeHealth = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseNodeHealthDTO, any>({
      path: `/api/node-health/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteNodeHealth
   * @request DELETE:/api/node-health/{id}
   * @secure
   */
  deleteNodeHealth = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/node-health/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetNodeHealthsByNode
   * @request GET:/api/node-health/node/{nodeId}
   * @secure
   */
  getNodeHealthsByNode = (
    nodeId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultNodeHealthDTO, any>({
      path: `/api/node-health/node/${nodeId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get metric by ID
   *
   * @tags api
   * @name GetMetric
   * @summary 根据ID查询指标
   * @request GET:/api/metric/{id}
   * @secure
   */
  getMetric = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseJobMetricDTO, any>({
      path: `/api/metric/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteMetric
   * @request DELETE:/api/metric/{id}
   * @secure
   */
  deleteMetric = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/metric/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobInstanceStatusCountByTenant
   * @request GET:/api/metric/status-count/{tenantId}
   * @secure
   */
  getJobInstanceStatusCountByTenant = (
    tenantId: number,
    params: RequestParams = {},
  ) =>
    this.request<Record<string, number>, any>({
      path: `/api/metric/status-count/${tenantId}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Get metrics by tenant and metricKey
   *
   * @tags api
   * @name GetMetricsByTenantAndMetricKey
   * @summary 查询租户的某类型指标
   * @request GET:/api/metric/listByTenant
   * @secure
   */
  getMetricsByTenantAndMetricKey = (
    query: {
      /** @format int64 */
      tenantId: number;
      metricKey: string;
      start: string;
      end: string;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobMetricDTO, any>({
      path: `/api/metric/listByTenant`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get metrics by job
   *
   * @tags api
   * @name GetMetricsByJob
   * @summary 查询任务的指标
   * @request GET:/api/metric/listByJob
   * @secure
   */
  getMetricsByJob = (
    query: {
      /** @format int64 */
      jobId: number;
      start: string;
      end: string;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobMetricDTO, any>({
      path: `/api/metric/listByJob`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobInstanceLastJobsByTenant
   * @request GET:/api/metric/last-jobs/{tenantId}
   * @secure
   */
  getJobInstanceLastJobsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobInstanceDTO, any>({
      path: `/api/metric/last-jobs/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetMetricDashboard
   * @request GET:/api/metric-dashboard/{id}
   * @secure
   */
  getMetricDashboard = (id: number, params: RequestParams = {}) =>
    this.request<MetricDashboardDTO, any>({
      path: `/api/metric-dashboard/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteMetricDashboard
   * @request DELETE:/api/metric-dashboard/{id}
   * @secure
   */
  deleteMetricDashboard = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/metric-dashboard/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetMetricDashboardsByTenant
   * @request GET:/api/metric-dashboard/tenant/{tenantId}
   * @secure
   */
  getMetricDashboardsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultMetricDashboardDTO, any>({
      path: `/api/metric-dashboard/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetLoginHistory
   * @request GET:/api/login-history/{id}
   * @secure
   */
  getLoginHistory = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseLoginHistoryDTO, any>({
      path: `/api/login-history/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteLoginHistory
   * @request DELETE:/api/login-history/{id}
   * @secure
   */
  deleteLoginHistory = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/login-history/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetLoginHistorysByUser
   * @request GET:/api/login-history/user/{userId}
   * @secure
   */
  getLoginHistorysByUser = (
    userId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultLoginHistoryDTO, any>({
      path: `/api/login-history/user/${userId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetSsoLoginHistorysByTenant
   * @summary 按租户查询登录历史
   * @request GET:/api/login-history/tenant
   * @secure
   */
  getSsoLoginHistorysByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultLoginHistoryDTO, any>({
      path: `/api/login-history/tenant`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name CountSuccessLoginByUser
   * @summary 统计用户成功登录次数
   * @request GET:/api/login-history/count-success
   * @secure
   */
  countSuccessLoginByUser = (
    query: {
      /** @format int64 */
      userId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseLong, any>({
      path: `/api/login-history/count-success`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetLabel
   * @request GET:/api/label/{id}
   * @secure
   */
  getLabel = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseLabelDTO, any>({
      path: `/api/label/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteLabel
   * @request DELETE:/api/label/{id}
   * @secure
   */
  deleteLabel = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/label/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetLabelsByTenant
   * @request GET:/api/label/tenant/{tenantId}
   * @secure
   */
  getLabelsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultLabelDTO, any>({
      path: `/api/label/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get log by ID
   *
   * @tags api
   * @name GetJobLog
   * @summary 根据ID查询日志
   * @request GET:/api/joblog/{id}
   * @secure
   */
  getJobLog = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseJobLogDTO, any>({
      path: `/api/joblog/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Soft delete job log
   *
   * @tags api
   * @name DeleteJobLog
   * @summary 删除日志（软删）
   * @request DELETE:/api/joblog/{id}
   * @secure
   */
  deleteJobLog = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/joblog/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * @description Get logs by tenant and level
   *
   * @tags api
   * @name GetJobLogsByTenantAndLevel
   * @summary 按级别查询租户日志
   * @request GET:/api/joblog/listByTenantLevel
   * @secure
   */
  getJobLogsByTenantAndLevel = (
    query: {
      /** @format int64 */
      tenantId: number;
      level: string;
      start: string;
      end: string;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobLogDTO, any>({
      path: `/api/joblog/listByTenantLevel`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get logs by job
   *
   * @tags api
   * @name GetJobLogsByJob
   * @summary 查询任务日志
   * @request GET:/api/joblog/listByJob
   * @secure
   */
  getJobLogsByJob = (
    query: {
      /** @format int64 */
      jobId: number;
      start: string;
      end: string;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobLogDTO, any>({
      path: `/api/joblog/listByJob`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllJobs
   * @request GET:/api/job
   * @secure
   */
  getAllJobs = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobDTO, any>({
      path: `/api/job`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get job by ID
   *
   * @tags api
   * @name GetJob
   * @summary 根据ID查询任务
   * @request GET:/api/job/{id}
   * @secure
   */
  getJob = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseJobDTO, any>({
      path: `/api/job/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Soft delete job
   *
   * @tags api
   * @name DeleteJob
   * @summary 删除任务（软删）
   * @request DELETE:/api/job/{id}
   * @secure
   */
  deleteJob = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/job/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * @description Get jobs by tenant
   *
   * @tags api
   * @name GetJobsByTenant
   * @summary 查询租户下所有任务
   * @request GET:/api/job/list
   * @secure
   */
  getJobsByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobDTO, any>({
      path: `/api/job/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get jobs by tenant and cluster
   *
   * @tags api
   * @name GetJobsByTenantAndCluster
   * @summary 查询集群下所有任务
   * @request GET:/api/job/listByCluster
   * @secure
   */
  getJobsByTenantAndCluster = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format int64 */
      clusterId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobDTO, any>({
      path: `/api/job/listByCluster`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Job漏斗统计
   *
   * @tags api
   * @name GetJobFunnelsByTenant
   * @summary Job漏斗统计
   * @request GET:/api/job-statistics/funnel
   * @secure
   */
  getJobFunnelsByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 10
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobFunnelDTO, any>({
      path: `/api/job-statistics/funnel`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobPermissionsByJobAndUser
   * @request GET:/api/job-permission/user
   * @secure
   */
  getJobPermissionsByJobAndUser = (
    query: {
      /** @format int64 */
      jobId: number;
      userId: string;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobPermissionDTO, any>({
      path: `/api/job-permission/user`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobPermissionsByJob
   * @request GET:/api/job-permission/list
   * @secure
   */
  getJobPermissionsByJob = (
    query: {
      /** @format int64 */
      jobId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobPermissionDTO, any>({
      path: `/api/job-permission/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobInstance
   * @request GET:/api/job-instance/{id}
   * @secure
   */
  getJobInstance = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseJobInstanceDTO, any>({
      path: `/api/job-instance/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteJobInstance
   * @request DELETE:/api/job-instance/{id}
   * @secure
   */
  deleteJobInstance = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/job-instance/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobInstancesByTenant
   * @request GET:/api/job-instance/tenant/{tenantId}
   * @secure
   */
  getJobInstancesByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobInstanceDTO, any>({
      path: `/api/job-instance/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobInstancesByStatus
   * @request GET:/api/job-instance/status/{status}
   * @secure
   */
  getJobInstancesByStatus = (
    status: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobInstanceDTO, any>({
      path: `/api/job-instance/status/${status}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobDiagnosticLogsByJob
   * @request GET:/api/job-diagnostic/logs/{jobId}
   * @secure
   */
  getJobDiagnosticLogsByJob = (
    jobId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobDiagnosticLogDTO, any>({
      path: `/api/job-diagnostic/logs/${jobId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetJobDiagnosticLogsByLevel
   * @request GET:/api/job-diagnostic/logs-level/{level}
   * @secure
   */
  getJobDiagnosticLogsByLevel = (
    level: string,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobDiagnosticLogDTO, any>({
      path: `/api/job-diagnostic/logs-level/${level}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetIntegrationConfig
   * @request GET:/api/integration-config/{id}
   * @secure
   */
  getIntegrationConfig = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseIntegrationConfigDTO, any>({
      path: `/api/integration-config/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteIntegrationConfig
   * @request DELETE:/api/integration-config/{id}
   * @secure
   */
  deleteIntegrationConfig = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/integration-config/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetIntegrationConfigsByTenant
   * @request GET:/api/integration-config/tenant/{tenantId}
   * @secure
   */
  getIntegrationConfigsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultIntegrationConfigDTO, any>({
      path: `/api/integration-config/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetGroupRole
   * @request GET:/api/group-role/{id}
   * @secure
   */
  getGroupRole = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseGroupRoleDTO, any>({
      path: `/api/group-role/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteGroupRole
   * @request DELETE:/api/group-role/{id}
   * @secure
   */
  deleteGroupRole = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/group-role/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetGroupRolesByRole
   * @request GET:/api/group-role/role/{roleId}
   * @secure
   */
  getGroupRolesByRole = (
    roleId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultGroupRoleDTO, any>({
      path: `/api/group-role/role/${roleId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetGroupRolesByGroup
   * @request GET:/api/group-role/group/{groupId}
   * @secure
   */
  getGroupRolesByGroup = (
    groupId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultGroupRoleDTO, any>({
      path: `/api/group-role/group/${groupId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetFile
   * @request GET:/api/file/{id}
   * @secure
   */
  getFile = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseFileDTO, any>({
      path: `/api/file/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteFile
   * @request DELETE:/api/file/{id}
   * @secure
   */
  deleteFile = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/file/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetFilesByTenant
   * @request GET:/api/file/tenant/{tenantId}
   * @secure
   */
  getFilesByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultFileDTO, any>({
      path: `/api/file/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetDict
   * @request GET:/api/dict/{id}
   * @secure
   */
  getDict = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseDictDTO, any>({
      path: `/api/dict/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteDict
   * @request DELETE:/api/dict/{id}
   * @secure
   */
  deleteDict = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/dict/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetDictsByDictType
   * @request GET:/api/dict/type/{dictType}
   * @secure
   */
  getDictsByDictType = (
    dictType: string,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultDictDTO, any>({
      path: `/api/dict/type/${dictType}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetDeptRole
   * @request GET:/api/dept-role/{id}
   * @secure
   */
  getDeptRole = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseDeptRoleDTO, any>({
      path: `/api/dept-role/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteDeptRole
   * @request DELETE:/api/dept-role/{id}
   * @secure
   */
  deleteDeptRole = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/dept-role/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetDeptRolesByRole
   * @request GET:/api/dept-role/role/{roleId}
   * @secure
   */
  getDeptRolesByRole = (
    roleId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultDeptRoleDTO, any>({
      path: `/api/dept-role/role/${roleId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetDeptRolesByDept
   * @request GET:/api/dept-role/dept/{deptId}
   * @secure
   */
  getDeptRolesByDept = (
    deptId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultDeptRoleDTO, any>({
      path: `/api/dept-role/dept/${deptId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get clusters by tenant
   *
   * @tags api
   * @name GetDataSource
   * @request GET:/api/data-source/{id}
   * @secure
   */
  getDataSource = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseDataSourceDTO, any>({
      path: `/api/data-source/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Get clusters by tenant
   *
   * @tags api
   * @name DeleteDataSource
   * @request DELETE:/api/data-source/{id}
   * @secure
   */
  deleteDataSource = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/data-source/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * @description Get clusters by tenant
   *
   * @tags api
   * @name GetDataSourcesByTenant
   * @request GET:/api/data-source/tenant/{tenantId}
   * @secure
   */
  getDataSourcesByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultDataSourceDTO, any>({
      path: `/api/data-source/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description 大盘核心统计信息
   *
   * @tags api
   * @name DashboardKpiStatisticsSummary
   * @summary 大盘核心指标
   * @request GET:/api/dashboard/summary
   * @secure
   */
  dashboardKpiStatisticsSummary = (
    query?: {
      /** @format int64 */
      tenantId?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseKPIStatusSummaryDTO, any>({
      path: `/api/dashboard/summary`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DashboardStatisticsAlertCountByStatus
   * @request GET:/api/dashboard/status
   * @secure
   */
  dashboardStatisticsAlertCountByStatus = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format date-time */
      start: string;
      /** @format date-time */
      end: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<object, any>({
      path: `/api/dashboard/status`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DashboardStatisticsAlertCountByLevel
   * @summary 大屏监控指标卡
   * @request GET:/api/dashboard/severity
   * @secure
   */
  dashboardStatisticsAlertCountByLevel = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format date-time */
      start: string;
      /** @format date-time */
      end: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<object, any>({
      path: `/api/dashboard/severity`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DashboardStatisticsResponseSeconds
   * @request GET:/api/dashboard/response-time
   * @secure
   */
  dashboardStatisticsResponseSeconds = (
    query: {
      /** @format int64 */
      tenantId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<object, any>({
      path: `/api/dashboard/response-time`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DashboardStatisticsMetricSeries
   * @summary 指标曲线
   * @request GET:/api/dashboard/metrics/series
   * @secure
   */
  dashboardStatisticsMetricSeries = (
    query: {
      /** @format int64 */
      tenantId?: number;
      metric: string;
      from: string;
      to: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseMetricSeriesDTO, any>({
      path: `/api/dashboard/metrics/series`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DashboardStatisticsFailedJobAlertTrend
   * @request GET:/api/dashboard/job-fail-trend
   * @secure
   */
  dashboardStatisticsFailedJobAlertTrend = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format date-time */
      start: string;
      /** @format date-time */
      end: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<object, any>({
      path: `/api/dashboard/job-fail-trend`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description 集群健康状态分布信息
   *
   * @tags api
   * @name DashboardStatisticsHealth
   * @summary 集群健康分布
   * @request GET:/api/dashboard/health
   * @secure
   */
  dashboardStatisticsHealth = (
    query?: {
      /** @format int64 */
      tenantId?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseHealthDistributionDTO, any>({
      path: `/api/dashboard/health`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description 各阶段统计
   *
   * @tags api
   * @name DashboardStatisticsJobFunnels
   * @summary 业务转化漏斗
   * @request GET:/api/dashboard/funnel
   * @secure
   */
  dashboardStatisticsJobFunnels = (
    query?: {
      /** @format int64 */
      tenantId?: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultJobFunnelDTO, any>({
      path: `/api/dashboard/funnel`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DashboardClusterTrend
   * @request GET:/api/dashboard/cluster/trend
   * @secure
   */
  dashboardClusterTrend = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format date-time */
      from: string;
      /** @format date-time */
      to: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseClusterTrendDTO, any>({
      path: `/api/dashboard/cluster/trend`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DashboardStatisticsClusterHealthMetrics
   * @summary 获取集群健康统计指标
   * @request GET:/api/dashboard/cluster-health
   * @secure
   */
  dashboardStatisticsClusterHealthMetrics = (
    query: {
      /** @format int64 */
      tenantId: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseClusterHealthMetricsDTO, any>({
      path: `/api/dashboard/cluster-health`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description 返回 times/total/fatal/warn
   *
   * @tags api
   * @name DashboardStatisticsAlertTrend
   * @summary 告警趋势
   * @request GET:/api/dashboard/alert-trend
   * @secure
   */
  dashboardStatisticsAlertTrend = (
    query?: {
      /** @format int64 */
      tenantId?: number;
      /** @format date */
      from?: string;
      /** @format date */
      to?: string;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseAlertTrendDTO, any>({
      path: `/api/dashboard/alert-trend`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAllClusters
   * @request GET:/api/cluster
   * @secure
   */
  getAllClusters = (
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultClusterDTO, any>({
      path: `/api/cluster`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get cluster by ID
   *
   * @tags api
   * @name GetCluster
   * @summary 根据ID查询集群
   * @request GET:/api/cluster/{id}
   * @secure
   */
  getCluster = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseClusterDTO, any>({
      path: `/api/cluster/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Soft delete cluster
   *
   * @tags api
   * @name DeleteCluster
   * @summary 删除集群（软删）
   * @request DELETE:/api/cluster/{id}
   * @secure
   */
  deleteCluster = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/cluster/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * @description Get clusters by tenant
   *
   * @tags api
   * @name GetClustersByTenant
   * @summary 查询租户下所有集群
   * @request GET:/api/cluster/list
   * @secure
   */
  getClustersByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultClusterDTO, any>({
      path: `/api/cluster/list`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAuditLog
   * @request GET:/api/audit-log/{id}
   * @secure
   */
  getAuditLog = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseAuditLogDTO, any>({
      path: `/api/audit-log/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteAuditLog
   * @request DELETE:/api/audit-log/{id}
   * @secure
   */
  deleteAuditLog = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/audit-log/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAuditLogsByTenant
   * @request GET:/api/audit-log/tenant/{tenantId}
   * @secure
   */
  getAuditLogsByTenant = (
    tenantId: number,
    query: {
      /** @format int64 */
      userId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAuditLogDTO, any>({
      path: `/api/audit-log/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetApiWhitelist
   * @summary 分页获取所有报警历史记录
   * @request GET:/api/api-whitelist/{id}
   * @secure
   */
  getApiWhitelist = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseApiWhitelistDTO, any>({
      path: `/api/api-whitelist/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteApiWhitelist
   * @summary 分页获取所有报警历史记录
   * @request DELETE:/api/api-whitelist/{id}
   * @secure
   */
  deleteApiWhitelist = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/api-whitelist/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetApiKey
   * @request GET:/api/api-key/{id}
   * @secure
   */
  getApiKey = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseApiKeyDTO, any>({
      path: `/api/api-key/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteApiKey
   * @request DELETE:/api/api-key/{id}
   * @secure
   */
  deleteApiKey = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/api-key/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetApiKeysByTenant
   * @request GET:/api/api-key/tenant/{tenantId}
   * @secure
   */
  getApiKeysByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultApiKeyDTO, any>({
      path: `/api/api-key/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetApiAccessLog
   * @request GET:/api/api-access-log/{id}
   * @secure
   */
  getApiAccessLog = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseApiAccessLogDTO, any>({
      path: `/api/api-access-log/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteApiAccessLog
   * @request DELETE:/api/api-access-log/{id}
   * @secure
   */
  deleteApiAccessLog = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/api-access-log/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetApiAccessLogsByTenant
   * @request GET:/api/api-access-log/tenant/{tenantId}
   * @secure
   */
  getApiAccessLogsByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultApiAccessLogDTO, any>({
      path: `/api/api-access-log/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAlert
   * @summary 根据ID查询报警事件
   * @request GET:/api/alert/{id}
   * @secure
   */
  getAlert = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseAlertDTO, any>({
      path: `/api/alert/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * @description Soft delete alert
   *
   * @tags api
   * @name DeleteAlert
   * @summary 删除报警事件（软删）
   * @request DELETE:/api/alert/{id}
   * @secure
   */
  deleteAlert = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseVoid, any>({
      path: `/api/alert/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * @description Get alerts by tenant and status
   *
   * @tags api
   * @name GetAlertsByTenantAndStatus
   * @summary 查询租户下报警事件
   * @request GET:/api/alert/listByTenantAndStatus
   * @secure
   */
  getAlertsByTenantAndStatus = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format int32 */
      status: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAlertDTO, any>({
      path: `/api/alert/listByTenantAndStatus`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get alerts by job and status
   *
   * @tags api
   * @name GetAlertsByLevelAndStatus
   * @summary 查询任务下报警事件
   * @request GET:/api/alert/listByLevelAndStatus
   * @secure
   */
  getAlertsByLevelAndStatus = (
    query: {
      level: string;
      /** @format int32 */
      status: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAlertDTO, any>({
      path: `/api/alert/listByLevelAndStatus`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Get alerts by job and status
   *
   * @tags api
   * @name GetAlertsByJobAndStatus
   * @summary 查询任务下报警事件
   * @request GET:/api/alert/listByJobAndStatus
   * @secure
   */
  getAlertsByJobAndStatus = (
    query: {
      /** @format int64 */
      jobId: number;
      /** @format int32 */
      status: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAlertDTO, any>({
      path: `/api/alert/listByJobAndStatus`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAlertRulesByTenant
   * @summary 获取租户下所有报警规则
   * @request GET:/api/alert-rule/listByTenant
   * @secure
   */
  getAlertRulesByTenant = (
    query: {
      /** @format int64 */
      tenantId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAlertRuleDTO, any>({
      path: `/api/alert-rule/listByTenant`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAlertRulesByTenantAndCluster
   * @summary 获取租户集群下所有报警规则
   * @request GET:/api/alert-rule/listByTenantAndCluster
   * @secure
   */
  getAlertRulesByTenantAndCluster = (
    query: {
      /** @format int64 */
      tenantId: number;
      /** @format int64 */
      clusterId: number;
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAlertRuleDTO, any>({
      path: `/api/alert-rule/listByTenantAndCluster`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAlertRuleById
   * @summary 获取报警规则详情
   * @request GET:/api/alert-rule/get
   * @secure
   */
  getAlertRuleById = (
    query: {
      /** @format int64 */
      id: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponseAlertRuleDTO, any>({
      path: `/api/alert-rule/get`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
  /**
   * @description Update alert info
   *
   * @tags api
   * @name GetAlertHistory
   * @summary 获取报警历史事件
   * @request GET:/api/alert-history/{id}
   * @secure
   */
  getAlertHistory = (id: number, params: RequestParams = {}) =>
    this.request<ApiResponseAlertHistoryDTO, any>({
      path: `/api/alert-history/${id}`,
      method: "GET",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name DeleteAlertHistory
   * @summary 更新报警历史记录
   * @request DELETE:/api/alert-history/{id}
   * @secure
   */
  deleteAlertHistory = (id: number, params: RequestParams = {}) =>
    this.request<boolean, any>({
      path: `/api/alert-history/${id}`,
      method: "DELETE",
      secure: true,
      ...params,
    });
  /**
   * No description
   *
   * @tags api
   * @name GetAlertHistorysByTenant
   * @summary 分页获取所有报警历史记录
   * @request GET:/api/alert-history/tenant/{tenantId}
   * @secure
   */
  getAlertHistorysByTenant = (
    tenantId: number,
    query?: {
      /**
       * @format int32
       * @default 0
       */
      page?: number;
      /**
       * @format int32
       * @default 20
       */
      size?: number;
    },
    params: RequestParams = {},
  ) =>
    this.request<ApiResponsePageResultAlertHistoryDTO, any>({
      path: `/api/alert-history/tenant/${tenantId}`,
      method: "GET",
      query: query,
      secure: true,
      ...params,
    });
}
