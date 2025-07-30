import React from 'react';

/**
 * 403页面
 */
const Forbidden: React.FC = () => (
  <div className="flex flex-col items-center justify-center min-h-screen">
    <h1 className="text-6xl font-bold mb-4">403</h1>
    <div className="text-gray-500 mb-4">无权限访问</div>
    <a href="/" className="btn-primary">返回首页</a>
  </div>
);
export default Forbidden;
