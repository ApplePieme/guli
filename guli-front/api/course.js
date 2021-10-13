import request from '@/utils/request'

export default {
  // 条件分页查询课程列表
  getPageList(current, limit, searchObj) {
    return request({
      url: `/edu/course/front/${current}/${limit}`,
      method: 'post',
      data: searchObj
    })
  },
  // 获取所有分类数据
  getNestedTreeList() {
    return request({
      url: `/edu/subject`,
      method: 'get'
    })
  },
  // 根据课程id查看课程详情
  getCourseDetailsById(id) {
    return request({
      url: `/edu/course/details/${id}`,
      method: 'get'
    })
  }
}