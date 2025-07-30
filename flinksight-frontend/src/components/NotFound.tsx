import React from 'react';

/**
 * 404页面
 */
const NotFound: React.FC = () => (
  <div className="flex flex-col items-center justify-center min-h-screen">
    <h1 className="text-6xl font-bold mb-4">404</h1>
    <div className="text-gray-500 mb-4">页面不存在</div>
    <a href="/" className="btn-primary">返回首页</a>
  </div>
);
export default NotFound;
