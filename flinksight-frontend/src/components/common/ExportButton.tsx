/**
 * @file 报表/表格导出组件
 * @desc 通用CSV/XLSX导出、数据批量下载，支持API导出或前端导出
 */
import React from 'react';
import {Button} from 'antd';
import {DownloadOutlined} from '@ant-design/icons';

interface ExportButtonProps {
  api: string; // 导出API地址
  params?: Record<string, any>;
  fileName?: string;
  children?: React.ReactNode;
}

const ExportButton: React.FC<ExportButtonProps> = ({ api, params, fileName = 'export.xlsx', children }) => {
  const handleExport = () => {
    const query = params
      ? '?' + Object.entries(params).map(([k, v]) => `${k}=${encodeURIComponent(v)}`).join('&')
      : '';
    window.open(`${api}${query}`, '_blank');
  };
  return (
    <Button icon={<DownloadOutlined />} onClick={handleExport}>
      {children || '导出'}
    </Button>
  );
};

export default ExportButton;
