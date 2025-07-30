/**
 * 文件实体
 */
export interface FileItem {
  id: number;
  name: string;
  type: string;
  size: number;
  url: string;
  uploader: string;
  uploadTime: string;
}
