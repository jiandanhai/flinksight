/**
 * @file 角色详情页
 * @desc 展示单个角色详细信息及权限，带返回
 */
import React, { useEffect, useState } from 'react';
import { Card, Descriptions, Tag, Button, Spin } from 'antd';
import { getRoleDetail } from '../../api/role';
import type { Role } from '../../types/role';

interface Props {
  id: number;
  onBack: () => void;
}

const RoleDetail: React.FC<Props> = ({ id, onBack }) => {
  const [data, setData] = useState<Role|null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    getRoleDetail(id).then(res => setData(res.data)).finally(() => setLoading(false));
  }, [id]);

  if (loading || !data) return <Spin tip="加载中..." />;

  return (
    <div className="p-6">
      <Button type="link" onClick={onBack}>返回角色列表</Button>
      <Card title={data.name}>
        <Descriptions column={2} bordered>
          <Descriptions.Item label="ID">{data.id}</Descriptions.Item>
          <Descriptions.Item label="描述">{data.desc}</Descriptions.Item>
          <Descriptions.Item label="权限" span={2}>
            {data.permissions?.map(p => <Tag key={p}>{p}</Tag>)}
          </Descriptions.Item>
        </Descriptions>
      </Card>
    </div>
  );
};
export default RoleDetail;
