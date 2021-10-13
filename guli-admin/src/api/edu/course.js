import request from '@/utils/request'

const api_name = '/edu/course'

export default {
  // 添加课程信息
  saveCourseInfo(courseInfo) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: courseInfo
    })
  },
  // 查询所有讲师
  findAllTeachers() {
    return request({
      url: '/edu/teacher',
      method: 'get'
    })
  },
  // 根据 id 查询课程
  getCourseInfoById(courseId) {
    return request({
      url: `${api_name}/${courseId}`,
      method: 'get'
    })
  },
  // 根据 id 修改课程信息
  updateCourseInfoById(courseInfo) {
    return request({
      url: `${api_name}/${courseInfo.id}`,
      method: 'put',
      data: courseInfo
    })
  },
  // 根据 id 获取要发布课程的信息
  getCoursePublishInfoById(id) {
    return request({
      url: `${api_name}/publish/${id}`,
      method: 'get'
    })
  },
  // 发布课程
  publishCourse(id) {
    return request({
      url: `${api_name}/publish/${id}`,
      method: 'put'
    })
  },
  // 条件组合分页查询
  getPageList(current, limit, courseQuery) {
    return request({
      url: `${api_name}/query/${current}/${limit}`,
      method: 'post',
      data: courseQuery
    })
  },
  // 根据 ID 删除课程
  removeById(id) {
    return request({
      url: `${api_name}/${id}`,
      method: 'delete'
    })
  }
}