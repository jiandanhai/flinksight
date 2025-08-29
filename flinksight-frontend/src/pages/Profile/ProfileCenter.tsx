/**
 * @file 个人信息与安全中心
 * @desc 用户可修改昵称、头像、密码，管理API Token等
 */
import React, { useEffect, useState } from 'react';
import { Button, Card, Form, Input, message, Modal, Upload } from 'antd';
import { getMyProfile, updateProfile, changePassword } from '@/api/modules';

type ProfileFromApi = {
  id?: number;
  userId?: number;
  tenantId?: number;
  realName?: string;
  avatarUrl?: string | null;
  gender?: number;
  department?: string;
  position?: string;
  signature?: string;
  phone?: string;
  email?: string;
};

type ProfileForm = {
  nickname?: string;
  email?: string;
  phone?: string;
  avatar?: string;
};

// 兼容：后端响应可能是 {success, data:{...}} 或直接 {...}
function unwrap<T = any>(res: any): T | undefined {
  if (!res) return undefined;
  const body = 'data' in res ? res.data : res;
  return body?.data ?? body;
}

// API -> 表单
function mapApiToForm(p?: ProfileFromApi): ProfileForm {
  if (!p) return {};
  return {
    nickname: p.realName ?? '',
    email: p.email ?? '',
    phone: p.phone ?? '',
    avatar: p.avatarUrl ?? '',
  };
}

// 表单 -> API
function mapFormToApi(values: ProfileForm): Partial<ProfileFromApi> {
  return {
    realName: values.nickname?.trim(),
    phone: values.phone?.trim(),
    avatarUrl: values.avatar || null,
  };
}

const ProfileCenter: React.FC = () => {
  const [info, setInfo] = useState<ProfileForm>({});
  const [edit, setEdit] = useState(false);
  const [form] = Form.useForm<ProfileForm>();

  // ↓↓↓ 新增：受控密码弹窗与表单实例
  const [pwdOpen, setPwdOpen] = useState(false);
  const [pwdForm] = Form.useForm();

  useEffect(() => {
    (async () => {
      try {
        const res = await getMyProfile();
        const data = unwrap<ProfileFromApi>(res);
        const mapped = mapApiToForm(data);
        setInfo(mapped);
        form.setFieldsValue(mapped);
      } catch {
        message.error('加载个人资料失败');
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 保存基本资料
  const handleSave = async (values: ProfileForm) => {
    const payload = mapFormToApi(values);
    await updateProfile(payload);
    message.success('资料已更新');
    setEdit(false);
    const merged = { ...info, ...values };
    setInfo(merged);
    form.setFieldsValue(merged);
  };

  // 打开修改密码弹窗
  const handlePassword = () => {
    setPwdOpen(true);
  };

  return (
    <div style={{ maxWidth: 560, margin: '0 auto', padding: 32 }}>
      <Card
        title="个人信息"
        extra={!edit ? <Button onClick={() => setEdit(true)}>编辑</Button> : null}
      >
        <Form form={form} layout="vertical" onFinish={handleSave} disabled={!edit}>
          <Form.Item label="昵称" name="nickname" rules={[{ required: true, message: '请输入昵称' }]}>
            <Input placeholder="请输入昵称" />
          </Form.Item>

          <Form.Item label="邮箱" name="email">
            <Input disabled />
          </Form.Item>

          <Form.Item label="手机号" name="phone">
            <Input placeholder="请输入手机号" />
          </Form.Item>

          <Form.Item label="头像" shouldUpdate>
            <Upload
              showUploadList={false}
              action="/api/profile/avatar"
              name="file"
              withCredentials
              onChange={(fileInfo) => {
                const { status, response } = fileInfo.file;
                if (status === 'done') {
                  const url = response?.url || response?.data?.url || '';
                  const merged = { ...form.getFieldsValue(), avatar: url };
                  setInfo(merged);
                  form.setFieldsValue({ avatar: url });
                  message.success('头像已上传');
                } else if (status === 'error') {
                  message.error('头像上传失败');
                }
              }}
            >
              <img
                src={info.avatar || '/avatar_default.png'}
                alt="avatar"
                style={{ width: 80, height: 80, borderRadius: 40, cursor: edit ? 'pointer' : 'default', objectFit: 'cover' }}
              />
            </Upload>
            <Form.Item name="avatar" style={{ display: 'none' }}>
              <Input />
            </Form.Item>
          </Form.Item>

          {edit && (
            <Form.Item>
              <Button type="primary" htmlType="submit">保存</Button>
              <Button style={{ marginLeft: 8 }} onClick={() => { setEdit(false); form.setFieldsValue(info); }}>
                取消
              </Button>
            </Form.Item>
          )}
        </Form>

        <Button style={{ marginTop: 16 }} onClick={handlePassword}>修改密码</Button>
      </Card>

      {/* 受控密码弹窗（稳定） */}
      <Modal
        title="修改密码"
        open={pwdOpen}
        onCancel={() => { setPwdOpen(false); pwdForm.resetFields(); }}
        onOk={() => pwdForm.submit()}
        okText="提交"
        cancelText="取消"
        destroyOnClose
        maskClosable
        width={400}
      >
        <Form
          form={pwdForm}
          layout="vertical"
          onFinish={async (vals: any) => {
            await changePassword({ oldPassword: vals.oldPassword, newPassword: vals.newPassword });
            message.success('密码已修改');
            setPwdOpen(false);
            pwdForm.resetFields();
          }}
        >
          <Form.Item label="原密码" name="oldPassword" rules={[{ required: true, message: '请输入原密码' }]}>
            <Input.Password autoFocus />
          </Form.Item>
          <Form.Item
            label="新密码"
            name="newPassword"
            rules={[{ required: true, min: 6, message: '至少 6 位' }]}
          >
            <Input.Password />
          </Form.Item>
          <Form.Item
            label="确认新密码"
            name="confirmPassword"
            dependencies={['newPassword']}
            rules={[
              { required: true, message: '请再次输入新密码' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('newPassword') === value) return Promise.resolve();
                  return Promise.reject(new Error('两次输入不一致'));
                },
              }),
            ]}
          >
            <Input.Password />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ProfileCenter;
