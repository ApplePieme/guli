import request from '@/utils/request'

export default {
  listPageComments(id, current, limit) {
    return request({
      url: `/edu/comment/${id}/${current}/${limit}`,
      method: 'get'
    })
  },
  saveComment(comment) {
    return request({
      url: '/edu/comment/save',
      method: 'post',
      data: comment
    })
  }
}