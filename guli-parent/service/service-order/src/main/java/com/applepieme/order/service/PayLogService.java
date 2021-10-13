package com.applepieme.order.service;

import com.applepieme.order.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
public interface PayLogService extends IService<PayLog> {
    /**
     * 更新订单表中的订单状态，并向支付日志表中添加一条记录
     *
     * @param map 向微信查询支付状态时返回的 map
     * @return 是否更新成功
     */
    boolean updateOrderStatus(Map<String, String> map);

    /**
     * 根据订单号查询支付状态
     *
     * @param orderNo 订单号
     * @return 包含支付状态的 map
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 根据订单号获取微信支付二维码等信息
     *
     * @param orderNo 订单号
     * @return 包含微信支付二维码地址的 map
     */
    Map<String, Object> createNative(String orderNo);
}
