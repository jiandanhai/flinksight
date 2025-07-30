import React, { useEffect, useState } from 'react';
import { getMetrics, deleteMetric } from '../../api/metric';
import type { Metric } from '../../types/metric';
import EditMetricModal from './EditMetricModal';
import PageTable from '../../components/PageTable';
import Loading from '../../components/Loading';

interface Props {
  onSelect: (id: number) => void;
}

/**
 * 指标列表页面
 */
const MetricList: React.FC<Props> = ({ onSelect }) => {
  const [metrics, setMetrics] = useState<Metric[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);

  async function fetchMetrics() {
    setLoading(true);
    try {
      const data = await getMetrics({});
      setMetrics(data);
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => { fetchMetrics(); }, []);

  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  async function handleDelete(m: Metric) {
    if (!window.confirm(`确认删除指标：${m.name}？`)) return;
    setLoading(true);
    try {
      await deleteMetric(m.id);
      fetchMetrics();
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <div className="mb-4 flex justify-between">
        <button className="btn-primary" onClick={() => openModal()}>新建指标</button>
        <span>共{metrics.length}个指标</span>
      </div>
      <PageTable<Metric>
        columns={[
          { key: 'name', title: '名称', render: m => (
            <span className="text-blue-600 cursor-pointer" onClick={() => onSelect(m.id)}>{m.name}</span>
          ) },
          { key: 'code', title: '标识' },
          { key: 'type', title: '类型' },
          { key: 'unit', title: '单位' },
          { key: 'tags', title: '标签' },
          {
            key: 'op', title: '操作', render: m => (
              <div>
                <button className="text-blue-600 mr-2" onClick={() => openModal(m.id)}>编辑</button>
                <button className="text-red-500" onClick={() => handleDelete(m)}>删除</button>
              </div>
            )
          }
        ]}
        data={metrics}
        loading={loading}
        page={1}
        size={10}
        total={metrics.length}
      />
      {/* 编辑弹窗 */}
      {modalVisible && (
        <EditMetricModal
          id={editId}
          onClose={() => setModalVisible(false)}
          onOk={fetchMetrics}
        />
      )}
      {loading && <Loading />}
    </div>
  );
};
export default MetricList;
