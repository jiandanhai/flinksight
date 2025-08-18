import React, {useEffect, useState} from 'react';
import api from '@/api/api-compat';

import type {RoleDTO} from '@/api/dto';
import EditRoleModal from './EditRoleModal';
import PageTable from '../../components/PageTable';
import Loading from '../../components/Loading';

interface Props {
  onSelect: (id: number) => void;
}

/**
 * 角色列表页面
 * - 支持新建、编辑、删除、详情
 */
const RoleList: React.FC<Props> = ({ onSelect }) => {
  const [roles, setRoles] = useState<RoleDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);

  async function fetchRoles() {
    setLoading(true);
    try {
      const data = await api.getAllRoles({});
      setRoles(data);
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => { fetchRoles(); }, []);

  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  async function handleDelete(role: DTO.RoleDTO) {
    if (!window.confirm(`确认删除角色：${role.name}？`)) return;
    setLoading(true);
    try {
      await api.deleteRole(role.id);
      fetchRoles();
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <div className="mb-4 flex justify-between">
        <button className="btn-primary" onClick={() => openModal()}>新建角色</button>
        <span>共{roles.length}个角色</span>
      </div>
      <PageTable<DTO.RoleDTO>
        columns={[
          { key: 'name', title: '角色名', render: r => (
            <span className="text-blue-600 cursor-pointer" onClick={() => onSelect(r.id)}>{r.name}</span>
          ) },
          { key: 'code', title: '标识' },
          { key: 'desc', title: '描述' },
          {
            key: 'op', title: '操作', render: r => (
              <div>
                <button className="text-blue-600 mr-2" onClick={() => openModal(r.id)}>编辑</button>
                <button className="text-red-500" onClick={() => handleDelete(r)}>删除</button>
              </div>
            )
          }
        ]}
        data={roles}
        loading={loading}
        page={1}
        size={10}
        total={roles.length}
      />
      {/* 编辑弹窗 */}
      {modalVisible && (
        <EditRoleModal
          id={editId}
          onClose={() => setModalVisible(false)}
          onOk={fetchRoles}
        />
      )}
      {loading && <Loading />}
    </div>
  );
};
export default RoleList;
