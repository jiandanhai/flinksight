/**
 * @file 通知中心
 * @desc 展示系统级消息/任务/报警推送，支持已读未读切换
 */
import React, { useEffect, useState } from 'react';
import { Table, Tag, Button } from 'antd';
import http from '@/api/http';

interface Notification {
  id: number;
  title: string;
  content: string;
  type: string;
  status: 'READ' | 'UNREAD';
  createTime: string;
}

const NotificationList: React.FC = () => {
  const [list, setList] = useState<Notification[]>([]);

  useEffect(() => {
    http.get('/notifications').then(res => setList(res.data || []));
  }, []);

  const markRead = async (id: number) => {
    await http.post(`/notifications/${id}/read`);
    setList(list => list.map(item => item.id === id ? { ...item, status: 'READ' } : item));
  };

  return (
    <Table
      rowKey="id"
      columns={[
        { title: '标题', dataIndex: 'title' },
        { title: '类型', dataIndex: 'type', render: t => <Tag color={t === 'ALERT' ? 'red' : 'blue'}>{t}</Tag> },
        { title: '内容', dataIndex: 'content' },
        { title: '时间', dataIndex: 'createTime' },
        {
          title: '状态', dataIndex: 'status', render: (v, r) =>
            v === 'UNREAD'
              ? <Button type="link" onClick={() => markRead(r.id)}>标记已读</Button>
              : <Tag color="green">已读</Tag>
        }
      ]}
      dataSource={list}
    />
  );
};

export default NotificationList;
