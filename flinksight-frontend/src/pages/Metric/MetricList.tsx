import React, { useEffect, useMemo, useState } from 'react';
import { Button, Input, message, Space, Table } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { listMetricDashboards, deleteMetricDashboard } from '@/api/modules';
import type { MetricDashboardDTO } from '@/api/dto';
import EditMetricModal from './EditMetricModal';

const { Search } = Input;

interface Props {
  onSelect: (id: number) => void;
}

/** 抽取列表：兼容 数组 / data / data.data / records / list */
function extractList<T = any>(resp: any): T[] {
  const payload = resp?.data?.data ?? resp?.data ?? resp;
  if (Array.isArray(payload)) return payload;
  if (Array.isArray(payload?.data)) return payload.data;
  if (Array.isArray(payload?.records)) return payload.records;
  if (Array.isArray(payload?.list)) return payload.list;
  return [];
}

const MetricList: React.FC<Props> = ({ onSelect }) => {
  const [rows, setRows] = useState<MetricDashboardDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [keyword, setKeyword] = useState('');

  const data = useMemo(() => {
    const kw = keyword.trim().toLowerCase();
    if (!kw) return rows;
    return rows.filter((m: any) =>
      (m?.name ?? '').toLowerCase().includes(kw) ||
      (m?.code ?? '').toLowerCase().includes(kw) ||
      (m?.tags ?? '').toLowerCase().includes(kw)
    );
  }, [rows, keyword]);

  async function fetchList() {
    setLoading(true);
    try {
      const resp = await listMetricDashboards({});
      const list = extractList<MetricDashboardDTO>(resp).map((it: any, i: number) => ({
        key: it?.id ?? i,
        ...it,
      }));
      setRows(list);
    } catch (e: any) {
      console.error('[metrics] fetch failed:', e);
      message.error(e?.message || '加载失败');
      setRows([]);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchList();
  }, []);

  function openModal(id?: number) {
    setEditId(id ?? null);
    setModalVisible(true);
  }

  async function handleDelete(m: any) {
    if (!window.confirm(`确认删除指标：${m?.name ?? ''}？`)) return;
    setLoading(true);
    try {
      await deleteMetricDashboard(m?.id);
      await fetchList();
    } catch (e: any) {
      message.error(e?.message || '删除失败');
    } finally {
      setLoading(false);
    }
  }

  const columns: ColumnsType<MetricDashboardDTO> = [
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
      ellipsis: true,
      render: (_: any, m: any) => (
        <span
          className="text-blue-600 cursor-pointer"
          title={m?.name}
          onClick={() => onSelect(m?.id)}
        >
          {m?.name ?? '-'}
        </span>
      ),
    },
    {
      title: '标识',
      dataIndex: 'code',
      key: 'code',
      ellipsis: true,
      responsive: ['sm'],
      render: (v: any) => v ?? '-',
    },
    {
      title: '类型',
      dataIndex: 'type',
      key: 'type',
      width: 120,
      responsive: ['md'],
      render: (v: any) => v ?? '-',
    },
    {
      title: '单位',
      dataIndex: 'unit',
      key: 'unit',
      width: 120,
      responsive: ['lg'],
      render: (v: any) => v ?? '-',
    },
    {
      title: '标签',
      dataIndex: 'tags',
      key: 'tags',
      ellipsis: true,
      responsive: ['md'],
      render: (v: any) => v ?? '-',
    },
    {
      title: '操作',
      key: 'op',
      fixed: 'right',
      render: (_: any, m: any) => (
        <Space size="small">
          <Button type="link" size="small" onClick={() => openModal(m?.id)}>
            编辑
          </Button>
          <Button type="link" size="small" danger onClick={() => handleDelete(m)}>
            删除
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div className="space-y-4">
      {/* 工具栏：两侧分布，保持留白 */}
      <div className="flex items-center justify-between gap-4">
        <Space size="middle">
          <Button type="primary" onClick={() => openModal()}>
            新建指标
          </Button>
          <span className="text-gray-500">共 {data.length} 个指标</span>
        </Space>
        <div className="w-full max-w-[420px]">
          <Search
            allowClear
            placeholder="按名称/标识/标签搜索"
            onSearch={(v) => setKeyword(v)}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>
      </div>

      {/* 列表：自适应/省略号/响应式列，窄屏时自动隐藏不重要列并支持横向滚动 */}
      <div className="bg-white rounded-xl shadow p-3">
        <Table<MetricDashboardDTO>
          rowKey="id"
          columns={columns}
          dataSource={data}
          loading={loading}
          pagination={{ pageSize: 10, showSizeChanger: false }}
          sticky
          scroll={{ x: 'max-content' }}     // 宽度不足时允许横向滚动
        />
      </div>

      {modalVisible && (
        <EditMetricModal
          id={editId}
          onClose={() => setModalVisible(false)}
          onOk={fetchList}
        />
      )}
    </div>
  );
};

export default MetricList;
