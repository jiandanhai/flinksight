/**
 * @file 通知中心
 * @desc 展示系统级消息/任务/报警推送，支持已读未读切换、批量操作、类型过滤
 */
import React, { useEffect, useState } from 'react';
import { Button, Table, Tag, Select, message } from 'antd';
import  api  from 'src/api/gen/client';

// 自动生成的DTO建议用 NotificationDTO
// interface NotificationDTO { ... }

const api = new Api();

const NOTIFICATION_TYPE_LABEL: Record<string, string> = {
  ALERT: '报警',
  SYSTEM: '系统',
  MARKETING: '运营',
  TASK: '任务',
};

const NotificationList: React.FC = () => {
  const [list, setList] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [typeFilter, setTypeFilter] = useState<string | undefined>();

  // 拉取通知列表
  const fetchList = async () => {
    setLoading(true);
    // 支持按类型过滤
    const res = await api.notificationControllerList({ type: typeFilter });
    setList(res.data || []);
    setLoading(false);
  };

  useEffect(() => { fetchList(); }, [typeFilter]);

  // 单条标记已读
  const markRead = async (id: number) => {
    await api.notificationControllerMarkRead({ id });
    setList(list => list.map(item => item.id === id ? { ...item, status: 'READ' } : item));
    message.success('已标记为已读');
  };

  // 批量已读
  const markAllRead = async () => {
    const unreadIds = list.filter(i => i.status === 'UNREAD').map(i => i.id);
    if (unreadIds.length === 0) return message.info('没有未读通知');
    await api.notificationControllerBatchRead({ ids: unreadIds });
    fetchList();
    message.success('全部标记为已读');
  };

  // 批量删除
  const handleBatchDelete = async () => {
    const ids = list.map(i => i.id);
    if (!ids.length) return;
    await api.notificationControllerDelete({ ids });
    fetchList();
    message.success('删除成功');
  };

  return (
    <div>
      <div style={{ marginBottom: 12 }}>
        <Select
          allowClear
          style={{ width: 120, marginRight: 8 }}
          placeholder="类型筛选"
          onChange={setTypeFilter}
        >
          {Object.entries(NOTIFICATION_TYPE_LABEL).map(([k, v]) =>
            <Select.Option key={k} value={k}>{v}</Select.Option>
          )}
        </Select>
        <Button onClick={markAllRead}>全部标记已读</Button>
        <Button danger style={{ marginLeft: 8 }} onClick={handleBatchDelete}>全部删除</Button>
      </div>
      <Table
        rowKey="id"
        loading={loading}
        dataSource={list}
        columns={[
          { title: '标题', dataIndex: 'title' },
          {
            title: '类型', dataIndex: 'type',
            render: t => <Tag color={t === 'ALERT' ? 'red' : 'blue'}>{NOTIFICATION_TYPE_LABEL[t] || t}</Tag>
          },
          { title: '内容', dataIndex: 'content', ellipsis: true },
          { title: '时间', dataIndex: 'createTime' },
          {
            title: '状态', dataIndex: 'status',
            render: (v, r) =>
              v === 'UNREAD'
                ? <Button type="link" size="small" onClick={() => markRead(r.id)}>标记已读</Button>
                : <Tag color="green">已读</Tag>
          }
        ]}
      />
    </div>
  );
};

export default NotificationList;
