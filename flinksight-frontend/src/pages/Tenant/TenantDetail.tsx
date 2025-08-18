/**
 * @file 租户详情页
 * @desc 展示租户基础信息、关联用户、操作日志
 */
import React, {useEffect, useState} from 'react';
import {Button, Card, Descriptions, Spin, Table} from 'antd';
import api from '@/api/api-compat';

import type {TenantDTO, TenantOpLogDTO, TenantUserDTO} from '@/api/dto';

interface Props {
  id: number;
  onBack: () => void;
}

const TenantDetail: React.FC<Props> = ({ id, onBack }) => {
  const [data, setData] = useState<TenantDTO|null>(null);
  const [users, setUsers] = useState<TenantUserDTO[]>([]);
  const [ops, setOps] = useState<TenantOpLogDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      api.getTenant(id),
      api.getTenantUsers(id),
      api.getTenantOpLog(id)
    ]).then(([res, usersRes, opsRes]) => {
      setData(res.data);
      setUsers(usersRes.data || []);
      setOps(opsRes.data || []);
    }).finally(() => setLoading(false));
  }, [id]);

  if (loading || !data) return <Spin tip="加载中..." />;

  return (
    <div className="p-6">
      <Button type="link" onClick={onBack}>返回租户列表</Button>
      <Card title={data.name}>
        <Descriptions column={2} bordered>
          <Descriptions.Item label="ID">{data.id}</Descriptions.Item>
          <Descriptions.Item label="编码">{data.code}</Descriptions.Item>
          <Descriptions.Item label="负责人">{data.owner}</Descriptions.Item>
          <Descriptions.Item label="手机号">{data.mobile}</Descriptions.Item>
          <Descriptions.Item label="状态">{data.enabled ? '启用' : '禁用'}</Descriptions.Item>
          <Descriptions.Item label="创建时间">{new Date(data.createTime).toLocaleString()}</Descriptions.Item>
        </Descriptions>
      </Card>
      <Card title="关联用户" className="mb-6" style={{ marginTop: 24 }}>
        <Table
          rowKey="id"
          dataSource={users}
          columns={[
            { title: '用户名', dataIndex: 'username' },
            { title: '昵称', dataIndex: 'nickname' },
            { title: '角色', dataIndex: 'role' }
          ]}
          pagination={{ pageSize: 5 }}
        />
      </Card>
      <Card title="操作日志">
        <Table
          rowKey="id"
          dataSource={ops}
          columns={[
            { title: '时间', dataIndex: 'opTime', render: (v: string) => new Date(v).toLocaleString() },
            { title: '操作人', dataIndex: 'operator' },
            { title: '操作类型', dataIndex: 'opType' },
            { title: '详情', dataIndex: 'opDetail' }
          ]}
          pagination={{ pageSize: 5 }}
        />
      </Card>
    </div>
  );
};
export default TenantDetail;
