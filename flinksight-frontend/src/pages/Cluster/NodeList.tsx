/**
 * @file 节点明细与健康管理
 * @desc 展示所有节点的状态、健康、资源利用、批量操作等
 */
import React, {useEffect, useState} from 'react';
import {Button, Input, message, Modal, Space, Table, Tag, Tooltip} from 'antd';
import { api } from 'src/api/gen/client';

import type {NodeDTO} from '../../api/gen/data-contracts.ts';
import NodeDetailModal from './NodeDetailModal';
import {useUser} from '../../store/user';

const { Search } = Input;

const NodeList: React.FC = () => {
  const [list, setList] = useState<NodeDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<NodeDTO>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [detailId, setDetailId] = useState<number | null>(null);
  const { role } = useUser();

  const canEdit = role === 'admin' || role === 'ops';

  // 拉取节点列表
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAllNodes({ ...query, page, size });
      setList(res.data?.records || []);
      setTotal(res.data?.total || 0);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, [query, page, size]);

  // 搜索
  const handleSearch = (val: string) => {
    setQuery({ ...query, keyword: val });
    setPage(1);
  };

  // 节点详情弹窗
  const openDetail = (id: number) => setDetailId(id);

  // 启用/禁用
  async function handleBatchEnable(enable: boolean) {
    await api.batchEnableNodes(selectedRowKeys, enable);
    message.success(enable ? '已启用' : '已禁用');
    setSelectedRowKeys([]);
    fetch();
  }

  // 删除节点
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该节点？',
      onOk: async () => {
        await api.deleteNode(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Search placeholder="节点名/IP/关键字" allowClear enterButton onSearch={handleSearch} style={{ width: 320 }} />
        <Space>
          <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length || !canEdit}>批量启用</Button>
          <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length || !canEdit}>批量禁用</Button>
        </Space>
      </div>
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        rowSelection={{
          selectedRowKeys,
          onChange: (keys) => setSelectedRowKeys(keys as number[])
        }}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          onChange: (p, s) => { setPage(p); setSize(s); }
        }}
        columns={[
          {
            title: '节点名',
            dataIndex: 'name',
            render: (v: string, n: Node) => (
              <Tooltip title="点击查看明细">
                <span className="text-blue-600 cursor-pointer" onClick={() => openDetail(n.id)}>{v}</span>
              </Tooltip>
            )
          },
          { title: 'IP', dataIndex: 'ip' },
          { title: '集群', dataIndex: 'clusterName' },
          { title: '角色', dataIndex: 'role' },
          { title: 'CPU', dataIndex: 'cpuUsage', render: (v: number) => `${v}%` },
          { title: '内存', dataIndex: 'memUsage', render: (v: number) => `${v}%` },
          { title: '健康', dataIndex: 'health', render: (v: string) =>
            <Tag color={v === 'healthy' ? 'green' : v === 'warning' ? 'orange' : 'red'}>
              {v === 'healthy' ? '健康' : v === 'warning' ? '预警' : '异常'}
            </Tag>
          },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag> },
          {
            title: '操作',
            render: (_: any, n: Node) => (
              <Space>
                <Button type="link" size="small" onClick={() => openDetail(n.id)}>明细</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(n.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
      />
      {/* 节点明细弹窗 */}
      {detailId && (
        <NodeDetailModal
          id={detailId}
          open={!!detailId}
          onClose={() => setDetailId(null)}
        />
      )}
    </div>
  );
};
export default NodeList;
