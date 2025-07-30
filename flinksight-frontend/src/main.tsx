// src/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import App from './App';
import store from './store';
import 'antd/dist/reset.css';    // Ant Design v5.x 新样式入口
import './assets/global.css';   // 可自定义全局样式
import './theme';        // 引入全局样式/主题
import './i18n';         // 国际化初始化

/**
 * 项目主入口，注入全局状态管理与路由
 */
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Provider store={store}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </Provider>
  </React.StrictMode>
);
