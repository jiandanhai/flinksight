/**
 * @file 数据权限管理
 * @desc 管理角色/用户对组织、资源、租户等数据的访问权限
 */
import React, { useEffect, useState } from 'react';
import { Table, Tag, Button } from 'antd';
import http from '@/api/http';

interface DataPerm {
  id: number;
  subject: string;
  subjectType: 'user' | 'role';
  resource: string;
  action: string;
}

const DataPermList: React.FC = () => {
  const [list, setList] = useState<DataPerm[]>([]);
  useEffect(() => {
    http.get('/data-perms').then(res => setList(res.data || []));
  }, []);

  return (
    <Table
      rowKey="id"
      columns={[
        { title: '主体', dataIndex: 'subject' },
        {
          title: '类型', dataIndex: 'subjectType',
          render: (val) => <Tag color={val === 'user' ? 'blue' : 'green'}>{val === 'user' ? '用户' : '角色'}</Tag>
        },
        { title: '资源', dataIndex: 'resource' },
        { title: '操作', dataIndex: 'action' },
        {
          title: '管理',
          render: (_, record) =>
            <Button size="small" danger onClick={async () => {
              await http.delete(`/data-perms/${record.id}`);
              setList(list => list.filter(item => item.id !== record.id));
            }}>删除</Button>
        }
      ]}
      dataSource={list}
    />
  );
};

export default DataPermList;
