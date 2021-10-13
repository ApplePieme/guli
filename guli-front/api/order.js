import request from '@/utils/request'

export default {
  // 创建订单
  createOrder(courseId) {
    return request({
      url: `/order/save/${courseId}`,
      method: 'get'
    })
  },
  // 根据订单号查询订单信息
  getOrderByOrderNo(orderNo) {
    return request({
      url: `/order/${orderNo}`,
      method: 'get'
    })
  },
  // 生成支付二维码
  createNative(orderNo) {
    return request({
      url: `/order/pay_log/create_native/${orderNo}`,
      method: 'get'
    })
  },
  // 查询订单状态
  queryOrderStatus(orderNo) {
    return request({
      url: `/order/pay_log/pay_status/${orderNo}`,
      method: 'get'
    })
  }
}