import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';
// vite.config.ts
export default defineConfig({
  // ...
  plugins: [react()],
  resolve: {
    alias: {
      src: path.resolve(__dirname, 'src'),
      // 如果你更习惯 @，也可以加上：
      '@': path.resolve(__dirname, 'src'),
    },
  },
  server: {
    host: '0.0.0.0', // 让 Vite 监听所有网卡，包括IPv4和IPv6
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',      // SpringBoot后端
      //'/mock': 'http://localhost:3001',  // Mock服务（任选）
    }
  }
});
