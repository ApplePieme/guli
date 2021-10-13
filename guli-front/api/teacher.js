import request from '@/utils/request'

export default {
  //获取名师列表
  listTeachers(current, limit) {
    return request({
      url: `/edu/teacher/front/${current}/${limit}`,
      method: 'get'
    })
  },
  // 获取讲师详情
  getTeacherDetailsById(id) {
    return request({
      url: `edu/teacher/details/${id}`,
      method: 'get'
    })
  }
}