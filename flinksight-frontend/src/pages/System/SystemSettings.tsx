/**
 * @file 系统设置
 * @desc 多租户参数、SMTP、登录安全等
 */
import React from "react";
import { Button, Card, Form, Input, message, Tabs } from "antd";
import { api } from 'src/api/gen/client';

const SystemSettings: React.FC = () => {
  const [form] = Form.useForm();

  const onFinish = async (values: any) => {
    // 假设 openapi 里有 settingsControllerSetSettings 这样的方法
    await api.settingsControllerSetSettings(values);
    message.success("设置已保存");
  };

  return (
    <Card>
      <Tabs defaultActiveKey="base">
        <Tabs.TabPane tab="基础设置" key="base">
          <Form
            form={form}
            onFinish={onFinish}
            labelCol={{ span: 6 }}
            wrapperCol={{ span: 12 }}
          >
            <Form.Item label="平台名称" name="siteName">
              <Input />
            </Form.Item>
            <Form.Item label="系统域名" name="domain">
              <Input />
            </Form.Item>
            <Form.Item wrapperCol={{ offset: 6 }}>
              <Button type="primary" htmlType="submit">
                保存
              </Button>
            </Form.Item>
          </Form>
        </Tabs.TabPane>
        <Tabs.TabPane tab="邮件/短信" key="mail">
          {/* 可添加SMTP、短信配置表单 */}
        </Tabs.TabPane>
        <Tabs.TabPane tab="安全设置" key="security">
          {/* 登录安全、密码策略表单 */}
        </Tabs.TabPane>
      </Tabs>
    </Card>
  );
};

export default SystemSettings;
