import React, { useEffect, useState } from 'react';
import { getSysParams, updateSysParam } from '../../api/settings';
import type { SysParam } from '../../types/settings';
import Loading from '../../components/Loading';

/**
 * 系统参数配置页面
 * - 支持直接编辑和保存
 */
const SysParam: React.FC = () => {
  const [params, setParams] = useState<SysParam[]>([]);
  const [loading, setLoading] = useState(false);

  async function fetchParams() {
    setLoading(true);
    try {
      const data = await getSysParams({});
      setParams(data);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { fetchParams(); }, []);

  // 修改并保存
  async function handleSave(p: SysParam, value: string) {
    setLoading(true);
    try {
      await updateSysParam(p.id, { ...p, value });
      fetchParams();
    } finally {
      setLoading(false);
    }
  }

  if (loading) return <Loading />;

  return (
    <div>
      <table className="min-w-full border mb-4">
        <thead>
          <tr>
            <th className="p-2 border-b">参数名</th>
            <th className="p-2 border-b">值</th>
            <th className="p-2 border-b">描述</th>
            <th className="p-2 border-b">操作</th>
          </tr>
        </thead>
        <tbody>
          {params.map(p => (
            <tr key={p.id}>
              <td className="p-2 border-b">{p.name}</td>
              <td className="p-2 border-b">
                <input className="input" value={p.value} onChange={e => setParams(list =>
                  list.map(item => item.id === p.id ? { ...item, value: e.target.value } : item)
                )} />
              </td>
              <td className="p-2 border-b">{p.desc}</td>
              <td className="p-2 border-b">
                <button className="btn-primary" onClick={() => handleSave(p, p.value)}>保存</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
export default SysParam;
