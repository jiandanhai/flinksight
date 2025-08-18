/**
 * @file 个人中心/Profile页
 * @desc 展示当前登录用户的基础信息、修改资料、修改密码、历史登录记录等，自动对接API/types，权限、交互、注释齐全
 */
import React, {useEffect, useState} from 'react';
import {Button, Card, Descriptions, Form, Input, message, Modal, Spin, Table} from 'antd';
import {useUser} from '../../store/user';
import api from '@/api/api-compat';

import type {ProfileDTO, UserLoginHistoryDTO} from '@/api/dto';

const ProfilePage: React.FC = () => {
  const { id } = useUser();
  const [data, setData] = useState<ProfileDTO|null>(null);
  const [loginHistory, setLoginHistory] = useState<UserLoginHistoryDTO[]>([]);
  const [editVisible, setEditVisible] = useState(false);
  const [pwdVisible, setPwdVisible] = useState(false);
  const [loading, setLoading] = useState(true);

  // 拉取用户资料和登录历史
  useEffect(() => {
    setLoading(true);
    Promise.all([
      api.getProfile(),
      api.getUserLoginHistory(id)
    ]).then(([res, loginRes]) => {
      setData(res.data);
      setLoginHistory(loginRes.data || []);
    }).finally(() => setLoading(false));
  }, [id]);

  // 修改个人资料
  const handleProfileSave = async (values: any) => {
    await api.updateProfile(values);
    message.success('资料已更新');
    setEditVisible(false);
    setLoading(true);
    const res = await api.getProfile();
    setData(res.data);
    setLoading(false);
  };

  // 修改密码
  const handlePwdSave = async (values: { oldPassword: string; newPassword: string }) => {
    await api.changePassword(values.oldPassword, values.newPassword);
    message.success('密码修改成功，请重新登录');
    setPwdVisible(false);
    // 可跳转到登录页或强制退出
    // window.location.href = '/login';
  };

  if (loading || !data) return <Spin tip="加载中..." />;

  return (
    <div className="p-8">
      {/* 个人信息卡片 */}
      <Card title="个人信息" className="mb-6">
        <Descriptions column={2} bordered>
          <Descriptions.Item label="用户名">{data.username}</Descriptions.Item>
          <Descriptions.Item label="昵称">{data.nickname}</Descriptions.Item>
          <Descriptions.Item label="邮箱">{data.email}</Descriptions.Item>
          <Descriptions.Item label="角色">{data.role}</Descriptions.Item>
          <Descriptions.Item label="状态">{data.enabled ? '启用' : '禁用'}</Descriptions.Item>
          <Descriptions.Item label="注册时间">{new Date(data.createTime).toLocaleString()}</Descriptions.Item>
        </Descriptions>
        <div className="mt-6 flex gap-4">
          <Button onClick={() => setEditVisible(true)} type="primary">修改资料</Button>
          <Button onClick={() => setPwdVisible(true)}>修改密码</Button>
        </div>
      </Card>

      {/* 登录历史 */}
      <Card title="最近登录历史">
        <Table
          rowKey="id"
          dataSource={loginHistory}
          columns={[
            { title: '登录时间', dataIndex: 'loginTime', render: (v: string) => new Date(v).toLocaleString() },
            { title: 'IP', dataIndex: 'ip' },
            { title: '地点', dataIndex: 'location' },
            { title: '客户端', dataIndex: 'userAgent' }
          ]}
          pagination={{ pageSize: 5 }}
        />
      </Card>

      {/* 编辑资料弹窗 */}
      <Modal
        open={editVisible}
        title="修改个人资料"
        onCancel={() => setEditVisible(false)}
        footer={null}
        destroyOnClose
      >
        <Form
          layout="vertical"
          initialValues={data}
          onFinish={handleProfileSave}
        >
          <Form.Item name="nickname" label="昵称" rules={[{ required: true, message: '必填' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="email" label="邮箱" rules={[{ type: 'email', required: true, message: '邮箱格式不正确' }]}>
            <Input />
          </Form.Item>
          <Button type="primary" htmlType="submit" block>保存</Button>
        </Form>
      </Modal>

      {/* 修改密码弹窗 */}
      <Modal
        open={pwdVisible}
        title="修改密码"
        onCancel={() => setPwdVisible(false)}
        footer={null}
        destroyOnClose
      >
        <Form layout="vertical" onFinish={handlePwdSave}>
          <Form.Item name="oldPassword" label="原密码" rules={[{ required: true, message: '必填' }]}>
            <Input.Password />
          </Form.Item>
          <Form.Item name="newPassword" label="新密码" rules={[{ required: true, min: 6, message: '至少6位' }]}>
            <Input.Password />
          </Form.Item>
          <Button type="primary" htmlType="submit" block>提交</Button>
        </Form>
      </Modal>
    </div>
  );
};
export default ProfilePage;
