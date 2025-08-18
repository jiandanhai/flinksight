// src/components/Alert/AlertStatusTag.tsx
import React from 'react';
import {Tag} from 'antd';
import {AlertStatusDTO} from '@/api/dto';

/**
 * 报警状态标签，根据不同状态渲染不同颜色
 */
const AlertStatusTag: React.FC<{ status: DTO.AlertStatusDTO }> = ({ status }) => {
  const statusMap = {
    OPEN: { color: 'red', text: '未处理' },
    PROCESSING: { color: 'orange', text: '处理中' },
    CLOSED: { color: 'green', text: '已关闭' },
    ARCHIVED: { color: 'blue', text: '已归档' },
  };
  return <Tag color={statusMap[status]?.color || 'default'}>{statusMap[status]?.text || status}</Tag>;
};
export default AlertStatusTag;