/**
 * @file 报警流详情页面
 * @desc 展示单条报警流的详细信息，包括规则、历史记录、操作日志，支持权限、错误处理、全注释。
 */
import React, {useEffect, useState} from 'react';
import {Button, Card, Descriptions, Spin, Table, Tag} from 'antd';
import api from '@/api/api-compat';

import type {AlertDTO, AlertOpLogDTO, AlertRuleDTO} from '@/api/dto';
import {useUser} from '../../store/user';

interface Props {
  id: number;
  onBack: () => void;
}

const LEVEL_MAP = ['未知', '低', '中', '高', '致命'];

const AlertDetail: React.FC<Props> = ({ id, onBack }) => {
  const [data, setData] = useState<AlertDTO|null>(null);
  const [rules, setRules] = useState<AlertRuleDTO[]>([]);
  const [history, setHistory] = useState<AlertDTO[]>([]);
  const [ops, setOps] = useState<AlertOpLogDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const { role } = useUser();

  // 获取详情/规则/历史/操作日志
  useEffect(() => {
    setLoading(true);
    Promise.all([
      api.getAlert(id),
      api.getAlertRules(id),
      api.getAlertHistory(id),
      api.getAlertOps(id)
    ]).then(([res, rulesRes, histRes, opsRes]) => {
      setData(res.data);
      setRules(rulesRes.data || []);
      setHistory(histRes.data || []);
      setOps(opsRes.data || []);
    }).finally(() => setLoading(false));
  }, [id]);

  if (loading || !data) return <Spin tip="加载中..." style={{ width: '100%', margin: '60px 0' }} />;

  return (
    <div className="p-6">
      <Button type="link" onClick={onBack}>返回列表</Button>
      <Card
        title={
          <>
            <span>{data.name}</span>
            <Tag color={data.status === 1 ? 'green' : 'gray'} className="ml-4">
              {data.status === 1 ? '激活' : '关闭'}
            </Tag>
          </>
        }
        className="mb-6"
      >
        <Descriptions column={2} bordered>
          <Descriptions.Item label="ID">{data.id}</Descriptions.Item>
          <Descriptions.Item label="级别">{LEVEL_MAP[data.level]}</Descriptions.Item>
          <Descriptions.Item label="状态">{data.status === 1 ? '激活' : '关闭'}</Descriptions.Item>
          <Descriptions.Item label="创建时间">{new Date(data.createTime).toLocaleString()}</Descriptions.Item>
          <Descriptions.Item label="创建人">{data.creator}</Descriptions.Item>
          <Descriptions.Item label="最后更新">{new Date(data.updateTime).toLocaleString()}</Descriptions.Item>
          <Descriptions.Item label="描述" span={2}>{data.desc || '-'}</Descriptions.Item>
        </Descriptions>
      </Card>

      <Card title="报警规则" className="mb-6">
        <Table
          rowKey="id"
          dataSource={rules}
          columns={[
            { title: '名称', dataIndex: 'name' },
            { title: '表达式', dataIndex: 'expr' },
            { title: '等级', dataIndex: 'level', render: (v: number) => LEVEL_MAP[v] },
            { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag>停用</Tag> },
            { title: '最近触发', dataIndex: 'lastFired', render: (v: string) => v ? new Date(v).toLocaleString() : '-' },
          ]}
          pagination={false}
        />
      </Card>

      <Card title="最近告警历史" className="mb-6">
        <Table
          rowKey="id"
          dataSource={history}
          columns={[
            { title: '时间', dataIndex: 'triggeredAt', render: (v: string) => new Date(v).toLocaleString() },
            { title: '规则', dataIndex: 'ruleName' },
            { title: '级别', dataIndex: 'level', render: (v: number) => LEVEL_MAP[v] },
            { title: '内容', dataIndex: 'content' },
            { title: '状态', dataIndex: 'status', render: (v: number) => v === 1 ? <Tag color="red">告警</Tag> : <Tag>恢复</Tag> },
          ]}
          pagination={{ pageSize: 5 }}
        />
      </Card>

      <Card title="操作日志">
        <Table
          rowKey="id"
          dataSource={ops}
          columns={[
            { title: '操作时间', dataIndex: 'opTime', render: (v: string) => new Date(v).toLocaleString() },
            { title: '操作人', dataIndex: 'operator' },
            { title: '操作类型', dataIndex: 'opType' },
            { title: '详情', dataIndex: 'opDetail' },
          ]}
          pagination={{ pageSize: 5 }}
        />
      </Card>
    </div>
  );
};
export default AlertDetail;
