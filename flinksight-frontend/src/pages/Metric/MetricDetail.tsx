import React, { useEffect, useState } from 'react';
import { getMetricDetail } from '../../api/metric';
import type { Metric } from '../../types/metric';
import Loading from '../../components/Loading';

interface Props {
  id: number;
  onBack: () => void;
}

/**
 * 指标详情页
 */
const MetricDetail: React.FC<Props> = ({ id, onBack }) => {
  const [metric, setMetric] = useState<Metric | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    getMetricDetail(id).then(setMetric).finally(() => setLoading(false));
  }, [id]);

  if (loading || !metric) return <Loading />;

  return (
    <div>
      <button className="mb-6 text-blue-600" onClick={onBack}>← 返回列表</button>
      <h3 className="font-bold text-xl mb-4">{metric.name}</h3>
      <div className="mb-2"><b>标识：</b>{metric.code}</div>
      <div className="mb-2"><b>类型：</b>{metric.type}</div>
      <div className="mb-2"><b>单位：</b>{metric.unit}</div>
      <div className="mb-2"><b>标签：</b>{metric.tags}</div>
      <div className="mb-2"><b>描述：</b>{metric.desc}</div>
      <div className="mb-2"><b>创建时间：</b>{new Date(metric.createTime).toLocaleString()}</div>
    </div>
  );
};
export default MetricDetail;
