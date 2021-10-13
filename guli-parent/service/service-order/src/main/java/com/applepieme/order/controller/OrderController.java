package com.applepieme.order.controller;


import com.applepieme.order.entity.Order;
import com.applepieme.order.service.OrderService;
import com.applepieme.util.JwtUtils;
import com.applepieme.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author applepieme
 * @since 2021-10-08
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/is_buy/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId) {
        return orderService.isBuyCourse(courseId, memberId);
    }

    @ApiOperation(value = "根据订单号获取订单信息")
    @GetMapping("/{orderNo}")
    public R getOrderByOrderNo(@PathVariable("orderNo") String orderNo) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        return R.ok().data("item", order);
    }

    @ApiOperation(value = "创建订单")
    @GetMapping("/save/{courseId}")
    public R createOrder(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return R.error().message("请先登录");
        }
        String orderNo = orderService.createOrder(courseId, memberId);
        return R.ok().data("orderNo", orderNo);
    }
}

