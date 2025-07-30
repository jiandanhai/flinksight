// /src/api/settings.ts
import http from './http';
import type {
  NotificationChannel,
  NotificationChannelReq,
  SystemSetting,
  SystemSettingUpdateReq,
  Tenant,
  TenantReq,
  AlertNotifyTarget,
} from '../types/settings';

/** 查询系统参数列表 */
export const getSystemSettings = (group?: string) =>
  http.get<SystemSetting[]>('/settings', { params: { group } });

/** 修改系统参数 */
export const updateSystemSetting = (data: SystemSettingUpdateReq) =>
  http.post('/settings/update', data);

/** 查询通知通道列表 */
export const getNotificationChannels = (params?: { tenantId?: number }) =>
  http.get<NotificationChannel[]>('/notification_channels', { params });

/** 查询单个通知通道详情 */
export const getNotificationChannelDetail = (id: number) =>
  http.get<NotificationChannel>(`/notification_channels/${id}`);

/** 新建通知通道 */
export const createNotificationChannel = (data: NotificationChannelReq) =>
  http.post<NotificationChannel>('/notification_channels', data);

/** 编辑通知通道 */
export const updateNotificationChannel = (id: number, data: NotificationChannelReq) =>
  http.put<NotificationChannel>(`/notification_channels/${id}`, data);

/** 删除通知通道 */
export const deleteNotificationChannel = (id: number) =>
  http.delete(`/notification_channels/${id}`);

/** 查询租户列表 */
export const getTenants = () =>
  http.get<Tenant[]>('/tenants');

/** 查询单个租户详情 */
export const getTenantDetail = (id: number) =>
  http.get<Tenant>(`/tenants/${id}`);

/** 新建租户 */
export const createTenant = (data: TenantReq) =>
  http.post<Tenant>('/tenants', data);

/** 编辑租户 */
export const updateTenant = (id: number, data: TenantReq) =>
  http.put<Tenant>(`/tenants/${id}`, data);

/** 删除租户 */
export const deleteTenant = (id: number) =>
  http.delete(`/tenants/${id}`);

/** 查询告警通知目标对象列表 */
export const getAlertNotifyTargets = (channelId: number) =>
  http.get<AlertNotifyTarget[]>(`/notification_channels/${channelId}/targets`);

/** 新增告警通知对象 */
export const createAlertNotifyTarget = (channelId: number, data: Omit<AlertNotifyTarget, 'id' | 'channelId'>) =>
  http.post<AlertNotifyTarget>(`/notification_channels/${channelId}/targets`, data);

/** 删除告警通知对象 */
export const deleteAlertNotifyTarget = (targetId: number) =>
  http.delete(`/notification_targets/${targetId}`);

