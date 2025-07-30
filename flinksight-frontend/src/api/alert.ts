// /src/api/alert.ts 覆盖报警流、报警规则、通知对象等所有类型，参数与返回类型全部覆盖
import http from './http';
import type {
  Alert,
  AlertQuery,
  AlertCreateReq,
  AlertUpdateReq,
  AlertBatchOpReq,
  AlertRule,
  AlertRuleReq,
  AlertNotifier,
  AlertTicket,
} from '../types/alert';

/** 查询报警事件列表 */
export const getAlerts = (params: AlertQuery) =>
  http.get<Alert[]>('/alerts', { params });

/** 查询单个报警详情 */
export const getAlertDetail = (id: number) =>
  http.get<Alert>(`/alerts/${id}`);

/** 新建报警事件（一般由系统自动，留接口供特殊场景） */
export const createAlert = (data: AlertCreateReq) =>
  http.post<Alert>('/alerts', data);

/** 修改报警（如处理、备注、关闭等） */
export const updateAlert = (id: number, data: AlertUpdateReq) =>
  http.put<Alert>(`/alerts/${id}`, data);

/** 删除报警 */
export const deleteAlert = (id: number) =>
  http.delete(`/alerts/${id}`);

/** 批量删除报警 */
export const deleteAlerts = (ids: number[]) =>
  http.post('/alerts/batch-delete', { ids });

/** 批量操作报警（关闭/分派/批量处理等） */
export const batchOperateAlerts = (data: AlertBatchOpReq) =>
  http.post('/alerts/batch-op', data);

/** 获取报警工单/处理流转记录 */
export const getAlertTickets = (alertId: number) =>
  http.get<AlertTicket[]>(`/alerts/${alertId}/tickets`);

/** 处理/关闭报警工单 */
export const resolveAlert = (alertId: number, data: AlertUpdateReq) =>
  http.post(`/alerts/${alertId}/resolve`, data);

/** 查询报警规则列表 */
export const getAlertRules = (params?: { clusterId?: number }) =>
  http.get<AlertRule[]>('/alert_rules', { params });

/** 查询单个报警规则详情 */
export const getAlertRuleDetail = (id: number) =>
  http.get<AlertRule>(`/alert_rules/${id}`);

/** 新建报警规则 */
export const createAlertRule = (data: AlertRuleReq) =>
  http.post<AlertRule>('/alert_rules', data);

/** 修改报警规则 */
export const updateAlertRule = (id: number, data: Partial<AlertRuleReq>) =>
  http.put<AlertRule>(`/alert_rules/${id}`, data);

/** 删除报警规则 */
export const deleteAlertRule = (id: number) =>
  http.delete(`/alert_rules/${id}`);

/** 获取报警规则通知对象列表 */
export const getAlertNotifiers = (ruleId: number) =>
  http.get<AlertNotifier[]>(`/alert_rules/${ruleId}/notifiers`);

/** 新增报警通知对象 */
export const addAlertNotifier = (ruleId: number, data: Omit<AlertNotifier, 'id' | 'ruleId'>) =>
  http.post<AlertNotifier>(`/alert_rules/${ruleId}/notifiers`, data);

/** 删除报警通知对象 */
export const deleteAlertNotifier = (notifierId: number) =>
  http.delete(`/alert_notifiers/${notifierId}`);

/** 查询报警统计数据（按天/级别等） */
export const getAlertStats = (params: { from: string; to: string; level?: string }) =>
  http.get('/alerts/stats', { params });

/** WebSocket报警实时推送地址（需在组件内使用 ws） */
export const ALERT_WS_URL = '/ws/alerts';

