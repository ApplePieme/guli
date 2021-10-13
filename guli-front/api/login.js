import request from '@/utils/request'

export default {
  // 用户登录
  submitLogin(userInfo) {
    return request({
      url: `/user/member/login`,
      method: 'post',
      data: userInfo
    })
  },
  // 根据 token 获取用户信息
  getUserInfoByToken() {
    return request({
      url: `/user/member/info`,
      method: 'get'
    })
  }
}