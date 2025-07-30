/**
 * 个人中心相关类型
 */
export interface Profile {
  id: number;
  username: string;
  nickname: string;
  email: string;
  phone?: string;
  avatar?: string;
  role: string;
  createTime: string;
}

export interface ProfileUpdateReq {
  nickname?: string;
  email?: string;
  phone?: string;
  avatar?: string;
}

export interface PasswordUpdateReq {
  oldPassword: string;
  newPassword: string;
}

export interface LoginLog {
  id: number;
  ip: string;
  device: string;
  time: string;
  status: string; // success/failed
}

//绑定微信/手机号
export interface Profile {
  // ...
  phone?: string;
  wechatOpenid?: string; // 可选，绑定状态
  wechatNickname?: string;
  // ...
}
