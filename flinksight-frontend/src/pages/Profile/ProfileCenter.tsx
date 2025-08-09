/**
 * @file 个人信息与安全中心
 * @desc 用户可修改昵称、头像、密码，管理API Token等
 */
import React, { useEffect, useState } from 'react';
import { Button, Card, Form, Input, message, Modal, Upload } from 'antd';
import { api } from 'src/api/gen/client';

const ProfileCenter: React.FC = () => {
  const [info, setInfo] = useState<any>({});
  const [edit, setEdit] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    api.profileControllerGetProfile().then(res => {
      setInfo(res.data || {});
      form.setFieldsValue(res.data || {});
    });
    // eslint-disable-next-line
  }, []);

  // 修改基本资料
  const handleSave = async (values: any) => {
    await api.profileControllerUpdateProfile(values);
    message.success('资料已更新');
    setEdit(false);
    setInfo({ ...info, ...values });
  };

  // 修改密码
  const handlePassword = async () => {
    Modal.confirm({
      title: '修改密码',
      content: (
        <Form
          id="passwordForm"
          layout="vertical"
          onFinish={async (vals) => {
            await api.profileControllerChangePassword(vals);
            message.success('密码已修改');
            Modal.destroyAll();
          }}
        >
          <Form.Item label="原密码" name="oldPassword" rules={[{ required: true }]}><Input.Password /></Form.Item>
          <Form.Item label="新密码" name="newPassword" rules={[{ required: true, min: 6 }]}><Input.Password /></Form.Item>
        </Form>
      ),
      okText: '提交',
      cancelText: '取消',
      onOk: () => {
        const form: any = document.getElementById('passwordForm');
        if (form) (form as HTMLFormElement).dispatchEvent(new Event('submit', { cancelable: true, bubbles: true }));
        return false;
      },
      onCancel: () => Modal.destroyAll(),
      maskClosable: true,
      width: 400
    });
  };

  return (
    <div style={{ maxWidth: 500, margin: '0 auto', padding: 32 }}>
      <Card
        title="个人信息"
        extra={!edit ? <Button onClick={() => setEdit(true)}>编辑</Button> : null}
      >
        <Form
          form={form}
          layout="vertical"
          initialValues={info}
          onFinish={handleSave}
          disabled={!edit}
        >
          <Form.Item label="昵称" name="nickname">
            <Input />
          </Form.Item>
          <Form.Item label="邮箱" name="email">
            <Input disabled />
          </Form.Item>
          <Form.Item label="手机号" name="phone">
            <Input />
          </Form.Item>
          <Form.Item label="头像">
            <Upload
              showUploadList={false}
              action="/api/profile/avatar"
              name="file"
              onChange={fileInfo => {
                if (fileInfo.file.status === 'done') {
                  setInfo({ ...info, avatar: fileInfo.file.response.url });
                  message.success('头像已上传');
                }
              }}
            >
              <img
                src={info.avatar || '/avatar_default.png'}
                alt="avatar"
                style={{ width: 80, height: 80, borderRadius: 40, cursor: 'pointer', objectFit: 'cover' }}
              />
            </Upload>
          </Form.Item>
          {edit && (
            <Form.Item>
              <Button type="primary" htmlType="submit">保存</Button>
              <Button style={{ marginLeft: 8 }} onClick={() => setEdit(false)}>取消</Button>
            </Form.Item>
          )}
        </Form>
        <Button style={{ marginTop: 16 }} onClick={handlePassword}>修改密码</Button>
      </Card>
    </div>
  );
};

export default ProfileCenter;
