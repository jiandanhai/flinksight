/**
 * @file 集群列表（分页、搜索、批量启停、详情/节点状态跳转）
 */
import React, { useEffect, useMemo, useState } from 'react';
import { Button, Input, message, Modal, Space, Table, Tag } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useNavigate, useSearchParams } from 'react-router-dom';

import type { ClusterDTO } from '@/api/dto';
import EditClusterModal from './EditClusterModal';
import ClusterDetail from './ClusterDetail';
import { useUser } from '@/store/user';
import { listClusters, deleteCluster, enableBatch } from '@/api/modules';

const { Search } = Input;

type Props = {
  /** 可选：让外层（index.tsx）控制跳转；不传则内部用 navigate */
  onOpenDetail?: (clusterId: number, tab?: string) => void;
};

// 统一抽取分页：兼容“数组/包一层/包两层/records/list/totalElements/totalCount/total”等
function extractPage<T = any>(resp: any) {
  const payload = resp?.data?.data ?? resp?.data ?? resp;
  const list: T[] = Array.isArray(payload)
      ? payload
      : Array.isArray(payload?.data)    ? payload.data
          : Array.isArray(payload?.records) ? payload.records
              : Array.isArray(payload?.list)    ? payload.list
                  : [];
  const totalRaw =
      payload?.total ??
      payload?.totalElements ??
      payload?.totalCount ??
      (Array.isArray(list) ? list.length : 0);
  const total = Number(totalRaw) || 0;
  return { list, total, payload };
}

const ClusterList: React.FC<Props> = ({ onOpenDetail }) => {
  const nav = useNavigate();
  const [sp] = useSearchParams();
  const detail = sp.get('detail');               // 有 detail 就渲染详情页

  if (detail) {
    // 进入详情页（与列表共用同一路由，不会覆盖侧边栏）
    return <ClusterDetail />;
  }

  const [list, setList] = useState<ClusterDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState<string>();
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);

  const { role } = useUser();
  const canEdit = role === 'admin' || role === 'ops';

  const fetch = async () => {
    setLoading(true);
    try {
      const resp = await listClusters({ page, size, keyword });
      const { list: rows, total } = extractPage<ClusterDTO>(resp);
      setList(rows);
      setTotal(total);
    } catch (e: any) {
      message.error(e?.message || '加载失败');
      setList([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetch(); }, [page, size, keyword]);

  const openModal = (id?: number) => {
    setEditId(id ?? null);
    setModalVisible(true);
  };

  const gotoDetail = (id: number, tab: string) => {
    if (onOpenDetail) {
      onOpenDetail(id, tab);
    } else {
      nav(`/cluster/list?detail=${id}&tab=${tab}`);
    }
  };

  const handleDelete = (id: number) => {
    Modal.confirm({
      title: '确认删除该集群？',
      content: '为避免误操作，这里执行的是“软删除”，数据可在回收站/审计中追溯。',
      onOk: async () => {
        await deleteCluster(id);
        message.success('已删除');
        fetch();
      }
    });
  };

  const handleBatchEnable = async (enable: boolean) => {
    if (!selectedRowKeys.length) return;
    await enableBatch(selectedRowKeys, enable);
    message.success(enable ? '已启用' : '已停用');
    setSelectedRowKeys([]);
    fetch();
  };

  const columns: ColumnsType<ClusterDTO> = useMemo(() => ([
    {
      title: '集群名',
      dataIndex: 'name',
      render: (_: any, t: any) => (
          <span
              className="text-blue-600 cursor-pointer"
              onClick={() => gotoDetail(Number(t.id), 'nodes')}
          >
          {t.name}
        </span>
      )
    },
    { title: '类型', dataIndex: 'type', render: v => <Tag>{v || '-'}</Tag> },
    { title: '负责人', dataIndex: 'owner', render: v => v ?? '-' },
    {
      title: '状态',
      dataIndex: 'enabled',
      render: (_: any, t: any) => {
        const raw = t.enabled ?? t.status;
        const on = typeof raw === 'boolean' ? raw : Number(raw) === 1;
        return <Tag color={on ? 'green' : 'red'}>{on ? '启用' : '停用'}</Tag>;
      }
    },
    {
      title: '健康',
      dataIndex: 'health',
      render: (v: string) =>
          v === 'healthy' ? <Tag color="green">健康</Tag>
              : v === 'warning' ? <Tag color="orange">预警</Tag>
                  : <Tag color="red">异常</Tag>
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      render: (v: any) => {
        if (!v) return '-';
        const ts = typeof v === 'number' ? v : Date.parse(v);
        return Number.isFinite(ts) && ts ? new Date(ts).toLocaleString() : '-';
      }
    },
    {
      title: '操作',
      render: (_: any, t: any) => (
          <Space>
            <Button
                type="link"
                size="small"
                onClick={() => gotoDetail(Number(t.id), 'status')}
            >
              节点状态
            </Button>
            <Button type="link" size="small" onClick={() => openModal(Number(t.id))} disabled={!canEdit}>编辑</Button>
            <Button type="link" size="small" danger onClick={() => handleDelete(Number(t.id))} disabled={!canEdit}>删除</Button>
          </Space>
      )
    }
  ]), [canEdit]);

  return (
      <div className="p-6 bg-white rounded-xl shadow">
        <div className="flex justify-between mb-4">
          <Search
              placeholder="集群名/负责人/关键字"
              allowClear
              enterButton
              onSearch={(val) => { setKeyword(val?.trim() || undefined); setPage(1); }}
              style={{ width: 320 }}
          />
          <Space>
            <Button type="primary" onClick={() => openModal()} disabled={!canEdit}>新建集群</Button>
            <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length || !canEdit}>批量启用</Button>
            <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length || !canEdit}>批量停用</Button>
          </Space>
        </div>

        <Table<ClusterDTO>
            rowKey={(r) => Number((r as any).id)}           // 强制数值，避免字符串 id 造成选择异常
            dataSource={list}
            loading={loading}
            rowSelection={{
              selectedRowKeys,
              onChange: (keys) => setSelectedRowKeys(keys as number[]),
            }}
            pagination={{
              current: page,
              pageSize: size,
              total,
              showSizeChanger: true,
              onChange: (p, s) => { setPage(p); setSize(s ?? size); },
            }}
            columns={columns}
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
