/**
 * @file 通知渠道类型定义
 * @desc 所有通知类型、参数、表单、API严格类型校验
 */

export interface Notification {
  id: number;
  name: string;           // 渠道名称，如“企业微信群告警”
  type: 'email' | 'sms' | 'webhook' | 'feishu' | 'dingtalk' | string;
  endpoint: string;       // 通知目标（邮箱/URL/手机号等）
  enabled: boolean;       // 是否启用
  config?: Record<string, any>; // 额外配置参数（如模板、秘钥）
  remark?: string;        // 备注
  createTime: string;
  updateTime: string;
}

export interface NotificationCreateReq {
  name: string;
  type: string;
  endpoint: string;
  enabled?: boolean;
  config?: Record<string, any>;
  remark?: string;
}

export interface NotificationUpdateReq extends Partial<NotificationCreateReq> {}

export interface NotificationQuery {
  type?: string;
  keyword?: string;
  page?: number;
  size?: number;
}
