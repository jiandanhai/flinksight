import React from 'react';

/**
 * 全局Loading指示器
 */
const Loading: React.FC = () => (
  <div className="flex items-center justify-center py-12 text-blue-500">
    <svg className="animate-spin mr-2 h-5 w-5" viewBox="0 0 24 24">
      <circle className="opacity-20" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z" />
    </svg>
    加载中...
  </div>
);
export default Loading;
