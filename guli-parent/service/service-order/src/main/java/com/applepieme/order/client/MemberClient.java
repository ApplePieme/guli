package com.applepieme.order.client;

import com.applepieme.order.client.impl.MemberClientImpl;
import com.applepieme.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author applepieme
 * @date 2021/10/8 21:56
 */
@Component
@FeignClient(name = "service-user", fallback = MemberClientImpl.class)
public interface MemberClient {
    /**
     * 根据用户 id 获取用户信息
     *
     * @param id 用户id
     * @return 用户信息对象
     */
    @GetMapping("/user/member/{id}")
    MemberVo getMemberVoById(@PathVariable("id") String id);
}
