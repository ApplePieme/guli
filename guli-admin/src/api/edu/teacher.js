import request from '@/utils/request'

const api_name = '/edu/teacher'

export default {
    getPageList(current, limit, teacherQuery) {
        return request({
            url: `${api_name}/query/${current}/${limit}`,
            method: 'post',
            data: teacherQuery
        })
    },
    removeById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete'
        })
    },
    save(teacher) {
        return request({
            url: `${api_name}/save`,
            method: 'post',
            data: teacher
        })
    },
    getById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'get'
        })
    },
    updateById(teacher) {
        return request({
            url: `${api_name}/${teacher.id}`,
            method: 'put',
            data: teacher
        })
    },
    getList() {
        return request({
            url: `${api_name}`,
            method: 'get'
        })
    }
}