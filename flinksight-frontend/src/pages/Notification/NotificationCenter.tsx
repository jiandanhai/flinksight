/**
 * @file 系统消息通知中心
 * @desc 支持平台推送的告警、运营、系统消息查看和批量操作
 */
import React, { useEffect, useState } from 'react';
import { Table, Badge, Button, Modal, message } from 'antd';
import http from '@/api/http';

interface Notification {
  id: number;
  title: string;
  content: string;
  type: 'ALERT' | 'SYSTEM' | 'MARKETING';
  status: 0 | 1; // 0-未读 1-已读
  createTime: string;
}

const NotificationCenter: React.FC = () => {
  const [list, setList] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(false);

  const fetch = async () => {
    setLoading(true);
    const res = await http.get('/notifications');
    setList(res.data || []);
    setLoading(false);
  };
  useEffect(() => { fetch(); }, []);

  // 批量标记已读
  const markRead = async (ids: number[]) => {
    await http.post('/notifications/read', { ids });
    message.success('标记为已读');
    fetch();
  };

  // 批量删除
  const handleDelete = async (ids: number[]) => {
    Modal.confirm({
      title: '确认删除？',
      onOk: async () => {
        await http.delete('/notifications', { data: { ids } });
        message.success('删除成功');
        fetch();
      }
    });
  };

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <Button onClick={() => markRead(list.filter(i => i.status === 0).map(i => i.id))}>全部标记已读</Button>
        <Button danger style={{ marginLeft: 8 }} onClick={() => handleDelete(list.map(i => i.id))}>全部删除</Button>
      </div>
      <Table
        rowKey="id"
        loading={loading}
        dataSource={list}
        columns={[
          { title: '类型', dataIndex: 'type', render: v =>
            v === 'ALERT' ? <Badge status="error" text="报警" /> :
            v === 'SYSTEM' ? <Badge status="processing" text="系统" /> :
            <Badge status="success" text="运营" /> },
          { title: '标题', dataIndex: 'title' },
          { title: '内容', dataIndex: 'content', ellipsis: true },
          { title: '时间', dataIndex: 'createTime' },
          { title: '状态', dataIndex: 'status', render: v => v === 0 ? <Badge color="red" text="未读" /> : <span>已读</span> },
          {
            title: '操作',
            render: (_, record) => (
              <>
                {record.status === 0 &&
                  <Button size="small" onClick={() => markRead([record.id])}>标记已读</Button>}
                <Button danger size="small" onClick={() => handleDelete([record.id])} style={{ marginLeft: 8 }}>删除</Button>
              </>
            )
          }
        ]}
      />
    </div>
  );
};
export default NotificationCenter;
