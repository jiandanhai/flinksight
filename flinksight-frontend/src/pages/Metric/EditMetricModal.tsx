import React, { useEffect, useState } from 'react';
import { createMetric, updateMetric, getMetricDetail } from '../../api/metric';
import type { Metric, MetricCreateReq, MetricUpdateReq } from '../../types/metric';

interface Props {
  id: number | null;
  onClose: () => void;
  onOk: () => void;
}

/**
 * 新建/编辑指标弹窗
 */
const EditMetricModal: React.FC<Props> = ({ id, onClose, onOk }) => {
  const [form, setForm] = useState<MetricCreateReq | MetricUpdateReq>({
    name: '', code: '', desc: '', type: '', unit: '', tags: ''
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) {
      setLoading(true);
      getMetricDetail(id).then(data => setForm({
        name: data.name, code: data.code, desc: data.desc, type: data.type, unit: data.unit, tags: data.tags
      })).finally(() => setLoading(false));
    } else {
      setForm({ name: '', code: '', desc: '', type: '', unit: '', tags: '' });
    }
  }, [id]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    try {
      if (id) {
        await updateMetric(id, form as MetricUpdateReq);
      } else {
        await createMetric(form as MetricCreateReq);
      }
      onClose();
      onOk();
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-40">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[420px]" onSubmit={handleSubmit}>
        <h3 className="font-bold text-lg mb-4">{id ? '编辑指标' : '新建指标'}</h3>
        <input className="input mb-4" placeholder="名称" required value={form.name || ''} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} />
        <input className="input mb-4" placeholder="标识" required value={form.code || ''} onChange={e => setForm(f => ({ ...f, code: e.target.value }))} />
        <input className="input mb-4" placeholder="类型" value={form.type || ''} onChange={e => setForm(f => ({ ...f, type: e.target.value }))} />
        <input className="input mb-4" placeholder="单位" value={form.unit || ''} onChange={e => setForm(f => ({ ...f, unit: e.target.value }))} />
        <input className="input mb-4" placeholder="标签" value={form.tags || ''} onChange={e => setForm(f => ({ ...f, tags: e.target.value }))} />
        <input className="input mb-4" placeholder="描述" value={form.desc || ''} onChange={e => setForm(f => ({ ...f, desc: e.target.value }))} />
        <div className="flex mt-6 justify-end">
          <button className="btn-secondary mr-2" type="button" onClick={onClose}>取消</button>
          <button className="btn-primary" type="submit" disabled={loading}>提交</button>
        </div>
      </form>
    </div>
  );
};
export default EditMetricModal;
