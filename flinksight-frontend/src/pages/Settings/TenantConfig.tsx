import React, {useEffect, useState} from 'react';
import { api } from 'src/api/gen/client';

import type {TenantDTO} from '../../api/gen/data-contracts.ts';
import EditTenantModal from './EditTenantModal';
import PageTable from '../../components/PageTable';
import Loading from '../../components/Loading';

/**
 * 租户配置页面
 * - 支持新建/编辑/删除
 */
const TenantConfig: React.FC = () => {
  const [tenants, setTenants] = useState<TenantDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);

  async function fetchTenants() {
    setLoading(true);
    try {
      const data = await api.getAllTenants({});
      setTenants(data);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { fetchTenants(); }, []);

  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  async function handleDelete(t: TenantDTO) {
    if (!window.confirm(`确认删除租户：${t.name}？`)) return;
    setLoading(true);
    try {
      await api.deleteTenant(t.id);
      fetchTenants();
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <div className="mb-4 flex justify-between">
        <button className="btn-primary" onClick={() => openModal()}>新建租户</button>
        <span>共{tenants.length}个租户</span>
      </div>
      <PageTable<Tenant>
        columns={[
          { key: 'name', title: '名称' },
          { key: 'code', title: '标识' },
          { key: 'desc', title: '描述' },
          {
            key: 'op', title: '操作', render: t => (
              <div>
                <button className="text-blue-600 mr-2" onClick={() => openModal(t.id)}>编辑</button>
                <button className="text-red-500" onClick={() => handleDelete(t)}>删除</button>
              </div>
            )
          }
        ]}
        data={tenants}
        loading={loading}
        page={1}
        size={10}
        total={tenants.length}
      />
      {/* 编辑弹窗 */}
      {modalVisible && (
        <EditTenantModal
          id={editId}
          onClose={() => setModalVisible(false)}
          onOk={fetchTenants}
        />
      )}
      {loading && <Loading />}
    </div>
  );
};
export default TenantConfig;
