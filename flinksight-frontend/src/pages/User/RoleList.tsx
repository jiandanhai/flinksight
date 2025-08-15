/**
 * @file 角色分配/角色管理页
 * @desc 支持所有角色的增删改查、权限设置，API/type全自动联动，权限控制、注释齐全
 */
import React, {useEffect, useState} from 'react';
import {Button, message, Modal, Space, Table, Tag} from 'antd';
import  api  from 'src/api/gen/client';

import type {RoleDTO} from '../../api/gen/data-contracts.ts';
import EditRoleModal from './EditRoleModal';
import RoleDetail from './RoleDetail';
import {useUser} from '../../store/user';

const RoleList: React.FC = () => {
  const [list, setList] = useState<RoleDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [detailId, setDetailId] = useState<number | null>(null);
  const { role } = useUser();

  const canEdit = role === 'admin';

  // 获取所有角色
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAllRoles();
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, []);

  // 详情
  if (detailId) {
    return <RoleDetail id={detailId} onBack={() => setDetailId(null)} />;
  }

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该角色？',
      onOk: async () => {
        await api.deleteRole(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Button type="primary" onClick={() => { setEditId(null); setModalVisible(true); }} disabled={!canEdit}>
          新建角色
        </Button>
      </div>
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        columns={[
          { title: '角色名', dataIndex: 'name', render: (v, r) => (
              <span className="text-blue-600 cursor-pointer" onClick={() => setDetailId(r.id)}>{v}</span>
            )
          },
          { title: '权限', dataIndex: 'permissions', render: (arr: string[]) => arr?.map(p => <Tag key={p}>{p}</Tag>) },
          { title: '描述', dataIndex: 'desc' },
          {
            title: '操作',
            render: (_: any, r: RoleDTO) => (
              <Space>
                <Button type="link" size="small" onClick={() => { setEditId(r.id); setModalVisible(true); }} disabled={!canEdit}>编辑</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(r.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
        pagination={false}
      />
      {modalVisible && (
        <EditRoleModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default RoleList;
