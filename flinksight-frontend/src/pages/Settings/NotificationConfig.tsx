/**
 * @file 通知渠道配置
 * @desc 支持邮件、短信、Webhook等多渠道编辑、测试、启用禁用，自动API/type对接
 */
import React, { useEffect, useState } from 'react';
import { Table, Button, Input, Space, Tag, Modal, Switch, message } from 'antd';
import { getNotifyChannels, updateNotifyChannel, deleteNotifyChannel, createNotifyChannel, testNotifyChannel } from '../../api/settings';
import type { NotifyChannel, NotifyChannelCreateReq, NotifyChannelUpdateReq } from '../../types/settings';
import EditNotifyModal from './EditNotifyModal';
import { useUser } from '../../store/user';

const { Search } = Input;

const NotificationConfig: React.FC = () => {
  const [list, setList] = useState<NotifyChannel[]>([]);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<string>('');
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const { role } = useUser();

  const canEdit = role === 'admin';

  const fetch = async () => {
    setLoading(true);
    try {
      const res = await getNotifyChannels({ keyword: query });
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, [query]);

  // 搜索
  const handleSearch = (val: string) => setQuery(val);

  // 编辑弹窗
  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该通知渠道？',
      onOk: async () => {
        await deleteNotifyChannel(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  // 启用/禁用
  async function handleToggle(channel: NotifyChannel) {
    await updateNotifyChannel(channel.id, { enabled: !channel.enabled } as NotifyChannelUpdateReq);
    message.success(channel.enabled ? '已禁用' : '已启用');
    fetch();
  }

  // 测试通知
  async function handleTest(channel: NotifyChannel) {
    await testNotifyChannel(channel.id);
    message.success('通知测试已发送');
  }

  return (
    <div>
      <div className="flex justify-between mb-4">
        <Search placeholder="通知渠道名称/类型" allowClear enterButton onSearch={handleSearch} style={{ width: 320 }} />
        <Button type="primary" onClick={() => openModal()} disabled={!canEdit}>新建渠道</Button>
      </div>
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        columns={[
          { title: '名称', dataIndex: 'name' },
          { title: '类型', dataIndex: 'type', render: (v: string) => <Tag>{v}</Tag> },
          { title: '配置', dataIndex: 'config', render: (v: any) => <span style={{ wordBreak: 'break-all' }}>{JSON.stringify(v)}</span> },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag> },
          {
            title: '操作',
            render: (_: any, r: NotifyChannel) => (
              <Space>
                <Button size="small" type="link" onClick={() => openModal(r.id)} disabled={!canEdit}>编辑</Button>
                <Button size="small" type="link" onClick={() => handleToggle(r)} disabled={!canEdit}>{r.enabled ? '禁用' : '启用'}</Button>
                <Button size="small" type="link" onClick={() => handleTest(r)}>测试</Button>
                <Button size="small" type="link" danger onClick={() => handleDelete(r.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
        pagination={false}
      />
      {modalVisible && (
        <EditNotifyModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default NotificationConfig;
