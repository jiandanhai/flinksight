import React, { useEffect, useState } from 'react';
import { getProfile, updateProfile } from '../../api/profile';
import type { Profile, ProfileUpdateReq } from '../../types/profile';
import { Form, Input, Button, Avatar, message } from 'antd';

/**
 * 个人资料展示与编辑
 */
const ProfileInfo: React.FC = () => {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [editing, setEditing] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    getProfile().then(res => {
      setProfile(res.data);
      form.setFieldsValue(res.data);
    });
  }, [form]);

  const handleSubmit = async (values: ProfileUpdateReq) => {
    await updateProfile(values);
    message.success('资料已更新');
    setEditing(false);
    getProfile().then(res => setProfile(res.data));
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
