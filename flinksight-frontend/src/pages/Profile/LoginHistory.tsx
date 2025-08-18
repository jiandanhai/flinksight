import React, {useEffect, useState} from 'react';
import api from '@/api/api-compat';

import type {LoginLogDTO} from '@/api/dto';
import {Table} from 'antd';

/**
 * 登录日志/安全日志
 */
const LoginHistory: React.FC = () => {
  const [logs, setLogs] = useState<LoginLogDTO[]>([]);
  useEffect(() => {
    api.getLoginLogs().then(res => setLogs(res.data || []));
  }, []);
  return (
    <Table<LoginLog>
      rowKey="id"
      dataSource={logs}
      columns={[
        { title: '时间', dataIndex: 'time' },
        { title: 'IP', dataIndex: 'ip' },
        { title: '设备', dataIndex: 'device' },
        { title: '状态', dataIndex: 'status', render: v => v === 'success' ? '成功' : '失败' }
      ]}
      pagination={false}
    />
  );
};
export default LoginHistory;
