import http from './http';
import type { FileMeta } from '../types/file';

/**
 * 查询文件列表
 */
export const getFiles = () => http.get<FileMeta[]>('/files');

/**
 * 上传文件
 */
export const uploadFile = (formData: FormData) =>
  http.post('/files/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } });

/**
 * 删除文件
 */
export const deleteFile = (id: number) => http.delete(`/files/${id}`);

/**
 * 批量删除文件
 */
export const deleteFiles = (ids: number[]) => http.post('/files/batch-delete', { ids });
