/**
 * @file 用户详情页
 * @desc 展示用户基础信息、历史登录、操作日志等，权限/注释齐全
 */
import React, {useEffect, useState} from 'react';
import {Button, Card, Descriptions, Spin, Table, Tag} from 'antd';
import { getUser, getLoginHistorysByUser } from '@/api/modules';

import type {UserDTO, LoginHistoryDTO, UserOpLogDTO} from '@/api/dto';

interface Props {
  id: number;
  onBack: () => void;
}

const ROLE_LABELS: Record<string, string> = {
  admin: '管理员',
  ops: '运维',
  user: '普通用户'
};

const UserDetail: React.FC<Props> = ({ id, onBack }) => {
  const [data, setData] = useState<UserDTO|null>(null);
  const [loginHistory, setLoginHistory] = useState<LoginHistoryDTO[]>([]);
  const [opLogs, setOpLogs] = useState<UserOpLogDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      getUser(id),
      getLoginHistorysByUser(id),
      getUserOpLog(id)
    ]).then(([res, logins, logs]) => {
      setData(res.data);
      setLoginHistory(logins.data || []);
      setOpLogs(logs.data || []);
    }).finally(() => setLoading(false));
  }, [id]);

  if (loading || !data) return <Spin tip="加载中..." style={{ width: '100%', margin: '60px 0' }} />;

  return (
    <div className="p-6">
      <Button type="link" onClick={onBack}>返回用户列表</Button>
      <Card title={`${data.username}（${data.nickname}）`} className="mb-6">
        <Descriptions column={2} bordered>
          <Descriptions.Item label="ID">{data.id}</Descriptions.Item>
          <Descriptions.Item label="角色">{ROLE_LABELS[data.role]}</Descriptions.Item>
          <Descriptions.Item label="邮箱">{data.email}</Descriptions.Item>
          <Descriptions.Item label="状态">{data.enabled ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag>}</Descriptions.Item>
          <Descriptions.Item label="注册时间">{new Date(data.createTime).toLocaleString()}</Descriptions.Item>
          <Descriptions.Item label="最近登录">{data.lastLogin ? new Date(data.lastLogin).toLocaleString() : '-'}</Descriptions.Item>
        </Descriptions>
      </Card>

      <Card title="最近登录历史" className="mb-6">
        <Table
          rowKey="id"
          dataSource={loginHistory}
          columns={[
            { title: '登录时间', dataIndex: 'loginTime', render: (v: string) => new Date(v).toLocaleString() },
            { title: 'IP', dataIndex: 'ip' },
            { title: '地点', dataIndex: 'location' },
            { title: '客户端', dataIndex: 'userAgent' }
          ]}
          pagination={{ pageSize: 5 }}
        />
      </Card>

      <Card title="操作日志">
        <Table
          rowKey="id"
          dataSource={opLogs}
          columns={[
            { title: '时间', dataIndex: 'opTime', render: (v: string) => new Date(v).toLocaleString() },
            { title: '操作类型', dataIndex: 'opType' },
            { title: '详情', dataIndex: 'opDetail' }
          ]}
          pagination={{ pageSize: 5 }}
        />
      </Card>
    </div>
  );
};
export default UserDetail;
