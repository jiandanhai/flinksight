/**
 * @file 系统审计日志
 * @desc 记录所有用户操作，支持搜索、分页、导出
 */
import React, {useEffect, useState} from 'react';
import {Button, Input, Table, Tag} from 'antd';
import { } from '@/api/modules';

import type {AuditLogDTO} from '@/api/dto';

const { Search } = Input;

const AuditLogPage: React.FC = () => {
  const [logs, setLogs] = useState<AuditLogDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [query, setQuery] = useState<DTO.AuditLogDTO>({});
  const [loading, setLoading] = useState(false);

  const fetch = async () => {
    setLoading(true);
    try {
      const res = await listAuditLogs({ ...query, page, size });
      setLogs(res.data?.records || []);
      setTotal(res.data?.total || 0);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, [query, page, size]);

  const handleExport = async () => {
    await exportAuditLogs({ ...query, page, size });
    // 可直接 window.open(url) 或下载文件流
  };

  return (
    <div className="p-8 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Search placeholder="用户名/操作类型" allowClear enterButton onSearch={val => setQuery({ ...query, keyword: val })} style={{ width: 320 }} />
        <Button onClick={handleExport}>导出日志</Button>
      </div>
      <Table
        rowKey="id"
        dataSource={logs}
        loading={loading}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          onChange: (p, s) => { setPage(p); setSize(s); }
        }}
        columns={[
          { title: '时间', dataIndex: 'opTime', render: (v: string) => new Date(v).toLocaleString() },
          { title: '用户', dataIndex: 'username' },
          { title: '操作类型', dataIndex: 'opType', render: (v: string) => <Tag>{v}</Tag> },
          { title: '对象', dataIndex: 'target' },
          { title: '详情', dataIndex: 'detail' },
          { title: 'IP', dataIndex: 'ip' }
        ]}
      />
    </div>
  );
};
export default AuditLogPage;
