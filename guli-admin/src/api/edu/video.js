import request from '@/utils/request'

const api_name = '/edu/video'

export default {
    // 添加课时
    saveVideo(video) {
        return request({
            url: `${api_name}/save`,
            method: 'post',
            data: video
        })
    },
    // 删除课时
    deleteVideo(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete'
        })
    },
    // 根据 id 获取课时信息
    getVideoById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'get'
        })
    },
    // 根据 id 修改课时信息
    updateVideoById(video) {
        return request({
            url: `${api_name}/${video.id}`,
            method: 'put',
            data: video
        })
    }
}