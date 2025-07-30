import React from 'react';

const GlobalLoading: React.FC<{show: boolean, text?: string}> = ({ show, text }) =>
  show
    ? <div className="fixed top-0 left-0 w-full h-full bg-gray-200 bg-opacity-60 flex items-center justify-center z-50">
        <div className="bg-white shadow-xl px-8 py-4 rounded-xl flex items-center gap-3">
          <span className="animate-spin h-6 w-6 border-2 border-blue-500 border-t-transparent rounded-full inline-block" />
          <span className="text-lg">{text || '加载中…'}</span>
        </div>
      </div>
    : null;

export default GlobalLoading;
