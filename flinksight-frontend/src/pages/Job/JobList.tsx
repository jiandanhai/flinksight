/**
 * @file 任务/作业管理页
 * @desc 支持任务查询、分页、批量启停、详情、编辑弹窗，权限自动校验（兼容可选 clusterId）
 */
import React, { useEffect, useMemo, useRef, useState } from 'react';
import { Button, Input, message, Modal, Space, Table, Tag, Empty } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { listJobs, deleteJob, batchUpdateStatus } from '../../api/modules';

import type { JobDTO } from '@/api/dto';
import EditJobModal from './EditJobModal';
import JobDetail from './JobDetail';
import { useUser } from '../../store/user';
import { useClusterId } from '@/hooks/useClusterId';   // ✅ 新增导入

const { Search } = Input;

/** 统一抽取分页（兼容多种后端包装） */
function extractPage<T = any>(resp: any) {
  const payload = resp?.data?.data ?? resp?.data ?? resp;
  const list: T[] = Array.isArray(payload)
    ? payload
    : Array.isArray(payload?.data)    ? payload.data
    : Array.isArray(payload?.records) ? payload.records
    : Array.isArray(payload?.list)    ? payload.list
    : [];
  const totalRaw =
    payload?.total ?? payload?.totalElements ?? payload?.totalCount ?? (Array.isArray(list) ? list.length : 0);
  const total = Number(totalRaw) || 0;
  const page  = Number(payload?.page ?? 1) || 1;
  const size  = Number(payload?.size ?? payload?.pageSize ?? 20) || 20;
  return { list, total, page, size, payload };
}

const JobList: React.FC = () => {
  // ✅ 顶层调用 Hook，获得 clusterId（来自 URL 或全局上下文）
  const clusterId = useClusterId();

  const [list, setList] = useState<JobDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<{ keyword?: string }>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [detailId, setDetailId] = useState<number | null>(null);

  const { role } = useUser();
  const canEdit = role === 'admin' || role === 'ops';

  // 防竞态：仅处理最新一次请求的返回
  const reqSeqRef = useRef(0);

  const fetch = async () => {
    // 若你的后端 /api/job/listByCluster 必须要 clusterId，这里缺参就不发请求
    if (!clusterId) { setList([]); setTotal(0); return; }

    setLoading(true);
    const seq = ++reqSeqRef.current;
    try {
      console.log('[clusterId:]', clusterId);
      const params: any = { ...query, page, size, clusterId }; // ✅ 这里直接用变量，不再调用 Hook
      const resp = await listJobs(params);
      if (seq !== reqSeqRef.current) return; // 已过期返回忽略
      const { list: rows, total, payload } = extractPage<JobDTO>(resp);
      console.log('[jobs] payload =', payload);
      console.log('[jobs] rows.length =', rows.length, ' total =', total);
      setList(Array.isArray(rows) ? rows : []);
      setTotal(total);
    } catch (e: any) {
      if (seq !== reqSeqRef.current) return;
      console.error('[jobs] fetch error:', e);
      message.error(e?.message || '加载失败');
      setList([]); setTotal(0);
    } finally {
      if (seq === reqSeqRef.current) setLoading(false);
    }
  };

  useEffect(() => { fetch(); }, [page, size, clusterId, query.keyword]);

  // 搜索
  const handleSearch = (val: string) => {
    setQuery(prev => ({ ...prev, keyword: val?.trim() || undefined }));
    setPage(1);
  };

  // 编辑弹窗
  function openModal(id?: number) {
    setEditId(id ?? null);
    setModalVisible(true);
  }

  // 详情
  if (detailId) return <JobDetail id={detailId} onBack={() => setDetailId(null)} />;

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该任务？',
      onOk: async () => {
        await deleteJob(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  // 批量启用/停用
  async function handleBatchEnable(enable: boolean) {
    if (!selectedRowKeys.length) return;
    await batchUpdateStatus(selectedRowKeys, enable);
    message.success(enable ? '已启用' : '已停用');
    setSelectedRowKeys([]);
    fetch();
  }

  const columns: ColumnsType<JobDTO> = useMemo(() => ([
    {
      title: '任务名',
      dataIndex: 'name',
      render: (v, t) => (
        <span className="text-blue-600 cursor-pointer" onClick={() => setDetailId((t as any).id)}>
          {v}
        </span>
      )
    },
    { title: '负责人', dataIndex: 'owner', render: v => v ?? '-' },
    {
      title: '状态',
      dataIndex: 'enabled',
      render: (_: any, t) => {
        const raw = (t as any).enabled ?? (t as any).status;
        const on = typeof raw === 'boolean' ? raw : Number(raw) === 1;
        return <Tag color={on ? 'green' : 'red'}>{on ? '启用' : '停用'}</Tag>;
      }
    },
    { title: '调度周期', dataIndex: 'cron', render: v => (v ?? v === 0 ? v : '-') },
    {
      title: '上次运行',
      dataIndex: 'lastRun',
      render: (v: any, t) => {
        const ts = v ?? (t as any).lastRunTime;
        return ts ? new Date(ts).toLocaleString() : '-';
      }
    },
    { title: '运行结果', dataIndex: 'lastResult', render: v => v ?? '-' },
    {
      title: '操作',
      render: (_: any, t) => (
        <Space>
          <Button type="link" size="small" onClick={() => openModal((t as any).id)} disabled={!canEdit}>编辑</Button>
          <Button type="link" size="small" danger onClick={() => handleDelete((t as any).id)} disabled={!canEdit}>删除</Button>
        </Space>
      )
    }
  ]), [canEdit]);

  // 缺少 clusterId 时的兜底 UI（如果已接入头部切换器，这里会很快被填上）
  if (!clusterId) {
    return (
      <div className="p-6 bg-white rounded-xl shadow flex items-center justify-center min-h-[320px]">
        <Empty description="缺少 clusterId。请从集群列表进入，或在右上角选择集群。" />
      </div>
    );
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Search
          placeholder="任务名/关键字"
          allowClear
          enterButton
          onSearch={handleSearch}
          style={{ width: 320 }}
          defaultValue={query.keyword}
        />
        <Space>
          <Button type="primary" onClick={() => openModal()} disabled={!canEdit}>新建任务</Button>
          <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length || !canEdit}>批量启用</Button>
          <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length || !canEdit}>批量停用</Button>
        </Space>
      </div>

      <Table<JobDTO>
        rowKey={(r) => Number((r as any).id)}
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
        <EditJobModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};

export default JobList;
