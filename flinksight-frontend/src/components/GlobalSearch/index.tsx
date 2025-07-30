/**
 * @file 全局搜索组件
 * @desc 支持多模块多表模糊搜索，带跳转
 */
import React, { useState } from 'react';
import { Input, AutoComplete } from 'antd';
import http from '@/api/http';
import { useNavigate } from 'react-router-dom';

const GlobalSearch: React.FC = () => {
  const [options, setOptions] = useState([]);
  const navigate = useNavigate();

  const handleSearch = async (value: string) => {
    if (value.trim().length === 0) return;
    const res = await http.get('/search', { params: { q: value } });
    setOptions((res.data || []).map((item: any) => ({
      value: item.name,
      label: (
        <span>
          [{item.type}] {item.name}
        </span>
      ),
      path: item.path
    })));
  };

  return (
    <AutoComplete
      style={{ width: 320 }}
      options={options}
      onSearch={handleSearch}
      onSelect={(_, option: any) => navigate(option.path)}
    >
      <Input.Search placeholder="全局搜索（租户/报警/用户/任务等）" allowClear />
    </AutoComplete>
  );
};

export default GlobalSearch;
