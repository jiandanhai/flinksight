import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
// vite.config.ts
export default defineConfig({
  // ...
  server: {
    proxy: {
      //'/api': 'http://localhost:8080',      // SpringBoot后端
      '/mock': 'http://localhost:3001',  // Mock服务（任选）
    }
  }
});
