// /src/types/settings.ts 系统参数、通知通道、租户等全部类型，带详细注释

/**
 * 通知通道类型
 */
export type NotificationChannelType = 'email' | 'sms' | 'dingding' | 'wechat' | 'webhook';

/**
 * 通知通道配置
 */
export interface NotificationChannel {
  id: number;
  tenantId?: number;
  name: string;
  type: NotificationChannelType;
  config: Record<string, any>; // 具体配置信息，如webhook url、邮箱地址等
  enable: 0 | 1;
  remark?: string;
  createTime: string;
}

/**
 * 新建/编辑通知通道请求
 */
export interface NotificationChannelReq {
  name: string;
  type: NotificationChannelType;
  config: Record<string, any>;
  enable?: 0 | 1;
  remark?: string;
}

/**
 * 系统参数项
 */
export interface SystemSetting {
  key: string;
  value: string;
  desc?: string;
  group?: string;
}

/**
 * 系统参数修改请求
 */
export interface SystemSettingUpdateReq {
  key: string;
  value: string;
}

/**
 * 租户信息
 */
export interface Tenant {
  id: number;
  name: string;
  code: string;
  contact?: string;
  status: 1 | 0;
  createTime: string;
}

/**
 * 新建/编辑租户请求
 */
export interface TenantReq {
  name: string;
  code: string;
  contact?: string;
  status?: 1 | 0;
}

/**
 * 告警通知对象配置
 */
export interface AlertNotifyTarget {
  id: number;
  channelId: number;
  type: 'user' | 'role' | 'webhook' | 'email';
  target: string; // 目标用户ID/邮箱/URL等
}
