/**
 * @file 租户管理
 * @desc 支持新增、编辑、启用、禁用、删除、批量、权限管理
 */
import React, {useEffect, useState} from 'react';
import {Button, message, Modal, Space, Table, Tag} from 'antd';
import  api  from 'src/api/gen/client';

import type {TenantDTO} from '../../api/gen/data-contracts.ts';
import EditTenantModal from './EditTenantModal';
import {useUser} from '../../store/user';

const TenantList: React.FC = () => {
  const [list, setList] = useState<TenantDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const { role } = useUser();
  const canEdit = role === 'admin';

  // 拉取租户
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAllTenants();
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, []);

  // 启停
  async function handleEnable(t: TenantDTO) {
    await api.updateTenant(t.id, { enabled: !t.enabled });
    message.success(t.enabled ? '已禁用' : '已启用');
    fetch();
  }

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该租户？',
      onOk: async () => {
        await api.deleteTenant(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Button type="primary" onClick={() => { setEditId(null); setModalVisible(true); }} disabled={!canEdit}>
          新增租户
        </Button>
      </div>
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        columns={[
          { title: '租户名', dataIndex: 'name' },
          { title: '管理员', dataIndex: 'admin' },
          { title: '邮箱', dataIndex: 'email' },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag>禁用</Tag> },
          {
            title: '操作',
            render: (_: any, t: TenantDTO) => (
              <Space>
                <Button type="link" size="small" onClick={() => { setEditId(t.id); setModalVisible(true); }} disabled={!canEdit}>编辑</Button>
                <Button type="link" size="small" onClick={() => handleEnable(t)} disabled={!canEdit}>
                  {t.enabled ? '禁用' : '启用'}
                </Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(t.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
        pagination={false}
      />
      {modalVisible && (
        <EditTenantModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default TenantList;
