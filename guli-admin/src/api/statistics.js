import request from '@/utils/request'

const api_name = '/statistics/daily'

export default {
  createStatistics(day) {
    return request({
      url: `${api_name}/${day}`,
      method: 'post'
    })
  },
  getChartData(searchObj) {
    return request({
        url: `${api_name}/${searchObj.type}/${searchObj.begin}/${searchObj.end}`,
        method: 'get'
    })
  }
}