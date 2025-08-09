/**
 * @file 语言切换按钮
 */
import React from 'react';
import {Button, Dropdown} from 'antd';
import {GlobalOutlined} from '@ant-design/icons';
import {setLocale} from '@/locales';

const LocaleSwitcher: React.FC = () => (
  <Dropdown
    menu={{
      items: [
        { key: 'zh-CN', label: '简体中文' },
        { key: 'en-US', label: 'English' }
      ],
      onClick: ({ key }) => setLocale(key as any)
    }}
  >
    <Button icon={<GlobalOutlined />}>语言</Button>
  </Dropdown>
);

export default LocaleSwitcher;
