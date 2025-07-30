import { message, notification } from 'antd';

export const notifyMsg = (content: string, type: 'success'|'error'|'info'|'warning' = 'info') => {
  message[type]?.(content);
};

export const notify = (title: string, desc: string, type: 'success'|'error'|'info'|'warning' = 'info') => {
  notification[type]?.({ message: title, description: desc });
};
