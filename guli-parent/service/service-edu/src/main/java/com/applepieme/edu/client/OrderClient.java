package com.applepieme.edu.client;

import com.applepieme.edu.client.impl.OrderClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author applepieme
 * @date 2021/10/9 21:47
 */
@Component
@FeignClient(name = "service-order", fallback = OrderClientImpl.class)
public interface OrderClient {
    /**
     * 根据课程 id 和用户 id 判断该用户是否购买该课程
     *
     * @param courseId 课程 id
     * @param memberId 用户 id
     * @return 是否购买该课程
     */
    @GetMapping("/order/is_buy/{courseId}/{memberId}")
    boolean isBuyCourse(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}
