// /src/types/alert.ts 覆盖报警流、报警规则、通知对象等所有类型，参数与返回类型全部覆盖

/**
 * 报警级别
 */
export type AlertLevel = 'fatal' | 'high' | 'medium' | 'low';

/**
 * 报警状态
 */
export type AlertStatus = 0 | 1 | 2; // 0未处理, 1处理中, 2已关闭

/**
 * 报警事件
 */
export interface Alert {
  id: number;
  tenantId?: number;
  jobId: number;
  level: AlertLevel;
  type: string; // 报警类型，如"资源", "异常"
  message: string;
  status: AlertStatus;
  handlerId?: number;
  isDeleted?: 0 | 1;
  createTime: string;
  updateTime: string;
}

/**
 * 报警查询参数
 */
export interface AlertQuery {
  level?: AlertLevel;
  status?: AlertStatus;
  jobId?: number;
  type?: string;
  from?: string;
  to?: string;
  page?: number;
  size?: number;
}

/**
 * 新建报警（一般由系统自动，留接口供特殊场景）
 */
export interface AlertCreateReq {
  jobId: number;
  level: AlertLevel;
  type: string;
  message: string;
}

/**
 * 更新报警（如处理、关闭、备注等）
 */
export interface AlertUpdateReq {
  status?: AlertStatus;
  handlerId?: number;
  message?: string;
  note?: string;
}

/**
 * 批量操作参数
 */
export interface AlertBatchOpReq {
  ids: number[];
  op: 'close' | 'resolve' | 'assign';
  handlerId?: number;
}

/**
 * 报警规则
 */
export interface AlertRule {
  id: number;
  tenantId?: number;
  clusterId: number;
  metricKey: string;
  threshold: number;
  compareOp: '>' | '<' | '=' | '!=' | '>=' | '<=';
  channel: string; // 通知方式
  enable: 0 | 1;
  isDeleted?: 0 | 1;
  createTime: string;
}

/**
 * 新建/编辑报警规则请求
 */
export interface AlertRuleReq {
  clusterId: number;
  metricKey: string;
  threshold: number;
  compareOp: '>' | '<' | '=' | '!=' | '>=' | '<=';
  channel: string;
  enable?: 0 | 1;
}

/**
 * 通知对象（推送给谁）
 */
export interface AlertNotifier {
  id: number;
  ruleId: number;
  type: 'user' | 'role' | 'webhook' | 'email';
  target: string; // 用户ID/邮箱/URL等
}

/**
 * 报警处理记录/工单
 */
export interface AlertTicket {
  id: number;
  alertId: number;
  handlerId: number;
  status: AlertStatus;
  note?: string;
  updateTime: string;
}
