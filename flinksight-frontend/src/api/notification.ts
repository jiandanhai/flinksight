/**
 * @file 通知渠道API实现
 * @desc 所有接口均严格对接类型、返回Promise，禁止any
 */
import http from './http';
import type {
  Notification,
  NotificationCreateReq,
  NotificationUpdateReq,
  NotificationQuery
} from '../types/notification';

/** 查询所有通知渠道（可带类型/关键字分页） */
export const getNotifications = (params?: NotificationQuery) =>
  http.get<{ records: Notification[]; total: number }>('/notification', { params });

/** 创建通知渠道 */
export const createNotification = (data: NotificationCreateReq) =>
  http.post('/notification', data);

/** 更新通知渠道 */
export const updateNotification = (id: number, data: NotificationUpdateReq) =>
  http.put(`/notification/${id}`, data);

/** 删除单个通知渠道 */
export const deleteNotification = (id: number) =>
  http.delete(`/notification/${id}`);

/** 批量删除 */
export const deleteNotifications = (ids: number[]) =>
  http.post('/notification/batch-delete', { ids });

/** 获取单个通知详情 */
export const getNotificationDetail = (id: number) =>
  http.get<Notification>(`/notification/${id}`);
