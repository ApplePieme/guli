package com.applepieme.order.service.impl;

import com.applepieme.order.client.CourseClient;
import com.applepieme.order.client.MemberClient;
import com.applepieme.order.entity.Order;
import com.applepieme.order.mapper.OrderMapper;
import com.applepieme.order.service.OrderService;
import com.applepieme.order.util.OrderNoUtils;
import com.applepieme.vo.CourseDetailsVo;
import com.applepieme.vo.MemberVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private CourseClient courseClient;

    @Resource
    private MemberClient memberClient;

    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        queryWrapper.eq("status", 1);
        return baseMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public String createOrder(String courseId, String memberId) {
        /* 调用课程模块，根据课程 id 获取课程信息 */
        CourseDetailsVo courseDetailsVo = courseClient.orderGetCourseDetailsVoById(courseId);
        /* 调用用户模块，根据用户 id 获取用户信息 */
        MemberVo memberVo = memberClient.getMemberVoById(memberId);
        /* 创建订单 */
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseDetailsVo.getId());
        order.setCourseTitle(courseDetailsVo.getTitle());
        order.setCourseCover(courseDetailsVo.getCover());
        order.setTeacherName(courseDetailsVo.getTeacherName());
        order.setMemberId(memberVo.getId());
        order.setNickname(memberVo.getNickname());
        order.setMobile(memberVo.getMobile());
        order.setTotalFee(courseDetailsVo.getPrice());
        order.setPayType(1);
        order.setStatus(0);
        baseMapper.insert(order);
        /* 返回订单号 */
        return order.getOrderNo();
    }
}
