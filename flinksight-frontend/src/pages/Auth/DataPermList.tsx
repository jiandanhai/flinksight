/**
 * @file 数据权限管理
 * @desc 管理角色/用户对组织、资源、租户等数据的访问权限
 */
import React, { useEffect, useState } from 'react';
import { Button, Table, Tag, message } from 'antd';
import api from '@/api/api-compat';

// 类型建议后端 openapi 自动生成，若有可以直接 import DataPerm
interface DataPerm {
  id: number;
  subject: string;
  subjectType: 'user' | 'role';
  resource: string;
  action: string;
}

const DataPermList: React.FC = () => {
  const [list, setList] = useState<DataPerm[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchList = () => {
    setLoading(true);
    api.dataPermControllerList().then(res => setList(res.data || []))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchList();
    // eslint-disable-next-line
  }, []);

  const handleDelete = async (id: number) => {
    await api.dataPermControllerDelete({ id }); // 按 openapi 实际参数，如果参数不是 {id}，可直接写 id
    setList(list => list.filter(item => item.id !== id));
    message.success('删除成功');
  };

  return (
    <Table
      rowKey="id"
      loading={loading}
      columns={[
        { title: '主体', dataIndex: 'subject' },
        {
          title: '类型',
          dataIndex: 'subjectType',
          render: (val) => (
            <Tag color={val === 'user' ? 'blue' : 'green'}>
              {val === 'user' ? '用户' : '角色'}
            </Tag>
          )
        },
        { title: '资源', dataIndex: 'resource' },
        { title: '操作', dataIndex: 'action' },
        {
          title: '管理',
          render: (_, record) => (
            <Button
              size="small"
              danger
              onClick={() => handleDelete(record.id)}
            >删除</Button>
          )
        }
      ]}
      dataSource={list}
    />
  );
};

export default DataPermList;
