import React, { useEffect, useState } from 'react';
import { createTenant, updateTenant, getTenantDetail } from '../../api/settings';
import type { Tenant, TenantCreateReq, TenantUpdateReq } from '../../types/settings';

interface Props {
  id: number | null;
  onClose: () => void;
  onOk: () => void;
}

/**
 * 新建/编辑租户弹窗
 */
const EditTenantModal: React.FC<Props> = ({ id, onClose, onOk }) => {
  const [form, setForm] = useState<TenantCreateReq | TenantUpdateReq>({ name: '', code: '', desc: '' });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) {
      setLoading(true);
      getTenantDetail(id).then(data => setForm({ name: data.name, code: data.code, desc: data.desc }))
        .finally(() => setLoading(false));
    } else {
      setForm({ name: '', code: '', desc: '' });
    }
  }, [id]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    try {
      if (id) {
        await updateTenant(id, form as TenantUpdateReq);
      } else {
        await createTenant(form as TenantCreateReq);
      }
      onClose();
      onOk();
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-40">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[400px]" onSubmit={handleSubmit}>
        <h3 className="font-bold text-lg mb-4">{id ? '编辑租户' : '新建租户'}</h3>
        <input className="input mb-4" placeholder="名称" required value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} />
        <input className="input mb-4" placeholder="标识" required value={form.code} onChange={e => setForm(f => ({ ...f, code: e.target.value }))} />
        <input className="input mb-4" placeholder="描述" value={form.desc} onChange={e => setForm(f => ({ ...f, desc: e.target.value }))} />
        <div className="flex mt-6 justify-end">
          <button className="btn-secondary mr-2" type="button" onClick={onClose}>取消</button>
          <button className="btn-primary" type="submit" disabled={loading}>提交</button>
        </div>
      </form>
    </div>
  );
};
export default EditTenantModal;
