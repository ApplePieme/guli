import request from '@/utils/request'

export default {
  getPlayAuth(id) {
    return request({
      url: `/vod/video/auth/${id}`,
      method: 'get'
    })
  }
}