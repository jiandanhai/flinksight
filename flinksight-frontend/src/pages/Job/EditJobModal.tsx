import React, {useEffect, useState} from 'react';
import  api  from 'src/api/gen/client';

import type {JobDTO} from '../../api/gen/data-contracts.ts';

interface Props {
  id: number | null;          // null为新建，否则为编辑
  onClose: () => void;
  onOk: () => void;
}

/**
 * 新建/编辑任务弹窗
 * - 可选类型、集群、负责人等字段
 * - 新建/编辑复用
 */
const EditJobModal: React.FC<Props> = ({ id, onClose, onOk }) => {
  const [form, setForm] = useState<JobDTO | JobDTO>({
    name: '', type: '', clusterId: 0, owner: '', config: '', remark: ''
  });
  const [loading, setLoading] = useState(false);

  // 编辑时加载详情
  useEffect(() => {
    if (id) {
      setLoading(true);
      api.getJob(id).then(data => {
        setForm({
          name: data.name, type: data.type, clusterId: data.clusterId, owner: data.owner,
          config: data.config || '', remark: data.remark || ''
        });
      }).finally(() => setLoading(false));
    } else {
      setForm({ name: '', type: '', clusterId: 0, owner: '', config: '', remark: '' });
    }
  }, [id]);

  // 提交
  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    try {
      if (id) {
        await api.updateJob(id, form as JobDTO);
      } else {
        await api.createJob(form as JobDTO);
      }
      onClose();
      onOk();
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-40">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[480px]" onSubmit={handleSubmit}>
        <h3 className="font-bold text-lg mb-4">{id ? '编辑任务' : '新建任务'}</h3>
        <input className="input mb-4" placeholder="任务名" required value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} />
        <input className="input mb-4" placeholder="类型" required value={form.type} onChange={e => setForm(f => ({ ...f, type: e.target.value }))} />
        <input className="input mb-4" placeholder="负责人" value={form.owner} onChange={e => setForm(f => ({ ...f, owner: e.target.value }))} />
        <input className="input mb-4" placeholder="集群ID" type="number" value={form.clusterId} onChange={e => setForm(f => ({ ...f, clusterId: parseInt(e.target.value) || 0 }))} />
        <textarea className="input mb-4" placeholder="任务配置（JSON/YAML）" rows={3} value={form.config} onChange={e => setForm(f => ({ ...f, config: e.target.value }))} />
        <input className="input mb-4" placeholder="备注" value={form.remark} onChange={e => setForm(f => ({ ...f, remark: e.target.value }))} />
        <div className="flex mt-6 justify-end">
          <button className="btn-secondary mr-2" type="button" onClick={onClose}>取消</button>
          <button className="btn-primary" type="submit" disabled={loading}>提交</button>
        </div>
      </form>
    </div>
  );
};

export default EditJobModal;
