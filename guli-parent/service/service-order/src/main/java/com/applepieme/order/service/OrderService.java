package com.applepieme.order.service;

import com.applepieme.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
public interface OrderService extends IService<Order> {
    /**
     * 根据课程 id 和用户 id 判断该用户是否购买该课程
     *
     * @param courseId 课程 id
     * @param memberId 用户 id
     * @return 是否购买该课程
     */
    boolean isBuyCourse(String courseId, String memberId);

    /**
     * 根据订单号获取订单信息
     *
     * @param orderNo 订单号
     * @return 订单对象
     */
    Order getOrderByOrderNo(String orderNo);

    /**
     * 创建订单
     *
     * @param courseId 课程 id
     * @param memberId 用户 id
     * @return 订单号
     */
    String createOrder(String courseId, String memberId);
}
