import http from './http';
import type { Metric, MetricQuery, MetricCreateReq, MetricUpdateReq } from '../types/metric';

/**
 * 查询指标列表
 */
export const getMetrics = (params: MetricQuery) => http.get<Metric[]>('/metrics', { params });

/**
 * 获取指标详情
 */
export const getMetricDetail = (id: number) => http.get<Metric>(`/metrics/${id}`);

/**
 * 新建指标
 */
export const createMetric = (data: MetricCreateReq) => http.post('/metrics', data);

/**
 * 更新指标
 */
export const updateMetric = (id: number, data: MetricUpdateReq) => http.put(`/metrics/${id}`, data);

/**
 * 删除指标
 */
export const deleteMetric = (id: number) => http.delete(`/metrics/${id}`);

/**
 * 批量删除指标
 */
export const deleteMetrics = (ids: number[]) => http.post('/metrics/batch-delete', { ids });
