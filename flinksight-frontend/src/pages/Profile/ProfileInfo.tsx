import React, {useEffect, useState} from 'react';
import  api  from 'src/api/gen/client';

import type {ProfileDTO} from '../../api/gen/data-contracts.ts';
import {Avatar, Button, Form, Input, message} from 'antd';

/**
 * 个人资料展示与编辑
 */
const ProfileInfo: React.FC = () => {
  const [profile, setProfile] = useState<ProfileDTO | null>(null);
  const [editing, setEditing] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    api.getProfileByUserId().then(res => {
      setProfile(res.data);
      form.setFieldsValue(res.data);
    });
  }, [form]);

  const handleSubmit = async (values: ProfileDTO) => {
    await api.updateProfile(values);
    message.success('资料已更新');
    setEditing(false);
    api.getProfile().then(res => setProfile(res.data));
  };

  if (!profile) return <div>加载中...</div>;

  return (
    <div>
      <div className="flex items-center mb-4">
        <Avatar src={profile.avatar} size={64}>{profile.nickname?.slice(0, 1)}</Avatar>
        <div className="ml-4">
          <div className="font-bold">{profile.nickname || profile.username}</div>
          <div className="text-gray-500">{profile.role}</div>
        </div>
      </div>
      <Form
        form={form}
        layout="vertical"
        initialValues={profile}
        onFinish={handleSubmit}
        disabled={!editing}
        className="max-w-md"
      >
        <Form.Item label="昵称" name="nickname">
          <Input />
        </Form.Item>
        <Form.Item label="邮箱" name="email">
          <Input type="email" />
        </Form.Item>
        <Form.Item label="手机" name="phone">
          <Input />
        </Form.Item>
        <div>
          {!editing ? (
            <Button type="primary" onClick={() => setEditing(true)}>编辑</Button>
          ) : (
            <span>
              <Button type="primary" htmlType="submit" className="mr-2">保存</Button>
              <Button onClick={() => setEditing(false)}>取消</Button>
            </span>
          )}
        </div>
      </Form>
    </div>
  );
};
export default ProfileInfo;
