import request from '@/utils/request'

const api_name = '/cms/banner'

export default {
    // 添加 banner
    saveBanner(banner) {
        return request({
            url: `${api_name}/save`,
            method: 'post',
            data: banner
        })
    },
    // 分页查询
    queryPage(current, limit) {
        return request({
            url: `${api_name}/query/${current}/${limit}`,
            method: 'get'
        })
    },
    // 根据 ID 查询 banner
    getBannerById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'get'
        })
    },
    // 根据 ID 修改 banner
    updateBannerById(banner) {
        return request({
            url: `${api_name}/${banner.id}`,
            method: 'put',
            data: banner
        })
    },
    // 根据 ID 删除 banner
    removeBannerById(id) {
        return request({
            url: `${api_name}/${id}`,
            method: 'delete'
        })
    }
}