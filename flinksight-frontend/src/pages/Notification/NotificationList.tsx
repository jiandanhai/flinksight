/**
 * @file 通知渠道管理
 * @desc 支持新增、编辑、启停、删除、批量、权限自动校验
 */
import React, {useEffect, useState} from 'react';
import {Button, message, Modal, Space, Table, Tag} from 'antd';
import  api  from 'src/api/gen/client';

import type {NotificationDTO} from '../../api/gen/data-contracts.ts';
import EditNotificationModal from './EditNotificationModal';
import {useUser} from '../../store/user';

const NotificationList: React.FC = () => {
  const [list, setList] = useState<NotificationDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const { role } = useUser();
  const canEdit = role === 'admin' || role === 'ops';

  // 拉取渠道
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAllNotifications();
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, []);

  // 启停
  async function handleEnable(n: NotificationDTO) {
    await api.updateNotification(n.id, { enabled: !n.enabled });
    message.success(n.enabled ? '已停用' : '已启用');
    fetch();
  }

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该通知渠道？',
      onOk: async () => {
        await api.deleteNotification(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Button type="primary" onClick={() => { setEditId(null); setModalVisible(true); }} disabled={!canEdit}>
          新增渠道
        </Button>
      </div>
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        columns={[
          { title: '名称', dataIndex: 'name' },
          { title: '类型', dataIndex: 'type' },
          { title: '地址', dataIndex: 'endpoint' },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag>停用</Tag> },
          {
            title: '操作',
            render: (_: any, n: Notification) => (
              <Space>
                <Button type="link" size="small" onClick={() => { setEditId(n.id); setModalVisible(true); }} disabled={!canEdit}>编辑</Button>
                <Button type="link" size="small" onClick={() => handleEnable(n)} disabled={!canEdit}>
                  {n.enabled ? '停用' : '启用'}
                </Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(n.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
        pagination={false}
      />
      {modalVisible && (
        <EditNotificationModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default NotificationList;
