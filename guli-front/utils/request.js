import axios from 'axios'
import { Message } from 'element-ui'
import cookie from 'js-cookie'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8222', // api 的 base_url
  timeout: 5000 // 请求超时时间
})

// request 拦截器
service.interceptors.request.use(request => {
  if (cookie.get('token')) {
    request.headers['token'] = cookie.get('token');
  }
    return request
  },
  err => {
  return Promise.reject(err);
})

// response 拦截器
service.interceptors.response.use(response => {
  const res = response.data
  if (res.code !== 20000) {
    if (res.code === 20002) {
      return response.data
    }
    Message({
      message: res.message,
      type: 'error',
      duration: 5 * 1000
    })
  } else {
    return response.data
  }
})

export default service