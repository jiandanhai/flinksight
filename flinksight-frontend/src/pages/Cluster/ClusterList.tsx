/**
 * @file 集群列表
 * @desc 支持分页、搜索、批量启用/停用、健康检查、集群详情、节点扩容，API/types联动
 */
import React, {useEffect, useState} from 'react';
import {Button, Input, message, Modal, Space, Table, Tag} from 'antd';
import { api } from 'src/api/gen/client';

import type {ClusterDTO} from '../../api/gen/data-contracts.ts';
import EditClusterModal from './EditClusterModal';
import ClusterDetail from './ClusterDetail';
import {useUser} from '../../store/user';

const { Search } = Input;

const ClusterList: React.FC = () => {
  const [list, setList] = useState<ClusterDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<ClusterDTO>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [detailId, setDetailId] = useState<number | null>(null);
  const { role } = useUser();

  const canEdit = role === 'admin' || role === 'ops';

  // 拉取集群列表
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAllClusters({ ...query, page, size });
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

  // 新建/编辑弹窗
  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  // 详情
  if (detailId) {
    return <ClusterDetail id={detailId} onBack={() => setDetailId(null)} />;
  }

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该集群？',
      onOk: async () => {
        await api.deleteCluster(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  // 批量启用/停用
  async function handleBatchEnable(enable: boolean) {
    await api.batchEnableClusters(selectedRowKeys, enable);
    message.success(enable ? '已启用' : '已停用');
    setSelectedRowKeys([]);
    fetch();
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Search placeholder="集群名/负责人/关键字" allowClear enterButton onSearch={handleSearch} style={{ width: 320 }} />
        <Space>
          <Button type="primary" onClick={() => openModal()} disabled={!canEdit}>新建集群</Button>
          <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length || !canEdit}>批量启用</Button>
          <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length || !canEdit}>批量停用</Button>
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
            title: '集群名',
            dataIndex: 'name',
            render: (_: any, t: Cluster) =>
              <span className="text-blue-600 cursor-pointer" onClick={() => setDetailId(t.id)}>{t.name}</span>
          },
          { title: '类型', dataIndex: 'type', render: v => <Tag>{v}</Tag> },
          { title: '负责人', dataIndex: 'owner' },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag color="red">停用</Tag> },
          { title: '健康', dataIndex: 'health', render: (v: string) =>
            <Tag color={v === 'healthy' ? 'green' : v === 'warning' ? 'orange' : 'red'}>
              {v === 'healthy' ? '健康' : v === 'warning' ? '预警' : '异常'}
            </Tag>
          },
          { title: '创建时间', dataIndex: 'createTime', render: (v: string) => new Date(v).toLocaleString() },
          {
            title: '操作',
            render: (_: any, t: Cluster) => (
              <Space>
                <Button type="link" size="small" onClick={() => openModal(t.id)} disabled={!canEdit}>编辑</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(t.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
      />
      {modalVisible && (
        <EditClusterModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default ClusterList;
