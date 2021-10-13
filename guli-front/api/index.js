import request from '@/utils/request'

export default {
  getHotCourseAndTeacherList() {
    return request({
      url: `/edu/index`,
      method: 'get'
    })
  }
}