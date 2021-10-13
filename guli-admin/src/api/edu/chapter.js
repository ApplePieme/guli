import request from '@/utils/request'

const api_name = '/edu/chapter'

export default {
  // 嵌套查询章节列表
  getNestedTreeList(courseId) {
    return request({
      url: `${api_name}/list/${courseId}`,
      method: 'get'
    })
  },
  // 添加章节
  saveChapter(chapter) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: chapter
    })
  },
  // 根据 id 查询章节
  getChapterById(id) {
    return request({
      url: `${api_name}/${id}`,
      method: 'get'
    })
  },
  // 根据 id 修改章节
  updateChapterById(chapter) {
    return request({
      url: `${api_name}/${chapter.id}`,
      method: 'put',
      data: chapter
    })
  },
  // 根据 id 删除章节
  deleteChapterById(id) {
    return request({
      url: `${api_name}/${id}`,
      method: 'delete'
    })
  }
}